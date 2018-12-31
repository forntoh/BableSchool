package ga.forntoh.bableschool.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.forntoh.EasyRecyclerView.EasyRecyclerView
import com.google.gson.Gson
import com.raizlabs.android.dbflow.kotlinextensions.*
import com.squareup.picasso.Picasso
import ga.forntoh.bableschool.ApiService
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.RetrofitBuilder
import ga.forntoh.bableschool.StorageUtil
import ga.forntoh.bableschool.adapters.CommentsAdapter
import ga.forntoh.bableschool.model.Comment
import ga.forntoh.bableschool.model.News
import ga.forntoh.bableschool.model.News_Table
import ga.forntoh.bableschool.utils.Utils
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_single_news.*
import java.util.*

class SingleNewsFragment : Fragment() {

    private val adapter by lazy { CommentsAdapter(news.comments as ArrayList<Comment>) }
    private lateinit var news: News

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

            EasyRecyclerView()
                    .setType(EasyRecyclerView.Type.VERTICAL)
                    .setAdapter(adapter)
                    .setRecyclerView(rv_comments)
                    .setItemSpacing(16, null)
                    .initialize()

            adapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("CheckResult")
    private fun onLikeClicked() {
        btn_like.isClickable = false
        val service = RetrofitBuilder.createService(ApiService::class.java)
        service.likeNews(StorageUtil.getInstance(activity!!.baseContext).loadMatriculation(), news.id.toString())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {
                            btn_like.isClickable = true
                            btn_like.post {
                                if (news.isLiked) btn_like.setImageResource(R.drawable.ic_heart_outline)
                                else Picasso.get().load(R.drawable.ic_heart).into(btn_like)
                            }
                        },
                        { btn_like.post { btn_like.isClickable = true; Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show() } })
    }

    @SuppressLint("CheckResult")
    private fun onPostClicked() {
        val text = et_message.text.toString()
        if (!text.isEmpty()) {
            val service = RetrofitBuilder.createService(ApiService::class.java)
            service.postComment(news.id.toString(), Gson().toJson(Comment(StorageUtil.getInstance(activity!!.baseContext).loadMatriculation(), text)))
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            {
                                activity!!.runOnUiThread {
                                    adapter.addComment(it.apply { newsId = news.id; it.save() })
                                    et_message.setText("")
                                }
                            },
                            { Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show() })
        }
    }
}
