package com.example.nct_lite.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.nct_lite.R
import com.example.nct_lite.data.song.response.SongMetadata
import com.example.nct_lite.ui.queue.QueueBottomSheetFragment
import com.example.nct_lite.viewmodel.player.PlayerViewModel
import com.example.nct_lite.viewmodel.song.SongViewModel
import com.example.nct_lite.viewmodel.song.SongViewModelFactory
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class SongViewActivity : AppCompatActivity() {

    private val songViewModel: SongViewModel by viewModels { SongViewModelFactory() }
    private val playerVM: PlayerViewModel by viewModels()

    private lateinit var albumArt: ImageView
    private lateinit var albumTitle: TextView
    private lateinit var songTitle: TextView
    private lateinit var artistName: TextView
    private lateinit var progressBar: SeekBar
    private lateinit var tvCurrentTime: TextView
    private lateinit var tvTotalTime: TextView
    private lateinit var btnShuffle: ImageButton
    private lateinit var btnPrev: ImageButton
    private lateinit var btnPlay: ImageButton
    private lateinit var btnNext: ImageButton
    private lateinit var btnRepeat: ImageButton
    private lateinit var btnBack: ImageButton
    private var currentSong: SongMetadata? = null

    private var isDragging = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.song_view)

        bindViews()
        observeSongDetail()
        observePlayerState()
        setupSeekBar()

        val songId = intent.getStringExtra(EXTRA_SONG_ID)
        val fallbackTitle = intent.getStringExtra(EXTRA_SONG_TITLE)
        val fallbackArtist = intent.getStringExtra(EXTRA_SONG_ARTIST)
        val fallbackCover = intent.getStringExtra(EXTRA_SONG_COVER)

        updateSongTexts(
            title = fallbackTitle ?: getString(R.string.app_name),
            artist = fallbackArtist ?: "",
            genres = emptyList()
        )
        loadCover(fallbackCover)

        songId?.let { songViewModel.getSongById(it) }
    }

    private fun bindViews() {
        albumArt = findViewById(R.id.album_art)
        albumTitle = findViewById(R.id.album_title)
        songTitle = findViewById(R.id.song_title)
        artistName = findViewById(R.id.artist_name)
        progressBar = findViewById(R.id.progress_bar)
        tvCurrentTime = findViewById(R.id.tvCurrentTime)
        tvTotalTime = findViewById(R.id.tvTotalTime)

        val imvQueue = findViewById<ImageView>(R.id.imvQueue)

        btnShuffle = findViewById(R.id.btn_shuffle)
        btnPrev = findViewById(R.id.btn_prev)
        btnPlay = findViewById(R.id.btn_play)
        btnNext = findViewById(R.id.btn_next)
        btnRepeat = findViewById(R.id.btn_repeat)
        btnBack = findViewById(R.id.btnBack)

        imvQueue.setOnClickListener { showQueue() }

        btnPlay.setOnClickListener { playerVM.pauseOrResume() }

        btnBack.setOnClickListener { finish() }

        btnNext.setOnClickListener { playerVM.skipNext() }
        btnPrev.setOnClickListener { playerVM.skipPrevious()}
        btnShuffle.setOnClickListener { Toast.makeText(this, "Shuffle", Toast.LENGTH_SHORT).show() }
        btnRepeat.setOnClickListener { Toast.makeText(this, "Repeat", Toast.LENGTH_SHORT).show() }

        progressBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun observeSongDetail() {
        songViewModel.songDetail.observe(this) { result ->
            result.onSuccess { response ->
                bindSong(response.metadata)
            }
            result.onFailure {
                Toast.makeText(this, "Not to load song", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun bindSong(song: SongMetadata) {
        currentSong = song
        updateSongTexts(song.title, song.artist, song.genreIDs.map { it.name })
        loadCover(song.coverUrl)
        playerVM.playSong(song)
    }

    private fun updateSongTexts(title: String, artist: String, genres: List<String>) {
        songTitle.text = title
        artistName.text = artist
        albumTitle.text = if (genres.isNotEmpty()) {
            genres.joinToString(" • ")
        } else {
            getString(R.string.app_name)
        }
    }

    private fun loadCover(coverUrl: String?) {
        if (coverUrl.isNullOrEmpty()) {
            albumArt.setImageResource(R.drawable.ic_avatar_foreground)
        } else {
            Picasso.get()
                .load(coverUrl)
                .placeholder(R.drawable.ic_avatar_background)
                .into(albumArt)
        }
    }

    private fun observePlayerState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                playerVM.playerState.collect { state ->

                    // 1. Cập nhật nút Play/Pause (Giữ nguyên)
                    btnPlay.setImageResource(
                        if (state.isPlaying) R.drawable.ic_pause_around else R.drawable.ic_play_around
                    )

                    // 2. Cập nhật thông tin bài hát (SỬA LẠI ĐOẠN NÀY)
                    // Chỉ cần kiểm tra state có dữ liệu không, bỏ điều kiện currentSong == null đi
                    if (state.title.isNotEmpty()) {

                        // Để tránh load lại ảnh khi state cập nhật tiến độ (mỗi 200ms),
                        // ta chỉ cập nhật khi tên bài hát THỰC SỰ thay đổi
                        if (songTitle.text.toString() != state.title) {
                            songTitle.text = state.title
                            artistName.text = state.artist
                            albumTitle.text = state.artist // Hoặc thông tin album nếu có trong state
                            loadCover(state.coverUrl)
                        }
                    }

                    // 3. Cập nhật Thanh tiến độ (Giữ nguyên)
                    if (state.duration > 0) {
                        progressBar.max = state.duration
                        tvTotalTime.text = formatTime(state.duration)
                    }

                    if (!isDragging) {
                        progressBar.progress = state.currentPosition
                        tvCurrentTime.text = formatTime(state.currentPosition)
                    }
                }
            }
        }
    }
    private fun setupSeekBar() {
        progressBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    tvCurrentTime.text = formatTime(progress)
                }
            }


            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isDragging = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                isDragging = false
                seekBar?.let {
                    playerVM.seekTo(it.progress)
                }
            }
        })
    }
    private fun formatTime(millis: Int): String {
        val seconds = (millis / 1000) % 60
        val minutes = (millis / (1000 * 60)) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
    private fun showQueue() {
        val queueFragment = QueueBottomSheetFragment()
        queueFragment.show(supportFragmentManager, "QueueBottomSheet")
    }
    companion object {
        private const val EXTRA_SONG_ID = "extra_song_id"
        private const val EXTRA_SONG_TITLE = "extra_song_title"
        private const val EXTRA_SONG_ARTIST = "extra_song_artist"
        private const val EXTRA_SONG_COVER = "extra_song_cover"

        fun createIntent(context: Context, song: SongMetadata): Intent {
            return Intent(context, SongViewActivity::class.java).apply {
                putExtra(EXTRA_SONG_ID, song._id)
                putExtra(EXTRA_SONG_TITLE, song.title)
                putExtra(EXTRA_SONG_ARTIST, song.artist)
                putExtra(EXTRA_SONG_COVER, song.coverUrl)
            }
        }
    }
}
