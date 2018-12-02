package ga.forntoh.bableschool.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.forntoh.EasyRecyclerView.EasyRecyclerView
import ga.forntoh.bableschool.ApiService
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.RetrofitBuilder
import ga.forntoh.bableschool.StorageUtil
import ga.forntoh.bableschool.adapters.TopSchoolsAdapter
import ga.forntoh.bableschool.adapters.TopStudentsAdapter
import ga.forntoh.bableschool.model.TopSchool
import ga.forntoh.bableschool.model.TopStudent
import io.reactivex.schedulers.Schedulers
import java.util.*

class RankingFragment : Fragment() {

    private lateinit var v: View

    private var t1: Thread? = null
    private val topStudentsAdapter by lazy { TopStudentsAdapter(topStudents) }
    private val topSchoolsAdapter by lazy { TopSchoolsAdapter(topSchools) }
    private val topStudents = ArrayList<TopStudent>()
    private val topSchools = ArrayList<TopSchool>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_ranking, container, false)

        t1 = EasyRecyclerView()
                .setType(EasyRecyclerView.Type.HORIZONTAL)
                .setAdapter(topStudentsAdapter)
                .setRecyclerView(v.findViewById(R.id.rv_top_students))
                .setItemSpacing(16, null)
                .initialize(topStudents.size, 5000, false)

        EasyRecyclerView()
                .setType(EasyRecyclerView.Type.VERTICAL)
                .setAdapter(topSchoolsAdapter)
                .setRecyclerView(v.findViewById(R.id.rv_school_ranking))
                .setItemSpacing(16, null)
                .initialize()

        fetchItems()

        return v
    }

    @SuppressLint("CheckResult")
    private fun fetchItems() {
        val service = RetrofitBuilder.createService(ApiService::class.java)
        service.topSchools
                .flatMap { list ->
                    //TODO: Save Top Schools
                    topSchools.clear()
                    topSchools.addAll(list)
                    activity!!.runOnUiThread { topSchoolsAdapter.notifyDataSetChanged() }
                    service.getTopStudents(StorageUtil.getInstance(activity!!).loadMatriculation())
                }
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { list ->
                            //TODO: Save Top Students
                            topStudents.clear()
                            topStudents.addAll(list)
                            activity!!.runOnUiThread { topStudentsAdapter.notifyDataSetChanged() }
                        }
                ) { it.printStackTrace() }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (t1 != null) t1!!.interrupt()
    }

}
