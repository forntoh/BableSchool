package ga.forntoh.bableschool.data.model.groupie

import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.utilities.capitalizeEachWord
import ga.forntoh.bableschool.utilities.in2Dp
import kotlinx.android.synthetic.main.item_top_students.view.*

data class ItemTopStudent(var name: String, var surname: String?, var image: String?, var school: String?, var average: Double) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.top_student_name.text = name.capitalizeEachWord
        viewHolder.itemView.top_student_school.text = school
        viewHolder.itemView.top_student_average.text = average.in2Dp
        if (image.isNullOrEmpty())
            Picasso.get().load(R.drawable.placeholder).fit().centerCrop().into(viewHolder.itemView.top_student_image)
        else
            Picasso.get().load(image).placeholder(R.drawable.placeholder).fit().centerCrop().into(viewHolder.itemView.top_student_image)
    }

    override fun getLayout() = R.layout.item_top_students

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        return if (other is ItemTopStudent) other.name == this.name
                && other.surname == this.surname
                && other.average == this.average
        else super.isSameAs(other)
    }
}