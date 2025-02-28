package dev.pegasus.exovideoplayer

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dev.pegasus.exovideoplayer.databinding.ActivityMainBinding
import dev.pegasus.exovideoplayer.m3u8Player.ActivityM3u8Player
import dev.pegasus.exovideoplayer.mp4Player.ActivityMp4Player

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setPadding()

        binding.mbPlayMp4.setOnClickListener { startActivity(Intent(this, ActivityMp4Player::class.java)) }
        binding.mbPlayM3u8.setOnClickListener { startActivity(Intent(this, ActivityM3u8Player::class.java)) }
    }

    private fun setPadding() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}