package ga.forntoh.bableschool.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.forntoh.EasyRecyclerView.EasyRecyclerView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.adapters.CommentsAdapter
import ga.forntoh.bableschool.model.Comment
import ga.forntoh.bableschool.model.News
import ga.forntoh.bableschool.utils.Utils
import java.text.SimpleDateFormat
import java.util.*

class SingleNewsFragment : Fragment() {

    private lateinit var v: View
    private val adapter by lazy { CommentsAdapter(news.comments as ArrayList<*>) }
    private val commentBox by lazy { v.findViewById<EditText>(R.id.et_message) }
    private val postBtn by lazy { v.findViewById<Button>(R.id.btn_post) }
    private val news by lazy { Gson().fromJson(arguments!!.getString("news"), News::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_single_news, container, false)

        if (arguments != null) {
            val title = v.findViewById<TextView>(R.id.news_title)
            val meta = v.findViewById<TextView>(R.id.news_meta)
            val description = v.findViewById<TextView>(R.id.news_description)
            val commentCountV = v.findViewById<TextView>(R.id.comment_count)
            val likeCountV = v.findViewById<TextView>(R.id.like_count)
            val thumbnail = v.findViewById<ImageView>(R.id.news_thumbnail)

            postBtn.setOnClickListener { onPostClicked() }

            title.text = news.title
            description.text = news.description
            commentCountV.text = getString(R.string.comment_counter, news.comments?.size)
            meta.text = getString(R.string.news_meta, news.author, DateFormat.format("EEE, MMM d, yyyy", Utils.getLongDate(news.date)), news.category)
            likeCountV.text = getString(R.string.like_counter, news.likes)

            if (news.thumbnail.isNullOrEmpty()) Picasso.get().load(R.drawable.placeholder).fit().centerCrop().into(thumbnail)
            else Picasso.get().load(news.thumbnail).placeholder(R.drawable.placeholder).fit().centerCrop().into(thumbnail)

            EasyRecyclerView()
                    .setType(EasyRecyclerView.Type.VERTICAL)
                    .setAdapter(adapter)
                    .setRecyclerView(v.findViewById(R.id.rv_comments))
                    .setItemSpacing(16, null)
                    .initialize()

            adapter.notifyDataSetChanged()
        }
        return v
    }

    private fun onPostClicked() {
        val text = commentBox.text.toString()
        if (!TextUtils.isEmpty(text)) {
            //TODO: Post comment to server
            val comment = Comment("Michy", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()), text, "https://images.pexels.com/photos/450271/pexels-photo-450271.jpeg?auto=compress&cs=tinysrgb&h=250")
            (news.comments as ArrayList<Comment>).add(comment)
            adapter.notifyItemInserted(news.comments!!.size)
            commentBox.setText("")
        }
    }

}
