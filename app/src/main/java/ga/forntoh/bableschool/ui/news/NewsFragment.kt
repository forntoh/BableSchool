package ga.forntoh.bableschool.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.ApiService
import ga.forntoh.bableschool.data.model.groupie.ItemNews
import ga.forntoh.bableschool.ui.CategoryActivity
import ga.forntoh.bableschool.ui.news.detail.NewsDetailFragment
import ga.forntoh.bableschool.utils.InsetDecoration
import ga.forntoh.bableschool.utils.StorageUtil
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class NewsFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    companion object {
        fun newInstance() = NewsFragment()
    }

    private val topNewsSection = Section()
    private val allNewsSection = Section()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_news, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val topNewsAdapter = GroupAdapter<ViewHolder>().apply {
            add(topNewsSection)
            setOnItemClickListener(onItemClickListener)
        }
        val allNewsAdapter = GroupAdapter<ViewHolder>().apply {
            add(allNewsSection)
            setOnItemClickListener(onItemClickListener)
        }

        rv_top_news.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = topNewsAdapter
            addItemDecoration(InsetDecoration(16))
        }
        rv_all_news.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = allNewsAdapter
            addItemDecoration(InsetDecoration(16))
        }

        launch {
            val news = ApiService().getNews(StorageUtil.getInstance(context?.applicationContext!!).loadMatriculation()).await()
            news.forEach { it.save() }

            val mappedNews = news.map {
                ItemNews(it.id, it.title, it.isLiked, it.author, it.description, it.date, it.thumbnail, it.category, it.likes, it.isTop, it.comments)
            }

            topNewsSection.update(mappedNews.filter { it.isTop })
            allNewsSection.update(mappedNews.filterNot { it.isTop })
        }
    }

    private val onItemClickListener = OnItemClickListener { item, _ ->
        if (item is ItemNews) {
            val fragment = NewsDetailFragment().apply { arguments = Bundle().apply { putLong("news", item.key) } }
            (context as CategoryActivity).loadFragment(fragment).addToBackStack(null)
        }
    }
}
