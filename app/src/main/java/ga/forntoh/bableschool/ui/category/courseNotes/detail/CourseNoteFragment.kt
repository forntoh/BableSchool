package ga.forntoh.bableschool.ui.category.courseNotes.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.krishna.fileloader.FileLoader
import com.krishna.fileloader.listener.FileRequestListener
import com.krishna.fileloader.pojo.FileResponse
import com.krishna.fileloader.request.FileLoadRequest
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.model.groupie.ItemDocument
import ga.forntoh.bableschool.data.model.groupie.ItemVideo
import ga.forntoh.bableschool.data.model.main.Course
import ga.forntoh.bableschool.internal.InsetDecoration
import ga.forntoh.bableschool.internal.exo.PlayerHolder
import ga.forntoh.bableschool.internal.exo.PlayerState
import ga.forntoh.bableschool.ui.base.ScopedFragment
import ga.forntoh.bableschool.ui.category.CategoryActivity
import ga.forntoh.bableschool.ui.category.courseNotes.CourseNotesViewModel
import ga.forntoh.bableschool.ui.category.courseNotes.CourseNotesViewModelFactory
import ga.forntoh.bableschool.ui.category.profile.ProfileViewModel
import ga.forntoh.bableschool.ui.category.profile.ProfileViewModelFactory
import ga.forntoh.bableschool.utilities.Utils
import ga.forntoh.bableschool.utilities.inPx
import kotlinx.android.synthetic.main.dialog_pdf_viewer.view.*
import kotlinx.android.synthetic.main.exo_controller_ui.view.*
import kotlinx.android.synthetic.main.fragment_course_note.*
import kotlinx.android.synthetic.main.item_video.view.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.io.File

class CourseNoteFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: CourseNotesViewModelFactory by instance()
    private lateinit var viewModel: CourseNotesViewModel
    private val profileViewModelFactory: ProfileViewModelFactory by instance()
    private lateinit var profileViewModel: ProfileViewModel

    private lateinit var course: Course

    private val videosSection = Section()
    private val documentsSection = Section()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_course_note, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CourseNotesViewModel::class.java)
        profileViewModel = ViewModelProviders.of(this, profileViewModelFactory).get(ProfileViewModel::class.java)

        buildUI()

        val savedState = Gson().fromJson(savedInstanceState?.getString("state"), PlayerState::class.java)
                ?: return
        state.whenReady = savedState.whenReady
        state.window = savedState.window
        state.position = savedState.position
        state.source = savedState.source
    }

    private fun buildUI() = launch {
        val startColors = activity!!.resources.getStringArray(R.array.start_colors)
        val endColors = activity!!.resources.getStringArray(R.array.end_colors)

        val index = (arguments?.getInt("index") ?: return@launch) % startColors.size
        viewModel.code = arguments?.getString("course") ?: return@launch

        course = viewModel.singleCourseNote.await() ?: return@launch

        subject_title?.text = course.title
        subject_abbr?.text = course.abbr
        subject_class?.text = profileViewModel.user.await()?.profileData?.clazz ?: return@launch

        val bg = GradientDrawable(GradientDrawable.Orientation.TR_BL, intArrayOf(Color.parseColor(startColors[index]), Color.parseColor(endColors[index])))
        bg.shape = GradientDrawable.OVAL
        subject_circle?.background = bg

        val videosAdapter = GroupAdapter<ViewHolder>().apply {
            add(videosSection)
            setOnItemClickListener(onVideoClickListener)
        }
        val documentsAdapter = GroupAdapter<ViewHolder>().apply {
            add(documentsSection)
            setOnItemClickListener(onDocumentClickListener)
        }

        rv_videos?.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = videosAdapter
            addItemDecoration(InsetDecoration(16))
        }
        rv_documents?.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = documentsAdapter
            addItemDecoration(InsetDecoration(16))
        }

        course.videos?.map { ItemVideo(it.courseCode, it.title, it.author, it.duration, it.url, it.thumbnail) }?.let {
            videosSection.update(it)
        }
        course.documents?.map { ItemDocument(it.courseCode, it.title, it.author, it.size, it.url, it.type, it.extension) }?.let {
            documentsSection.update(it)
        }
    }

    @SuppressLint("InflateParams")
    private val onDocumentClickListener = OnItemClickListener { item, _ ->
        if (item is ItemDocument) {
            val layout = LayoutInflater.from(context).inflate(R.layout.dialog_pdf_viewer, null)
            Utils.startPopUpWindow(layout, view!!, null)

            FileLoader.with(context)
                    .load(item.url, false)
                    .fromDirectory(context?.getString(R.string.app_name), FileLoader.DIR_INTERNAL)
                    .asFile(object : FileRequestListener<File> {
                        override fun onLoad(request: FileLoadRequest, response: FileResponse<File>) {
                            val loadedFile = response.body

                            if (item.extension != "pdf") {
                                activity?.startActivity(Intent().apply {
                                    action = Intent.ACTION_VIEW
                                    setDataAndType(Uri.fromFile(loadedFile), MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(item.url)))
                                })
                            } else
                                layout.pdfView.fromFile(loadedFile)
                                        .defaultPage(0)
                                        .enableSwipe(true)
                                        .enableDoubletap(true)
                                        .enableAntialiasing(true)
                                        .spacing(4)
                                        .onLoad { layout.progressBar.visibility = View.GONE }
                                        .load()
                        }

                        override fun onError(request: FileLoadRequest, t: Throwable) {
                            layout.progressBar.visibility = View.GONE
                            t.printStackTrace()
                            Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                            (context as CategoryActivity).onBackPressed()
                        }
                    })
        }
    }

    private var playerHolder: PlayerHolder? = null
    private val state = PlayerState()

    private val onVideoClickListener = OnItemClickListener { item, view ->
        if (item is ItemVideo) {
            //state.source = item.url ?: return@OnItemClickListener
            view.exo_close.setOnClickListener { doStuffWithView(view, true); it.setOnClickListener(null) }
            doStuffWithView(view, false)
        }
    }

    private fun doStuffWithView(view: View, toClose: Boolean) {
        view.card_parent.apply {
            radius = if (!toClose) 0.inPx.toFloat() else 5.inPx.toFloat()
            layoutParams.width = if (!toClose) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
        }
        view.exo_player_view.apply {
            visibility = if (!toClose) View.VISIBLE else View.GONE
            layoutParams.height = if (!toClose) 200.inPx else 0.inPx
        }
        view.root.layoutParams.apply {
            width = if (!toClose) ViewGroup.LayoutParams.MATCH_PARENT else 150.inPx
            height = if (!toClose) ViewGroup.LayoutParams.MATCH_PARENT else 80.inPx
        }
        if (!toClose) {
            playerHolder = PlayerHolder(context!!, view.exo_player_view, state, view.progressBarExo)
        } else playerHolder?.stop()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        when (newConfig.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> playerHolder?.openFullScreenDialog()
            Configuration.ORIENTATION_PORTRAIT -> playerHolder?.closeFullScreenDialog()
        }
    }

    override fun onStart() {
        super.onStart()
        playerHolder?.start()
    }

    override fun onStop() {
        super.onStop()
        playerHolder?.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerHolder?.release()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("state", Gson().toJson(state))
    }
}
