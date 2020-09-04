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
import ga.forntoh.bableschool.ui.category.CategoryActivity
import ga.forntoh.bableschool.utilities.invalidateViewState
import ga.forntoh.bableschool.utilities.toggleViewState
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.fragment_ranking.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class RankingFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: RankingViewModelFactory by instance()
    private lateinit var viewModel: RankingViewModel

    private val topStudentSection = Section()
    private val topSchoolSection = Section()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_ranking, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RankingViewModel::class.java)
        buildUI()
    }

    private fun buildUI() {
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

        topStudentsAdapter.add(topStudentSection)
        topSchoolAdapter.add(topSchoolSection)

        loadData()
        (activity as CategoryActivity).srl.setOnRefreshListener {
            viewModel.resetState()
            loadData()
        }
    }

    private fun loadData() = launch {
        rv_top_students.invalidateViewState()
        rv_school_ranking.invalidateViewState()

        viewModel.topStudent.await().observe(viewLifecycleOwner, { list ->
            rv_top_students.toggleViewState(topStudentSection.apply { update(list.map { ItemTopStudent(it.name, it.surname, it.image, it.school, it.average) }) })
            (activity as CategoryActivity).srl.isRefreshing = false
        })
        viewModel.topSchool.await().observe(viewLifecycleOwner, { list ->
            rv_school_ranking.toggleViewState(topSchoolSection.apply { update(list.map { ItemTopSchool(it.schoolName, it.image, it.topStudentName, it.average) }) })
            (activity as CategoryActivity).srl.isRefreshing = false
        })
    }
}
