package ga.forntoh.bableschool.data.model.groupie

import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.utilities.capitalizeEachWord
import ga.forntoh.bableschool.utilities.in2Dp
import kotlinx.android.synthetic.main.item_school_ranking.view.*

data class ItemTopSchool(var schoolName: String?, var image: String?, var topStudentName: String, var average: Double) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.top_student_name.text = topStudentName.capitalizeEachWord
        viewHolder.itemView.top_school_name.text = schoolName
        viewHolder.itemView.top_school_average.text = average.in2Dp
        viewHolder.itemView.top_school_name.isSelected = true
        if (image.isNullOrEmpty())
            Picasso.get().load(R.drawable.placeholder).fit().centerCrop().into(viewHolder.itemView.top_school_image)
        else
            Picasso.get().load(image).placeholder(R.drawable.placeholder).fit().centerCrop().into(viewHolder.itemView.top_school_image)
    }

    override fun getLayout() = R.layout.item_school_ranking

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        return if (other is ItemTopSchool) other.schoolName == this.schoolName
                && other.topStudentName == this.topStudentName
                && other.average == this.average
        else super.isSameAs(other)
    }
}