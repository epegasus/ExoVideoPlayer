package dev.pegasus.exovideoplayer.m3u8Player

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import dev.pegasus.exovideoplayer.R
import dev.pegasus.exovideoplayer.databinding.ActivityM3u8PlayerBinding

class ActivityM3u8Player : AppCompatActivity() {

    private val binding by lazy { ActivityM3u8PlayerBinding.inflate(layoutInflater) }
    private val exoPlayer by lazy { ExoPlayer.Builder(this).build() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setPadding()

        initPlayerM3U8()

        binding.mbBack.setOnClickListener { finish() }
    }

    private fun setPadding() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    @OptIn(UnstableApi::class)
    private fun initPlayerM3U8() {
        binding.playerView.player = exoPlayer

        //val videoUri = "https://ythls.onrender.com/channel/UCz2yxQJZgiB_5elTzqV7FiQ.m3u8".toUri()
        val videoUri = "https://live-hls-abr-cdn.livepush.io/live/bigbuckbunnyclip/index.m3u8".toUri()

        val mediaItem = MediaItem.fromUri(videoUri)

        val mediaSource = HlsMediaSource.Factory(DefaultDataSourceFactory(this, Util.getUserAgent(this, "app-name"))).createMediaSource(mediaItem)
        //val mediaSource = ProgressiveMediaSource.Factory(DefaultDataSourceFactory(this, Util.getUserAgent(this, "app-name"))).createMediaSource(mediaItem)

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