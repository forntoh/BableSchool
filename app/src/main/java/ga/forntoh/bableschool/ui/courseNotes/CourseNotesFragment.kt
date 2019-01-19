package ga.forntoh.bableschool.ui.courseNotes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raizlabs.android.dbflow.sql.language.Delete
import com.xwray.groupie.*
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.ApiService
import ga.forntoh.bableschool.data.model.groupie.ItemCourse
import ga.forntoh.bableschool.data.model.main.Course
import ga.forntoh.bableschool.ui.CategoryActivity
import ga.forntoh.bableschool.ui.courseNotes.detail.CourseNoteFragment
import ga.forntoh.bableschool.utils.InsetDecoration
import ga.forntoh.bableschool.utils.StorageUtil
import kotlinx.android.synthetic.main.fragment_course_notes.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CourseNotesFragment : Fragment(), CoroutineScope, OnItemClickListener {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var section = Section()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_course_notes, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val groupAdapter = GroupAdapter<ViewHolder>().apply { setOnItemClickListener(this@CourseNotesFragment) }

        rv_course_notes.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = groupAdapter
            addItemDecoration(InsetDecoration(16))
        }

        groupAdapter.add(section)

        launch {
            val courseNotes = ApiService().getCourseNotes(StorageUtil.getInstance(context?.applicationContext!!).loadMatriculation()).await()
            Delete.table(Course::class.java)
            courseNotes.forEach { it.save() }
            section.update(courseNotes.map { ItemCourse(it.code, it.title, it.videos, it.documents, it.abbr) })
        }

    }

    override fun onItemClick(item: Item<*>, view: View) {
        if (item is ItemCourse) {
            val fragment = CourseNoteFragment().apply {
                arguments = Bundle().apply {
                    putString("course", item.code)
                    putInt("index", section.getPosition(item))
                }
            }
            (context as CategoryActivity).loadFragment(fragment).addToBackStack(null)
        }
    }
}
