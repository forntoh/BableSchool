package ga.forntoh.bableschool.data.model.groupie

import androidx.core.content.ContextCompat
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.utils.in2Dp
import kotlinx.android.synthetic.main.item_summary.view.*

data class ItemScoreSummary(var average: Double, var rank: String?) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tv_average.text = average.in2Dp
        viewHolder.itemView.tv_position.text = rank
        viewHolder.itemView.tv_average.setTextColor(ContextCompat.getColor(viewHolder.itemView.context, if (average > 10) R.color.textBlue else R.color.bad))
    }

    override fun getLayout() = R.layout.item_summary
}