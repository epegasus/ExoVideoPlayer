package dev.pegasus.exovideoplayer.m3u8Player

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
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

    /**
     *  There are two types of Videos in m3u8,
     *      1) VOD (video on demand)
     *      2) Live Streaming
     */

    private fun initPlayerM3U8() {
        //val videoUrl = "https://live-hls-web-aje.getaj.net/AJE/index.m3u8"        // LIVE
        val videoUrl = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"        // VOD

        val mediaItem = MediaItem.fromUri(videoUrl)
        exoPlayer.setMediaItem(mediaItem)

        exoPlayer.prepare()
        exoPlayer.playWhenReady = true

        // ⚡ Auto-reconnect if live stream fails
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                exoPlayer.seekToDefaultPosition() // Restart from the latest position
                exoPlayer.prepare()
            }
        })

        binding.playerView.player = exoPlayer
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