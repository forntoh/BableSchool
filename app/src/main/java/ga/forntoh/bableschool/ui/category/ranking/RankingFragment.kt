package ga.forntoh.bableschool.ui.category.ranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.model.groupie.ItemTopSchool
import ga.forntoh.bableschool.data.model.groupie.ItemTopStudent
import ga.forntoh.bableschool.internal.InsetDecoration
import ga.forntoh.bableschool.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.fragment_ranking.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class RankingFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: RankingViewModelFactory by instance()
    private lateinit var viewModel: RankingViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_ranking, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RankingViewModel::class.java)
        buildUI()
    }

    private fun buildUI() = launch {
        val topStudentsAdapter = GroupAdapter<ViewHolder>()
        val topSchoolAdapter = GroupAdapter<ViewHolder>()

        rv_top_students.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = topStudentsAdapter
            addItemDecoration(InsetDecoration(16))
        }
        rv_school_ranking.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = topSchoolAdapter
            addItemDecoration(InsetDecoration(16))
        }

        val topStudentSection = Section().apply { topStudentsAdapter.add(this) }
        val topSchoolSection = Section().apply { topSchoolAdapter.add(this) }

        topStudentSection.update(viewModel.topStudent.await().map { ItemTopStudent(it.name, it.surname, it.image, it.school, it.average) })
        topSchoolSection.update(viewModel.topSchool.await().map { ItemTopSchool(it.schoolName, it.image, it.topStudentName, it.average) })
    }
}
