package ga.forntoh.bableschool.data.model.groupie

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.model.main.Document
import ga.forntoh.bableschool.data.model.main.Video
import kotlinx.android.synthetic.main.item_course_note.view.*

data class ItemCourse(var code: String?, var title: String?, var videos: List<Video>?, var documents: List<Document>?, val abbr: String) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.subject_title.text = title
        viewHolder.itemView.subject_abbr.text = abbr
        viewHolder.itemView.tv.text = "${videos?.size} Videos | ${documents?.size} Documents"
        viewHolder.itemView.subject_circle.background = GradientDrawable().apply {
            val startColors = viewHolder.itemView.context.resources.getStringArray(R.array.start_colors)
            val endColors = viewHolder.itemView.context.resources.getStringArray(R.array.end_colors)

            orientation = GradientDrawable.Orientation.TR_BL
            shape = GradientDrawable.OVAL
            colors = intArrayOf(Color.parseColor(startColors[position % startColors.size]), Color.parseColor(endColors[position % startColors.size]))
        }
    }

    override fun getLayout() = R.layout.item_course_note
}