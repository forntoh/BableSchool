package ga.forntoh.bableschool.ui.courseNotes.detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.krishna.fileloader.FileLoader
import com.krishna.fileloader.listener.FileRequestListener
import com.krishna.fileloader.pojo.FileResponse
import com.krishna.fileloader.request.FileLoadRequest
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.result
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.kotlinextensions.where
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.model.groupie.ItemDocument
import ga.forntoh.bableschool.data.model.groupie.ItemVideo
import ga.forntoh.bableschool.data.model.main.Course
import ga.forntoh.bableschool.data.model.main.Course_Table
import ga.forntoh.bableschool.ui.CategoryActivity
import ga.forntoh.bableschool.utils.InsetDecoration
import ga.forntoh.bableschool.utils.StorageUtil
import ga.forntoh.bableschool.utils.Utils
import kotlinx.android.synthetic.main.dialog_pdf_viewer.view.*
import kotlinx.android.synthetic.main.fragment_course_note.*
import java.io.File

class CourseNoteFragment : androidx.fragment.app.Fragment() {

    private lateinit var course: Course

    private val videosSection = Section()
    private val documentsSection = Section()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_course_note, container, false)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val startColors = activity!!.resources.getStringArray(R.array.start_colors)
        val endColors = activity!!.resources.getStringArray(R.array.end_colors)

        if (arguments != null) {
            val index = arguments!!.getInt("index") % startColors.size

            course = (select from Course::class where Course_Table.code.eq(arguments!!.getString("course"))).result!!

            subject_title.text = course.title
            subject_abbr.text = course.abbr
            subject_class.text = StorageUtil.getInstance(activity!!.baseContext).loadClass()

            val bg = GradientDrawable(GradientDrawable.Orientation.TR_BL, intArrayOf(Color.parseColor(startColors[index]), Color.parseColor(endColors[index])))
            bg.shape = GradientDrawable.OVAL
            subject_circle.background = bg

            val videosAdapter = GroupAdapter<ViewHolder>().apply {
                add(videosSection)
                setOnItemClickListener(onVideoClickListener)
            }
            val documentsAdapter = GroupAdapter<ViewHolder>().apply {
                add(documentsSection)
                setOnItemClickListener(onDocumentClickListener)
            }

            rv_videos.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                adapter = videosAdapter
                addItemDecoration(InsetDecoration(16))
            }
            rv_documents.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                adapter = documentsAdapter
                addItemDecoration(InsetDecoration(16))
            }

            course.videos?.map { ItemVideo(it.courseCode, it.title, it.author, it.duration, it.url, it.thumbnail) }?.let {
                videosSection.update(it)
            }
            course.documents?.map { ItemDocument(it.courseCode, it.title, it.author, it.size, it.url, it.type) }?.let {
                documentsSection.update(it)
            }
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

    private val onVideoClickListener = OnItemClickListener { item, _ ->

    }
}
