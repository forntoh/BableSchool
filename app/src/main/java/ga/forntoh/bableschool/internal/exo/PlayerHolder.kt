package ga.forntoh.bableschool.internal.exo

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.utilities.clipAllParents
import ga.forntoh.bableschool.utilities.inPx
import kotlinx.android.synthetic.main.item_video.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerHolder(
        private val context: Context,
        private val playerView: PlayerView,
        private val playerState: PlayerState,
        progressBar: ProgressBar
) {

    private var inFullscreen: Boolean = false
    private val player: ExoPlayer
    private var fullscreenIcon: ImageView? = null
    private var fullScreenDialog: Dialog? = null
    private val parent: ViewGroup
    private val exoProgress: MyTimeBar

    init {
        this.player = ExoPlayerFactory.newSimpleInstance(
                this.context,
                // Renders audio, video, text (subtitles) content
                DefaultRenderersFactory(this.context),
                // Choose best audio, video, text track from available sources,
                // based on bandwidth, device capabilities, language, etc
                DefaultTrackSelector(),
                // Manage buffering and loading data over the network
                DefaultLoadControl()
        )
        this.exoProgress = this.playerView.findViewById(R.id.exo_progress)
        this.parent = this.playerView.parent as ViewGroup
        this.playerView.player = player

        exoProgress.clipAllParents()

        player.addListener(object : Player.EventListener {
            @SuppressLint("ClickableViewAccessibility")
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                playerView.loading_view.setOnTouchListener { _, _ -> false }
                when (playbackState) {
                    Player.STATE_READY -> {
                        if (!inFullscreen)
                            playerView.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                        (context as Activity).window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                        progressBar.max = player.getContentDuration().toInt()
                        GlobalScope.launch(Dispatchers.Main) {
                            while (progressBar.progress < progressBar.max) {
                                progressBar.progress = player.getContentPosition().toInt()
                                delay(750)
                            }
                        }
                        playerView.loading_view.animate().alpha(0f).duration = 500
                    }
                    Player.STATE_ENDED -> (context as Activity).window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    Player.STATE_BUFFERING -> playerView.loading_view.animate().alpha(1f).duration = 500
                }
            }
        })
        playerView.setControllerVisibilityListener { visibility -> progressBar.visibility = if (visibility == View.GONE) View.VISIBLE else View.GONE }

        initFullScreenDialog()
        initFullScreenButton()
    }

    fun start() {
        // Load media.
        player.prepare(buildMediaSource(Uri.parse(playerState.source)))
        // Restore state (after onResume()/onStart())
        player.playWhenReady = playerState.whenReady
        player.seekTo(playerState.window, playerState.position)
    }

    fun stop() {
        // Save state
        playerState.position = player.currentPosition
        playerState.window = player.currentWindowIndex
        playerState.whenReady = player.playWhenReady
        // Stop the player.
        player.stop(true)
    }

    fun release() {
        player.release()
    }

    private fun buildMediaSource(uri: Uri): ExtractorMediaSource {
        return ExtractorMediaSource.Factory(DefaultDataSourceFactory(context, context.packageName)).createMediaSource(uri)
    }

    fun openFullScreenDialog() {
        inFullscreen = true
        (playerView.parent as ViewGroup).removeView(playerView)
        fullScreenDialog!!.addContentView(playerView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        fullscreenIcon!!.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.exo_controls_fullscreen_exit))
        fullScreenDialog!!.show()
        exoProgress.animate().translationY(6.inPx.toFloat())
    }

    fun closeFullScreenDialog() {
        inFullscreen = false
        (playerView.parent as ViewGroup).removeView(playerView)
        parent.addView(playerView, 0, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        fullscreenIcon!!.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.exo_controls_fullscreen_enter))
        fullScreenDialog!!.dismiss()
        exoProgress.animate().translationY(12.inPx.toFloat())
    }

    private fun initFullScreenButton() {
        this.fullscreenIcon = playerView.findViewById(R.id.exo_fullscreen)
        this.fullscreenIcon!!.setOnClickListener {
            if (!inFullscreen)
                (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            else
                (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        }
    }

    private fun initFullScreenDialog() {
        fullScreenDialog = object : Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            override fun onBackPressed() {
                if (inFullscreen) closeFullScreenDialog()
                super.onBackPressed()
            }
        }
    }
}

