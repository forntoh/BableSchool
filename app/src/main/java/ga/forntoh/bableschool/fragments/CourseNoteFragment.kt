package ga.forntoh.bableschool.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.forntoh.EasyRecyclerView.EasyRecyclerView
import com.google.gson.Gson
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.StorageUtil
import ga.forntoh.bableschool.adapters.DocumentsAdapter
import ga.forntoh.bableschool.adapters.VideosAdapter
import ga.forntoh.bableschool.model.Course
import ga.forntoh.bableschool.utils.SquareConstraintLayout
import java.util.*

class CourseNoteFragment : Fragment() {

    private val videosAdapter by lazy { VideosAdapter(course.videos as ArrayList<*>) }
    private val documentsAdapter by lazy { DocumentsAdapter(course.documents as ArrayList<*>) }
    private lateinit var course: Course

    @SuppressLint("CheckResult")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_course_note, container, false)

        val subjectCircleView = v.findViewById<SquareConstraintLayout>(R.id.subject_circle)
        val subjectAbbrView = v.findViewById<TextView>(R.id.subject_abbr)
        val subjectTitleView = v.findViewById<TextView>(R.id.subject_title)
        val subjectClassView = v.findViewById<TextView>(R.id.subject_class)

        val startColors = activity!!.resources.getStringArray(R.array.start_colors)
        val endColors = activity!!.resources.getStringArray(R.array.end_colors)

        if (arguments != null) {
            val index = arguments!!.getInt("index")
            var (code, title) = Gson().fromJson(arguments!!.getString("course"), Course::class.java)

            subjectTitleView.text = course.title
            subjectAbbrView.text = course.abbr
            subjectClassView.text = StorageUtil.getInstance(activity!!.baseContext).loadClass()

            val bg = GradientDrawable(GradientDrawable.Orientation.TR_BL, intArrayOf(Color.parseColor(startColors[index]), Color.parseColor(endColors[index])))
            bg.shape = GradientDrawable.OVAL
            subjectCircleView.background = bg

            EasyRecyclerView().apply {
                setType(EasyRecyclerView.Type.HORIZONTAL)
                setAdapter(videosAdapter)
                setRecyclerView(v.findViewById(R.id.rv_videos))
                setItemSpacing(16, null)
                initialize()
            }
            EasyRecyclerView().apply {
                setType(EasyRecyclerView.Type.VERTICAL)
                setAdapter(documentsAdapter)
                setRecyclerView(v.findViewById(R.id.rv_documents))
                setItemSpacing(16, null)
                initialize()
            }
        }
        return v
    }
}
