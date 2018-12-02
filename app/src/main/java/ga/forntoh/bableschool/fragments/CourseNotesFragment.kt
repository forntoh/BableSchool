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
import ga.forntoh.bableschool.adapters.CourseNotesAdapter
import ga.forntoh.bableschool.model.Course
import io.reactivex.schedulers.Schedulers
import java.util.*

class CourseNotesFragment : Fragment() {

    private val adapter by lazy { CourseNotesAdapter(list) }
    private val list = ArrayList<Course>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_course_notes, container, false)

        EasyRecyclerView().apply {
            setType(EasyRecyclerView.Type.VERTICAL)
            setAdapter(adapter)
            setRecyclerView(view.findViewById(R.id.rv_course_notes))
            setItemSpacing(16, null)
            initialize()
        }

        fetchItems()
        return view
    }

    @SuppressLint("CheckResult")
    private fun fetchItems() {
        val service = RetrofitBuilder.createService(ApiService::class.java)
        service.getCourseNotes(StorageUtil.getInstance(context!!).loadMatriculation())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { courses ->
                            //TODO: Save Course Notes
                            list.clear()
                            list.addAll(courses)
                            activity!!.runOnUiThread { adapter.notifyDataSetChanged() }
                        }
                ) { it.printStackTrace() }
    }
}
