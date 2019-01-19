package ga.forntoh.bableschool.data.model.groupie

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.utils.SquareConstraintLayout
import ga.forntoh.bableschool.utils.inPx
import kotlinx.android.synthetic.main.item_category.view.*

data class ItemCategory(var title: String?, var thumbnail: String?, var color: String?) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.category_title.text = title
        viewHolder.itemView.category_title.setTextColor(Color.parseColor(color))
        viewHolder.itemView.category_icon.setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_ATOP)

        if (thumbnail.isNullOrEmpty()) Picasso.get().load(R.drawable.placeholder).into(viewHolder.itemView.category_icon)
        else Picasso.get().load(thumbnail).placeholder(R.drawable.placeholder).into(viewHolder.itemView.category_icon)

        val drawable = viewHolder.itemView.findViewById<SquareConstraintLayout>(R.id.parent).background as GradientDrawable
        drawable.setStroke(2.5f.inPx, Color.parseColor(color))
    }

    override fun getLayout() = R.layout.item_category

    override fun getSpanSize(spanCount: Int, position: Int) = spanCount / 2
}