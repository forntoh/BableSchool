package ga.forntoh.bableschool.ui.news.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.raizlabs.android.dbflow.kotlinextensions.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.ApiService
import ga.forntoh.bableschool.data.model.groupie.ItemComment
import ga.forntoh.bableschool.data.model.main.Comment
import ga.forntoh.bableschool.data.model.main.News
import ga.forntoh.bableschool.data.model.main.News_Table
import ga.forntoh.bableschool.utils.InsetDecoration
import ga.forntoh.bableschool.utils.StorageUtil
import ga.forntoh.bableschool.utils.Utils
import kotlinx.android.synthetic.main.fragment_single_news.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class NewsDetailFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private lateinit var news: News
    private val section = Section()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_single_news, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (arguments != null) {
            news = (select from News::class where (News_Table.id.eq(arguments!!.getLong("news")))).result!!

            btn_post.setOnClickListener { onPostClicked() }
            btn_like.setOnClickListener { onLikeClicked() }

            news_title.text = news.title
            news_description.text = news.description
            comment_count.text = getString(R.string.comment_counter, news.comments?.size)
            news_meta.text = getString(R.string.news_meta, news.author, DateFormat.format("EEE, MMM d, yyyy", Utils.getLongDate(news.date)), news.category)
            like_count.text = getString(R.string.like_counter, news.likes)
            if (news.isLiked) Picasso.get().load(R.drawable.ic_heart).into(btn_like)
            else btn_like.setImageResource(R.drawable.ic_heart_outline)

            if (news.thumbnail.isNullOrEmpty()) Picasso.get().load(R.drawable.placeholder).fit().centerCrop().into(news_thumbnail)
            else Picasso.get().load(news.thumbnail).placeholder(R.drawable.placeholder).fit().centerCrop().into(news_thumbnail)

            val groupAdapter = GroupAdapter<ViewHolder>().apply { add(section) }

            rv_comments.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                adapter = groupAdapter
                addItemDecoration(InsetDecoration(16))
            }

            news.comments?.map { ItemComment(it.sender, it.message, it.thumbnail, it.date) }?.let { section.update(it) }
        }
    }

    private fun onLikeClicked() {
        launch {
            btn_like.isClickable = false
            ApiService().likeNews(StorageUtil.getInstance(activity!!.baseContext).loadMatriculation(), news.id.toString()).await()
            btn_like.isClickable = true

            if (news.isLiked) btn_like.setImageResource(R.drawable.ic_heart_outline)
            else Picasso.get().load(R.drawable.ic_heart).into(btn_like)
        }
    }

    @SuppressLint("CheckResult")
    private fun onPostClicked() {
        val text = et_message.text.toString()
        if (!text.isEmpty())
            launch {
                val comment = ApiService().postComment(news.id.toString(), Gson().toJson(Comment(StorageUtil.getInstance(activity!!.baseContext).loadMatriculation(), text))).await()
                comment.apply { newsId = news.id; save() }
                section.add(ItemComment(comment.sender, comment.message, comment.thumbnail, comment.date))
                et_message.setText("")
            }
    }
}
