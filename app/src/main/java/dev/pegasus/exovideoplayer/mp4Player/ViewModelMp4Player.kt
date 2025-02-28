package dev.pegasus.exovideoplayer.mp4Player

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector

/**
 * Created by: Sohaib Ahmed
 * Date: 2/28/2025
 *
 * Links:
 * - LinkedIn: https://linkedin.com/in/epegasus
 * - GitHub: https://github.com/epegasus
 */

@UnstableApi
class ViewModelMp4Player(application: Application) : AndroidViewModel(application) {

    private val _isBuffering = MutableLiveData(false)
    val isBuffering: LiveData<Boolean> = _isBuffering

    private val trackSelector = DefaultTrackSelector(application)
    private var _exoPlayer: ExoPlayer? = null

    val exoPlayer: ExoPlayer
        get() {
            if (_exoPlayer == null) {
                _exoPlayer = ExoPlayer.Builder(getApplication())
                    .setTrackSelector(trackSelector)
                    .build().apply {
                        playWhenReady = true
                        repeatMode = Player.REPEAT_MODE_ONE
                        addListener(object : Player.Listener {
                            override fun onPlaybackStateChanged(state: Int) {
                                _isBuffering.postValue(state == Player.STATE_BUFFERING)
                            }
                        })
                    }
            }
            return _exoPlayer!!
        }

    fun setMedia(uri: Uri) {
        val mediaItem = MediaItem.fromUri(uri)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("MyTag", "onCleared: called")
        _exoPlayer?.release()
        _exoPlayer = null
    }
}