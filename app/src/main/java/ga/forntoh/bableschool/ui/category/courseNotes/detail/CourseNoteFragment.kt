package ga.forntoh.bableschool.ui.category.courseNotes.detail

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
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
import ga.forntoh.bableschool.data.model.main.toDocumentView
import ga.forntoh.bableschool.data.model.main.toVideo
import ga.forntoh.bableschool.data.model.main.toVideoView
import ga.forntoh.bableschool.internal.InsetDecoration
import ga.forntoh.bableschool.ui.base.ScopedFragment
import ga.forntoh.bableschool.ui.category.courseNotes.CourseNotesViewModel
import ga.forntoh.bableschool.ui.category.courseNotes.CourseNotesViewModelFactory
import ga.forntoh.bableschool.ui.category.profile.ProfileViewModel
import ga.forntoh.bableschool.ui.category.profile.ProfileViewModelFactory
import ga.forntoh.bableschool.utilities.invalidateViewState
import ga.forntoh.bableschool.utilities.toggleViewState
import kotlinx.android.synthetic.main.fragment_course_note.*
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
        viewModel = ViewModelProvider(this, viewModelFactory).get(CourseNotesViewModel::class.java)
        profileViewModel = ViewModelProvider(this, profileViewModelFactory).get(ProfileViewModel::class.java)
        buildUI()
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

        rv_videos.invalidateViewState()
        rv_documents.invalidateViewState()
        rv_videos.toggleViewState(videosSection.apply { viewModel.videos.await()?.map { it.toVideoView() }?.let { update(it) } })
        rv_documents.toggleViewState(documentsSection.apply { viewModel.documents.await()?.map { it.toDocumentView() }?.let { update(it) } })
    }

    @SuppressLint("InflateParams")
    private val onDocumentClickListener = OnItemClickListener { item, _ ->
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
        } else if (item is ItemDocument) {
            Toast.makeText(context!!, "Loading document...", Toast.LENGTH_SHORT).show()
            FileLoader.with(context)
                    .load(item.url, false)
                    .fromDirectory(context?.getString(R.string.app_name), FileLoader.DIR_EXTERNAL_PRIVATE)
                    .asFile(object : FileRequestListener<File> {
                        override fun onLoad(request: FileLoadRequest, response: FileResponse<File>) {
                            val loadedFile = response.body

                            if (item.extension != "pdf") {
                                activity?.startActivity(Intent().apply {
                                    action = Intent.ACTION_VIEW
                                    setDataAndType(Uri.fromFile(loadedFile), MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(item.url)))
                                })
                            } else {
                                val intent: Intent
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    val uri = FileProvider.getUriForFile(context!!, activity!!.packageName + ".provider", loadedFile)
                                    intent = Intent.createChooser(Intent(Intent.ACTION_VIEW).apply {
                                        flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                                        flags = Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                                        setDataAndType(uri, "application/pdf")
                                    }, "Open document with...").apply {
                                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    }
                                } else {
                                    intent = Intent.createChooser(Intent(Intent.ACTION_VIEW).apply {
                                        setDataAndType(Uri.fromFile(loadedFile), "application/pdf")
                                    }, "Open document with...").apply {
                                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    }
                                }
                                startActivity(intent)

                            }

                        }

                        override fun onError(request: FileLoadRequest, t: Throwable) = Unit
                    })
        }
    }

    private val onVideoClickListener = OnItemClickListener { item, _ ->
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
        } else if (item is ItemVideo) {
            Toast.makeText(context!!, "Loading video...", Toast.LENGTH_SHORT).show()
            FileLoader.with(context)
                    .load(item.url, false)
                    .fromDirectory(context?.getString(R.string.app_name), FileLoader.DIR_EXTERNAL_PRIVATE)
                    .asFile(object : FileRequestListener<File> {
                        override fun onLoad(request: FileLoadRequest, response: FileResponse<File>) {
                            val loadedFile = response.body
                            startActivity(Intent(context, VideoPlayerActivity::class.java).apply { putExtra("video", Gson().toJson(item.toVideo().apply { url = loadedFile.path })) })
                        }

                        override fun onError(request: FileLoadRequest, t: Throwable) = Unit
                    })
        }
    }

}
