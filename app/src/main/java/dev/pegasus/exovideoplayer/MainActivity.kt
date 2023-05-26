package dev.pegasus.exovideoplayer

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import dev.pegasus.exovideoplayer.databinding.ActivityMainBinding

@UnstableApi
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val exoPlayer by lazy { ExoPlayer.Builder(this).build() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initPlayerNormal()
        //initPlayerM3U8()
    }

    private fun initPlayerNormal() {
        binding.playerViewMain.player = exoPlayer

        val videoUri = Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
        val mediaItem = MediaItem.fromUri(videoUri)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    private fun initPlayerM3U8() {
        binding.playerViewMain.player = exoPlayer

        val videoUri = Uri.parse("https://ythls.onrender.com/channel/UCz2yxQJZgiB_5elTzqV7FiQ.m3u8")
        val mediaItem = MediaItem.fromUri(videoUri)
        //val mediaSource = HlsMediaSource.Factory(DefaultDataSourceFactory(this, Util.getUserAgent(this, "app-name"))).createMediaSource(mediaItem)
        val mediaSource = ProgressiveMediaSource.Factory(DefaultDataSourceFactory(this, Util.getUserAgent(this, "app-name"))).createMediaSource(mediaItem)

        exoPlayer.setMediaSource(mediaSource)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    override fun onPause() {
        super.onPause()
        exoPlayer.pause()
    }

    override fun onDestroy() {
        exoPlayer.release()
        super.onDestroy()
    }
}