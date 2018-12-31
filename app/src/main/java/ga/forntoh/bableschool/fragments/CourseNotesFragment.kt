package ga.forntoh.bableschool.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.forntoh.EasyRecyclerView.EasyRecyclerView
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.list
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.sql.language.Delete
import ga.forntoh.bableschool.ApiService
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.RetrofitBuilder
import ga.forntoh.bableschool.StorageUtil
import ga.forntoh.bableschool.adapters.CourseNotesAdapter
import ga.forntoh.bableschool.model.Course
import ga.forntoh.bableschool.utils.Utils
import ga.forntoh.bableschool.utils.Utils.dealWithData
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_course_notes.*

class CourseNotesFragment : Fragment() {

    private val adapter by lazy { CourseNotesAdapter(list) }
    private val list = ArrayList<Course>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_course_notes, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EasyRecyclerView().apply {
            setType(EasyRecyclerView.Type.VERTICAL)
            setAdapter(adapter)
            setRecyclerView(rv_course_notes)
            setItemSpacing(16, null)
            initialize()
        }
        fetchItems()
    }

    private fun fetchItems() {
        val service = RetrofitBuilder.createService(ApiService::class.java)
        if (Utils.isConnected(activity!!))
            service.getCourseNotes(StorageUtil.getInstance(activity!!.baseContext).loadMatriculation())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ courses ->
                        Delete.table(Course::class.java)
                        if (!courses.isNullOrEmpty()) courses.forEach { it.save() }
                        dealWithData(activity!!, (select from Course::class).list, list, adapter)
                    }, { dealWithData(activity!!, (select from Course::class).list, list, adapter) })
        else dealWithData(activity!!, (select from Course::class).list, list, adapter)
    }
}
