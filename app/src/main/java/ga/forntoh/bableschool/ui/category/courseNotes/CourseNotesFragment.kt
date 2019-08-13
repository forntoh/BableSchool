package ga.forntoh.bableschool.ui.category.courseNotes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.*
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.model.groupie.ItemCourse
import ga.forntoh.bableschool.data.model.main.toCourseView
import ga.forntoh.bableschool.internal.InsetDecoration
import ga.forntoh.bableschool.ui.base.ScopedFragment
import ga.forntoh.bableschool.ui.category.CategoryActivity
import ga.forntoh.bableschool.ui.category.courseNotes.detail.CourseNoteFragment
import ga.forntoh.bableschool.utilities.invalidateViewState
import ga.forntoh.bableschool.utilities.toggleViewState
import kotlinx.android.synthetic.main.fragment_course_notes.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CourseNotesFragment : ScopedFragment(), OnItemClickListener, KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: CourseNotesViewModelFactory by instance()
    private lateinit var viewModel: CourseNotesViewModel

    private var section = Section()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_course_notes, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CourseNotesViewModel::class.java)
        init()
        buildUI()
    }

    private fun init() {
        val groupAdapter = GroupAdapter<ViewHolder>().apply { setOnItemClickListener(this@CourseNotesFragment) }

        rv_course_notes.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = groupAdapter
            addItemDecoration(InsetDecoration(16))
        }
        groupAdapter.add(section)
    }

    private fun buildUI() = launch {
        rv_course_notes.invalidateViewState()
        rv_course_notes.toggleViewState(section.apply { update(viewModel.allCourseNotes.await().map { it.toCourseView() }) })
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
