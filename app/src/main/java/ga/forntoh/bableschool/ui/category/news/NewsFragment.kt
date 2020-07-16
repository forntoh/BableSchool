package ga.forntoh.bableschool.ui.category.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.model.groupie.ItemNews
import ga.forntoh.bableschool.data.model.main.toNewsView
import ga.forntoh.bableschool.internal.InsetDecoration
import ga.forntoh.bableschool.ui.base.ScopedFragment
import ga.forntoh.bableschool.ui.category.CategoryActivity
import ga.forntoh.bableschool.ui.category.news.detail.NewsDetailFragment
import ga.forntoh.bableschool.utilities.invalidateViewState
import ga.forntoh.bableschool.utilities.toggleViewState
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class NewsFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: NewsViewModelFactory by instance<NewsViewModelFactory>()
    private lateinit var viewModel: NewsViewModel

    private val topNewsSection = Section()
    private val allNewsSection = Section()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_news, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(NewsViewModel::class.java)
        init()
    }

    private fun init() {
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

        loadData()
        (activity as CategoryActivity).srl.setOnRefreshListener {
            viewModel.resetState()
            loadData()
        }
    }

    private fun loadData() = launch {
        rv_top_news.invalidateViewState()
        rv_all_news.invalidateViewState()

        viewModel.getAllNews().observe(viewLifecycleOwner, Observer { news ->
            if (!news.isNullOrEmpty()) {
                val mappedNews = news.map {
                    viewModel.id = it.id
                    it.toNewsView()
                }
                rv_top_news.toggleViewState(topNewsSection.apply { update(mappedNews.filter { it.isTop }) })
                rv_all_news.toggleViewState(allNewsSection.apply { update(mappedNews.filterNot { it.isTop }.asReversed()) })
                (activity as CategoryActivity).srl.isRefreshing = false
            } else {
                for (i in 0 until allNewsSection.itemCount) {
                    allNewsSection.remove(allNewsSection.getGroup(0))
                }
            }
        })
    }

    private val onItemClickListener = OnItemClickListener { item, _ ->
        if (item is ItemNews) {
            val fragment = NewsDetailFragment().apply { arguments = Bundle().apply { putLong("news", item.key) } }
            (context as CategoryActivity).loadFragment(fragment).addToBackStack(null)
        }
    }
}
