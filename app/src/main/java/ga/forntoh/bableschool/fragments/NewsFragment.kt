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
import ga.forntoh.bableschool.adapters.NewsAdapter
import ga.forntoh.bableschool.model.News
import io.reactivex.schedulers.Schedulers
import java.util.*

class NewsFragment : Fragment() {

    private var t1: Thread? = null
    private val topNewsList = ArrayList<News>()
    private val allNewsList = ArrayList<News>()
    private val newsAdapter1 by lazy { NewsAdapter(topNewsList, false) }
    private val newsAdapter2 by lazy { NewsAdapter(allNewsList, true) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_news, container, false)

        t1 = EasyRecyclerView()
                .setType(EasyRecyclerView.Type.HORIZONTAL)
                .setAdapter(newsAdapter1)
                .setRecyclerView(v.findViewById(R.id.rv_top_news))
                .setItemSpacing(16, null)
                .initialize(topNewsList.size, 5000, true)

        EasyRecyclerView()
                .setType(EasyRecyclerView.Type.VERTICAL)
                .setAdapter(newsAdapter2)
                .setRecyclerView(v.findViewById(R.id.rv_all_news))
                .setItemSpacing(16, null)
                .initialize()

        fetchItems()

        return v
    }

    @SuppressLint("CheckResult")
    private fun fetchItems() {
        val service = RetrofitBuilder.createService(ApiService::class.java)

        service.news
                .subscribeOn(Schedulers.io())
                .subscribe({ news ->
                    //TODO: Save News T
                    topNewsList.clear()
                    topNewsList.addAll(news)
                    newsAdapter1.notifyDataSetChanged()
                }) { it.printStackTrace() }

        service.news
                .subscribeOn(Schedulers.io())
                .subscribe({ news ->
                    //TODO: Save News A
                    allNewsList.clear()
                    allNewsList.addAll(news)
                    newsAdapter2.notifyDataSetChanged()
                }) { it.printStackTrace() }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (t1 != null) t1!!.interrupt()
    }

}
