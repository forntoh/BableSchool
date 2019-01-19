package ga.forntoh.bableschool.data.model.groupie

import android.text.method.PasswordTransformationMethod
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ga.forntoh.bableschool.R
import kotlinx.android.synthetic.main.item_profile_data.view.*

data class ItemProfileData(var key: String?, var value: String?) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.profile_data_key.text = key
        viewHolder.itemView.profile_data_value.text = value
        viewHolder.itemView.profile_data_value.transformationMethod = if (key == "Password") PasswordTransformationMethod() else null
    }

    override fun getLayout() = R.layout.item_profile_data
}