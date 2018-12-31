package ga.forntoh.bableschool.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.forntoh.EasyRecyclerView.EasyRecyclerView
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.list
import com.raizlabs.android.dbflow.kotlinextensions.save
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.sql.language.Delete
import ga.forntoh.bableschool.ApiService
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.RetrofitBuilder
import ga.forntoh.bableschool.StorageUtil
import ga.forntoh.bableschool.adapters.TopSchoolsAdapter
import ga.forntoh.bableschool.adapters.TopStudentsAdapter
import ga.forntoh.bableschool.model.TopSchool
import ga.forntoh.bableschool.model.TopStudent
import ga.forntoh.bableschool.utils.Utils.dealWithData
import ga.forntoh.bableschool.utils.Utils.isConnected
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_ranking.*
import java.util.*

class RankingFragment : Fragment() {

    private lateinit var v: View
    private var t1: Thread? = null
    private val topStudentsAdapter by lazy { TopStudentsAdapter(topStudents) }
    private val topSchoolsAdapter by lazy { TopSchoolsAdapter(topSchools) }
    private val topStudents = ArrayList<TopStudent>()
    private val topSchools = ArrayList<TopSchool>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_ranking, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        t1 = EasyRecyclerView()
                .setType(EasyRecyclerView.Type.HORIZONTAL)
                .setAdapter(topStudentsAdapter)
                .setRecyclerView(rv_top_students)
                .setItemSpacing(16, null)
                .initialize(topStudents.size, 5000, false)

        EasyRecyclerView()
                .setType(EasyRecyclerView.Type.VERTICAL)
                .setAdapter(topSchoolsAdapter)
                .setRecyclerView(rv_school_ranking)
                .setItemSpacing(16, null)
                .initialize()
        fetchItems()
    }

    private fun fetchItems() {
        val service = RetrofitBuilder.createService(ApiService::class.java)

        if (isConnected(activity!!))
            service.topSchools
                    .flatMap { list ->
                        Delete.table(TopSchool::class.java)
                        list.forEach { it.save() }
                        dealWithData(activity!!, list, topSchools, topSchoolsAdapter)
                        return@flatMap service.getTopStudents(StorageUtil.getInstance(activity!!.baseContext).loadMatriculation())
                    }
                    .subscribeOn(Schedulers.io())
                    .subscribe({ list ->
                        Delete.table(TopStudent::class.java)
                        list.forEach { it.save() }
                        dealWithData(activity!!, list, topStudents, topStudentsAdapter)
                    }) {
                        dealWithData(activity!!, (select from TopStudent::class).list, topStudents, topStudentsAdapter)
                        dealWithData(activity!!, (select from TopSchool::class).list, topSchools, topSchoolsAdapter)
                    }
        else {
            dealWithData(activity!!, (select from TopStudent::class).list, topStudents, topStudentsAdapter)
            dealWithData(activity!!, (select from TopSchool::class).list, topSchools, topSchoolsAdapter)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (t1 != null) t1!!.interrupt()
    }

}
