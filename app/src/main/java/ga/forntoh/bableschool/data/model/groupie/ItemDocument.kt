package ga.forntoh.bableschool.data.model.groupie

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ga.forntoh.bableschool.R
import kotlinx.android.synthetic.main.item_document.view.*

data class ItemDocument(var courseCode: String?, var title: String?, var author: String?, var size: String?, var url: String?, val type: Int, val extension: String?) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.document_title.text = title
        viewHolder.itemView.document_author.text = author
        viewHolder.itemView.document_size.text = size
        viewHolder.itemView.document_thumbnail.setImageResource(type)
    }

    override fun getLayout() = R.layout.item_document
}