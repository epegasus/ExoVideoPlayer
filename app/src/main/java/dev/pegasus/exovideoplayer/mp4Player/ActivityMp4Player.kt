package dev.pegasus.exovideoplayer.mp4Player

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import dev.pegasus.exovideoplayer.R
import dev.pegasus.exovideoplayer.databinding.ActivityMp4PlayerBinding
import dev.pegasus.exovideoplayer.databinding.PlayerViewStyleBinding

@UnstableApi
class ActivityMp4Player : AppCompatActivity() {

    private val binding by lazy { ActivityMp4PlayerBinding.inflate(layoutInflater) }
    private val bindingStyle by lazy { PlayerViewStyleBinding.bind(binding.playerView.findViewById(R.id.clContainerPlayerViewStyle)) }

    private val viewModel by viewModels<ViewModelMp4Player>()

    private val resizeModes = listOf(
        AspectRatioFrameLayout.RESIZE_MODE_FIT,
        AspectRatioFrameLayout.RESIZE_MODE_FILL,
        AspectRatioFrameLayout.RESIZE_MODE_ZOOM
    )
    private val resizeIcons = listOf(
        R.drawable.ic_full_screen,
        R.drawable.ic_fit_screen,
        R.drawable.ic_crop_screen
    )

    private var currentResizeModeIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setPadding()

        updateUI()
        setupPlayer()
        observeViewModel()

        bindingStyle.mbBackPlayerViewStyle.setOnClickListener { finish() }
        bindingStyle.mbRotateScreen.setOnClickListener { onRotateClick() }
        bindingStyle.mbBackward.setOnClickListener { viewModel.exoPlayer.seekBack() }
        bindingStyle.mbForward.setOnClickListener { viewModel.exoPlayer.seekForward() }
        bindingStyle.mbFullScreen.setOnClickListener { onFullScreenClick() }
    }

    private fun setPadding() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun updateUI() {
        bindingStyle.mtvTitlePlayerViewStyle.text = getString(R.string.big_buck_bunny_mp4)
    }

    private fun setupPlayer() {
        binding.playerView.player = viewModel.exoPlayer
        val videoUri = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4".toUri()
        viewModel.setMedia(videoUri)
    }

    private fun observeViewModel() {
        viewModel.isBuffering.observe(this) { isBuffering ->
            binding.progressBar.visibility = if (isBuffering) View.VISIBLE else View.GONE
        }
    }

    private fun onRotateClick() {
        val currentOrientation = resources.configuration.orientation
        requestedOrientation = if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }

    private fun onFullScreenClick() {
        currentResizeModeIndex = (currentResizeModeIndex + 1) % resizeModes.size
        binding.playerView.resizeMode = resizeModes[currentResizeModeIndex]
        bindingStyle.mbFullScreen.setIconResource(resizeIcons[currentResizeModeIndex])
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.playerView.player = null
    }
}