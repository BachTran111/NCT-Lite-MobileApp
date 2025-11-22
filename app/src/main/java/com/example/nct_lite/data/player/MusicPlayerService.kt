package com.example.nct_lite.data.player

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.nct_lite.R
import com.example.nct_lite.ui.activity.MainActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.log

class MusicPlayerService : Service() {

    data class PlayerState(
        val isPlaying: Boolean = false,
        val title: String = "",
        val artist: String = "",
        val coverUrl: String = "",
        val url: String = ""
    )

    private val channelId = "player_channel"
    private val notificationId = 101

    private val _state = MutableStateFlow(PlayerState())
    val state: StateFlow<PlayerState> get() = _state

    // private val mediaPlayer: MediaPlayer by lazy {
    //     MediaPlayer().apply {
    //         setAudioAttributes(
    //             AudioAttributes.Builder()
    //                 .setUsage(AudioAttributes.USAGE_MEDIA)
    //                 .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
    //                 .build()
    //         )
    //         setOnCompletionListener {
    //             updateState(_state.value.copy(isPlaying = false))
    //         }
    //     }
    // }
    private val mediaPlayer: MediaPlayer by lazy {
    MediaPlayer().apply {
        setOnCompletionListener {
            _state.value = _state.value.copy(isPlaying = false)
        }
    }
}

    private val binder = MusicBinder()

    inner class MusicBinder : Binder() {
        fun getService(): MusicPlayerService = this@MusicPlayerService
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(notificationId, buildNotification(_state.value))
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val url = intent?.getStringExtra("url")
        val title = intent?.getStringExtra("title")
        val artist = intent?.getStringExtra("artist")
        val cover = intent?.getStringExtra("cover")

        if (!url.isNullOrEmpty()) {
            play(url, title ?: "", artist ?: "", cover ?: "")
        }

        return START_STICKY
    }

fun play(url: String, title: String, artist: String, cover: String) {
    try {
        mediaPlayer.reset()                 // quay về Idle
        mediaPlayer.setDataSource(url)      // -> Initialized
        mediaPlayer.prepareAsync()          // -> Async preparing

        mediaPlayer.setOnPreparedListener {
            it.start()                      // ✅ CHỈ start TRONG onPrepared
            _state.value = PlayerState(
                isPlaying = true,
                title = title,
                artist = artist,
                coverUrl = cover,
                url = url
            )
        }
        print("")
        Log.e("PLAYER", "play() called with url = $url")

    } catch (e: Exception) {
        e.printStackTrace()
            Log.e("PLAYER", "Exception in play()", e)

    }
}

    fun pauseOrResume() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            updateState(_state.value.copy(isPlaying = false))
        } else {
            mediaPlayer.start()
            updateState(_state.value.copy(isPlaying = true))
        }
    }

    private fun updateState(state: PlayerState) {
        _state.value = state
        updateNotification(state)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(state: PlayerState): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle(state.title.ifEmpty { getString(R.string.app_name) })
            .setContentText(state.artist)
            .setSmallIcon(R.drawable.ic_library_music)
            .setContentIntent(pendingIntent)
            .setOngoing(state.isPlaying)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun updateNotification(state: PlayerState) {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = buildNotification(state)
        manager.notify(notificationId, notification)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && state.isPlaying) {
            startForeground(notificationId, notification)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}
