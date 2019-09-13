package ga.forntoh.bableschool.ui.category.courseNotes.detail

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import com.google.gson.Gson
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.model.main.Video
import ga.forntoh.bableschool.ui.base.BaseActivity
import ga.forntoh.bableschool.utilities.exoplayer.PlayerHolder
import ga.forntoh.bableschool.utilities.exoplayer.PlayerState
import kotlinx.android.synthetic.main.activity_video_player.*

class VideoPlayerActivity : BaseActivity() {

    private var playerHolder: PlayerHolder? = null
    private val state = PlayerState()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        val video = Gson().fromJson(intent.getStringExtra("video"), Video::class.java)

        if (video != null) {
            tv_video_title.text = video.title
            tv_video_time.text = "${video.duration} - ${video.author}"
            state.source = video.url
        }
        playerHolder = PlayerHolder(this, exo_player_view, state, progressBar)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        when (newConfig.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> playerHolder!!.openFullScreenDialog()
            Configuration.ORIENTATION_PORTRAIT -> playerHolder!!.closeFullScreenDialog()
        }
    }

    override fun onStart() {
        super.onStart()
        playerHolder!!.start()
    }

    override fun onStop() {
        super.onStop()
        playerHolder!!.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerHolder!!.release()
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("state", Gson().toJson(state))
    }

    public override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val savedState = Gson().fromJson(savedInstanceState.getString("state"), PlayerState::class.java)
                ?: return
        state.whenReady = savedState.whenReady
        state.window = savedState.window
        state.position = savedState.position
        state.source = savedState.source
    }
}
