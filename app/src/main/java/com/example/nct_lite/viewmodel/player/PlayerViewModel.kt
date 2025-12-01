
package com.example.nct_lite.viewmodel.player

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nct_lite.data.player.MusicPlayerService
import com.example.nct_lite.data.player.MusicPlayerService.PlayerState
import com.example.nct_lite.data.song.response.SongMetadata
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlayerViewModel(application: Application) : AndroidViewModel(application) {
    private val app = getApplication<Application>()
    private var musicService: MusicPlayerService? = null
    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> get() = _playerState
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val svc = (binder as MusicPlayerService.MusicBinder).getService()
            musicService = svc
            viewModelScope.launch {
                svc.state.collect {
                    _playerState.value = it
                }
            }
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            musicService = null
        }
    }

    init {
        val intent = Intent(app, MusicPlayerService::class.java)
        app.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }
    fun playSong(song: SongMetadata) {
        val intent = Intent(app, MusicPlayerService::class.java).apply {
            putExtra("url", song.url)
            putExtra("title", song.title)
            putExtra("artist", song.artist)
            putExtra("cover", song.coverUrl)
        }

        ContextCompat.startForegroundService(app, intent)
    }


    fun pauseOrResume() {
        musicService?.pauseOrResume()
    }

    override fun onCleared() {
        super.onCleared()
        app.unbindService(serviceConnection)
    }
}