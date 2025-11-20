package com.example.nct_lite.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log

class MusicPlayerService : Service() {

    private val binder = MusicBinder()
    private var mediaPlayer: MediaPlayer? = null


    var currentTitle: String = ""
    var currentArtist: String = ""
    var currentCoverUrl: String? = null

    inner class MusicBinder : Binder() {
        fun getService(): MusicPlayerService = this@MusicPlayerService
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("MusicPlayerService", "Service created")
    }

    fun playMusic(url: String, title: String, artist: String, coverUrl: String?) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(url)
            prepare()
            start()
        }

        currentTitle = title
        currentArtist = artist
        currentCoverUrl = coverUrl
    }

    fun pauseMusic() {
        mediaPlayer?.let {
            if (it.isPlaying) it.pause()
        }
    }

    fun resumeMusic() {
        mediaPlayer?.let {
            if (!it.isPlaying) it.start()
        }
    }

    fun stopMusic() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopMusic()
        Log.d("MusicPlayerService", "Service destroyed")
    }
}
