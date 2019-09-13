package ga.forntoh.bableschool.data.model.groupie

import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ga.forntoh.bableschool.R
import kotlinx.android.synthetic.main.item_video.view.*

data class ItemVideo(var courseCode: String?, var title: String?, var author: String?, var duration: String?, var url: String, var thumbnail: String?) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.video_title.text = title
        viewHolder.itemView.video_author.text = author
        viewHolder.itemView.video_time.text = duration
        viewHolder.itemView.video_thumbnail
        if (thumbnail.isNullOrEmpty())
            Picasso.get().load(R.drawable.placeholder).fit().centerCrop().into(viewHolder.itemView.video_thumbnail)
        else
            Picasso.get().load(thumbnail).placeholder(R.drawable.placeholder).fit().centerCrop().into(viewHolder.itemView.video_thumbnail)
    }

    override fun getLayout() = R.layout.item_video
}