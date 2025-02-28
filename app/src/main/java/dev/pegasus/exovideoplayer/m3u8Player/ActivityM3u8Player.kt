package dev.pegasus.exovideoplayer.m3u8Player

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
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
    /*private fun initPlayerM3U8() {
        binding.playerView.player = exoPlayer

        val videoUri = "https://vod3.cf.dmcdn.net/sec2(xN82Wovbx_0A1BkRYKI_sL3_F00V5oY2gs96oikU2mA_80X1N1mFw9NL0tN5xC3L4SLit_e1hyx2dRrWMGQ4q_TisPyRO6Fd9QT6_UF42sYqa3bnf85t-d8apCPCwVNTiJ0R8lZSfAiVoYq6m7h9jLq9KyE5iEg5s0ElSksYpxY)/video/614/142/476241416_mp4_h264_aac.m3u8".toUri()
        //val videoUri = "https://live-hls-abr-cdn.livepush.io/live/bigbuckbunnyclip/index.m3u8".toUri()

        val mediaItem = MediaItem.fromUri(videoUri)
        val mediaSource = HlsMediaSource
            .Factory(DefaultDataSourceFactory(this, Util.getUserAgent(this, "app-name")))
            .createMediaSource(mediaItem)

        exoPlayer.setMediaSource(mediaSource)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }*/

    private fun initPlayerM3U8() {
        binding.playerView.player = exoPlayer

        // Replace this with your actual URL (Live or VOD)
        //val videoUrl = "https://vod3.cf.dmcdn.net/sec2(xN82Wovbx_0A1BkRYKI_sL3_F00V5oY2gs96oikU2mA_80X1N1mFw9NL0tN5xC3L4SLit_e1hyx2dRrWMGQ4q_TisPyRO6Fd9QT6_UF42sYqa3bnf85t-d8apCPCwVNTiJ0R8lZSfAiVoYq6m7h9jLq9KyE5iEg5s0ElSksYpxY)/video/614/142/476241416_mp4_h264_aac.m3u8"
        val videoUrl = "https://live-hls-web-aje.getaj.net/AJE/index.m3u8"

        val mediaItem = MediaItem.Builder()
            .setUri(videoUrl)
            .setLiveConfiguration(MediaItem.LiveConfiguration.Builder().setMaxPlaybackSpeed(1.02f).build()) // Helps with live streams
            .build()

        val mediaSource = HlsMediaSource
            .Factory(DefaultDataSourceFactory(this, Util.getUserAgent(this, "app-name")))
            .createMediaSource(mediaItem)

        exoPlayer.setMediaSource(mediaSource)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true

        // ⚡ Auto-reconnect if live stream fails
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                exoPlayer.seekToDefaultPosition() // Restart from the latest position
                exoPlayer.prepare()
            }
        })
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