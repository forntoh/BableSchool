package ga.forntoh.bableschool.data.model.groupie

import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.utilities.Utils
import kotlinx.android.synthetic.main.item_comment.view.*
import java.text.SimpleDateFormat
import java.util.*

data class ItemComment(var sender: String?, var message: String?, var thumbnail: String?, var date: String?) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.comment_sender.text = sender
        viewHolder.itemView.comment_message.text = message
        viewHolder.itemView.comment_date.text = Utils.getRelativeTimeSpanString(date, SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()))
        if (thumbnail.isNullOrEmpty())
            Picasso.get().load(R.drawable.placeholder).fit().centerCrop().into(viewHolder.itemView.author_image)
        else
            Picasso.get().load(thumbnail).placeholder(R.drawable.placeholder).fit().centerCrop().into(viewHolder.itemView.author_image)
    }

    override fun getLayout() = R.layout.item_comment

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        return if (other is ItemComment) other.sender == this.sender
                && other.message == this.message
                && other.date == this.date
        else super.isSameAs(other)
    }
}