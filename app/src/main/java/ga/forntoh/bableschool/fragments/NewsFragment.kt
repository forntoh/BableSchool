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
import ga.forntoh.bableschool.ApiService
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.RetrofitBuilder
import ga.forntoh.bableschool.StorageUtil
import ga.forntoh.bableschool.adapters.NewsAdapter
import ga.forntoh.bableschool.model.News
import ga.forntoh.bableschool.utils.Utils
import ga.forntoh.bableschool.utils.Utils.dealWithData
import io.reactivex.schedulers.Schedulers
import java.util.*

class NewsFragment : Fragment() {

    private var t1: Thread? = null
    private val topNewsList = ArrayList<News>()
    private val allNewsList = ArrayList<News>()
    private val newsAdapter1 by lazy { NewsAdapter(topNewsList, false) }
    private val newsAdapter2 by lazy { NewsAdapter(allNewsList, true) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_news, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        t1 = EasyRecyclerView()
                .setType(EasyRecyclerView.Type.HORIZONTAL)
                .setAdapter(newsAdapter1)
                .setRecyclerView(view.findViewById(R.id.rv_top_news))
                .setItemSpacing(16, null)
                .initialize(topNewsList.size, 5000, true)

        EasyRecyclerView()
                .setType(EasyRecyclerView.Type.VERTICAL)
                .setAdapter(newsAdapter2)
                .setRecyclerView(view.findViewById(R.id.rv_all_news))
                .setItemSpacing(16, null)
                .initialize()
        fetchItems()
    }

    private fun fetchItems() {
        val service = RetrofitBuilder.createService(ApiService::class.java)

        if (Utils.isConnected(activity!!))
            service.getNews(StorageUtil.getInstance(context?.applicationContext!!).loadMatriculation())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ news ->
                        news.forEach { it.save() }
                        dealWithData(activity!!, news.filter { n -> n.isTop }, topNewsList, newsAdapter1)
                        dealWithData(activity!!, news, allNewsList, newsAdapter2)
                    }) {
                        val news = (select from News::class).list
                        if (!news.isNullOrEmpty()) {
                            dealWithData(activity!!, news.filter { n -> n.isTop }, topNewsList, newsAdapter1)
                            dealWithData(activity!!, news, allNewsList, newsAdapter2)
                        }
                    }
        else {
            val news = (select from News::class).list
            dealWithData(activity!!, news.filter { n -> n.isTop }, topNewsList, newsAdapter1)
            dealWithData(activity!!, news, allNewsList, newsAdapter2)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (t1 != null) t1!!.interrupt()
    }

}
