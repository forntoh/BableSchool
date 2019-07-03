package ga.forntoh.bableschool.ui.category.news.detail

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.model.main.Comment
import ga.forntoh.bableschool.data.model.main.News
import ga.forntoh.bableschool.data.model.main.toCommentView
import ga.forntoh.bableschool.internal.InsetDecoration
import ga.forntoh.bableschool.ui.base.ScopedFragment
import ga.forntoh.bableschool.ui.category.news.NewsViewModel
import ga.forntoh.bableschool.ui.category.news.NewsViewModelFactory
import ga.forntoh.bableschool.utilities.Utils
import kotlinx.android.synthetic.main.fragment_single_news.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class NewsDetailFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: NewsViewModelFactory by instance()
    private lateinit var viewModel: NewsViewModel

    private lateinit var news: News
    private val section = Section()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_single_news, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(NewsViewModel::class.java)
        bindUI()
    }

    private fun bindUI() = launch {
        viewModel.id = arguments?.getLong("news") ?: 0

        news = viewModel.singleNews.await() ?: return@launch

        btn_post.setOnClickListener { onPostClicked() }
        btn_like.setOnClickListener { onLikeClicked() }

        news_title.text = news.title
        news_description.text = news.description
        news_meta.text = getString(R.string.news_meta, news.author, DateFormat.format("EEE, MMM d, yyyy", Utils.getLongDate(news.date)), news.category)
        like_count.text = getString(R.string.like_counter, news.likes)
        if (news.isLiked) Picasso.get().load(R.drawable.ic_heart).into(btn_like)
        else btn_like.setImageResource(R.drawable.ic_heart_outline)

        if (news.thumbnail.isNullOrEmpty()) Picasso.get().load(R.drawable.placeholder).fit().centerCrop().into(news_thumbnail)
        else Picasso.get().load(news.thumbnail).placeholder(R.drawable.placeholder).fit().centerCrop().into(news_thumbnail)

        val groupAdapter = GroupAdapter<ViewHolder>().apply { add(section) }

        rv_comments.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
            adapter = groupAdapter
            addItemDecoration(InsetDecoration(16))
        }

        viewModel.comments.await().observe(this@NewsDetailFragment, Observer { comments ->
            comment_count.text = getString(R.string.comment_counter, comments?.size)
            section.update(comments.map { it.toCommentView() })
        })
        viewModel.likes.observe(this@NewsDetailFragment, Observer { likes ->
            like_count.text = getString(R.string.like_counter, likes.likes)
            if (news.isLiked) Picasso.get().load(R.drawable.ic_heart).into(btn_like)
            else btn_like.setImageResource(R.drawable.ic_heart_outline)
        })
    }

    private fun onLikeClicked() = launch {
        btn_like.isClickable = false
        news.liked = !news.liked
        viewModel.likeNews(viewModel.id, news.isLiked)
        btn_like.isClickable = true
    }

    private fun onPostClicked() = launch {
        btn_post.isClickable = false
        with(et_message.text.toString()) {
            if (!isNullOrEmpty()) {
                val comment = Comment()
                comment.newsId = news.id
                comment.message = this
                viewModel.postComment(comment)
                et_message.setText("")
            }
        }
        btn_post.isClickable = true
    }
}
