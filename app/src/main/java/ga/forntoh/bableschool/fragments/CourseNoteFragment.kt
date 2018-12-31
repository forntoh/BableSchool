package ga.forntoh.bableschool.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.forntoh.EasyRecyclerView.EasyRecyclerView
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.result
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.kotlinextensions.where
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.StorageUtil
import ga.forntoh.bableschool.adapters.DocumentsAdapter
import ga.forntoh.bableschool.adapters.VideosAdapter
import ga.forntoh.bableschool.model.Course
import ga.forntoh.bableschool.model.Course_Table
import kotlinx.android.synthetic.main.fragment_course_note.*
import java.util.*

class CourseNoteFragment : Fragment() {

    private val videosAdapter by lazy { VideosAdapter(course.videos as ArrayList<*>) }
    private val documentsAdapter by lazy { DocumentsAdapter(course.documents as ArrayList<*>) }
    private lateinit var course: Course

    @SuppressLint("CheckResult")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_course_note, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val startColors = activity!!.resources.getStringArray(R.array.start_colors)
        val endColors = activity!!.resources.getStringArray(R.array.end_colors)

        if (arguments != null) {
            val index = arguments!!.getInt("index")

            course = (select from Course::class where Course_Table.code.eq(arguments!!.getString("course"))).result!!

            subject_title.text = course.title
            subject_abbr.text = course.abbr
            subject_class.text = StorageUtil.getInstance(activity!!.baseContext).loadClass()

            val bg = GradientDrawable(GradientDrawable.Orientation.TR_BL, intArrayOf(Color.parseColor(startColors[index]), Color.parseColor(endColors[index])))
            bg.shape = GradientDrawable.OVAL
            subject_circle.background = bg

            EasyRecyclerView().apply {
                setType(EasyRecyclerView.Type.HORIZONTAL)
                setAdapter(videosAdapter)
                setRecyclerView(rv_videos)
                setItemSpacing(16, null)
                initialize()
            }
            EasyRecyclerView().apply {
                setType(EasyRecyclerView.Type.VERTICAL)
                setAdapter(documentsAdapter)
                setRecyclerView(rv_documents)
                setItemSpacing(16, null)
                initialize()
            }
        }
    }
}
