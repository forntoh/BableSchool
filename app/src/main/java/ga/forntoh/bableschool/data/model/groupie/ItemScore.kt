package ga.forntoh.bableschool.data.model.groupie

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.model.main.Course
import ga.forntoh.bableschool.utils.in2Dp
import kotlinx.android.synthetic.main.item_term_subject.view.*

data class ItemScore(var course: Course?, var firstSequenceMark: Double, var secondSequenceMark: Double, var rank: String?, var termRank: String?, var scoreAverage: Double) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.subject_title.text = course?.title
        viewHolder.itemView.subject_abbr.text = course?.abbr
        viewHolder.itemView.subject_rank.text = rank
        viewHolder.itemView.subject_first.text = firstSequenceMark.in2Dp
        viewHolder.itemView.subject_second.text = secondSequenceMark.in2Dp
        viewHolder.itemView.subject_average.text = scoreAverage.in2Dp

        val context = viewHolder.itemView.context

        viewHolder.itemView.subject_first.setTextColor(ContextCompat.getColor(context, if (firstSequenceMark > 10) R.color.good else R.color.bad))
        viewHolder.itemView.subject_second.setTextColor(ContextCompat.getColor(context, if (secondSequenceMark > 10) R.color.good else R.color.bad))
        viewHolder.itemView.subject_average.setTextColor(ContextCompat.getColor(context, if (scoreAverage > 10) R.color.good else R.color.bad))

        viewHolder.itemView.subject_circle.background = GradientDrawable().apply {
            val startColors = viewHolder.itemView.context.resources.getStringArray(R.array.start_colors)
            val endColors = viewHolder.itemView.context.resources.getStringArray(R.array.end_colors)

            orientation = GradientDrawable.Orientation.TR_BL
            shape = GradientDrawable.OVAL
            colors = intArrayOf(Color.parseColor(startColors[position % startColors.size]), Color.parseColor(endColors[position % startColors.size]))
        }
    }

    override fun getLayout() = R.layout.item_term_subject
}