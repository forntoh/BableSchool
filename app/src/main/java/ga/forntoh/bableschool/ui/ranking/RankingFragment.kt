package ga.forntoh.bableschool.ui.ranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raizlabs.android.dbflow.kotlinextensions.save
import com.raizlabs.android.dbflow.sql.language.Delete
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.ApiService
import ga.forntoh.bableschool.data.model.groupie.ItemTopSchool
import ga.forntoh.bableschool.data.model.groupie.ItemTopStudent
import ga.forntoh.bableschool.data.model.other.TopSchool
import ga.forntoh.bableschool.utils.InsetDecoration
import ga.forntoh.bableschool.utils.StorageUtil
import kotlinx.android.synthetic.main.fragment_ranking.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class RankingFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_ranking, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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

        launch {
            val topStudents = ApiService().getTopStudents(StorageUtil.getInstance(context?.applicationContext!!).loadMatriculation()).await()
            topStudents.forEach { it.save() }
            topStudentSection.update(topStudents.map { ItemTopStudent(it.name, it.surname, it.image, it.school, it.average) })

            val topSchools = ApiService().topSchools.await()
            Delete.table(TopSchool::class.java)
            topSchools.forEach { it.save() }
            topSchoolSection.update(topSchools.map { ItemTopSchool(it.schoolName, it.image, it.topStudentName, it.average) })
        }
    }
}
