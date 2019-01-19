package ga.forntoh.bableschool.data.model.groupie

import android.text.format.DateFormat
import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.model.main.Comment
import ga.forntoh.bableschool.utils.Utils
import ga.forntoh.bableschool.utils.inPx
import ga.forntoh.bableschool.utils.screenWidth
import kotlinx.android.synthetic.main.item_news_top.view.*
import java.text.SimpleDateFormat
import java.util.*

data class ItemNews(var key: Long, var title: String?, var liked: Boolean, var author: String?, var description: String?, var date: String?, var thumbnail: String?, var category: String?, var likes: Int, var isTop: Boolean, var comments: List<Comment>?) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.news_title.text = title
        viewHolder.itemView.news_meta.text = if (!isTop) Utils.getRelativeTimeSpanString(date, SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())) + "  |  " + "@" + author else "By " + author + " on " + DateFormat.format("EEE, MMM d, yyyy", Utils.getLongDate(date))
        if (thumbnail.isNullOrEmpty()) Picasso.get().load(R.drawable.placeholder).fit().centerCrop().into(viewHolder.itemView.news_thumbnail)
        else Picasso.get().load(thumbnail).placeholder(R.drawable.placeholder).fit().centerCrop().into(viewHolder.itemView.news_thumbnail)
        if (isTop) viewHolder.itemView.card_parent.layoutParams.width = 1.screenWidth - 56.inPx
    }

    override fun getLayout() = if (isTop) R.layout.item_news_top else R.layout.item_news
}
