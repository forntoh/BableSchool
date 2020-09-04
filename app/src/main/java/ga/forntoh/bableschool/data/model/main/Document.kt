package ga.forntoh.bableschool.data.model.main

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.model.groupie.ItemDocument

@Entity(foreignKeys = [ForeignKey(
        entity = Course::class,
        parentColumns = ["code"],
        childColumns = ["courseCode"],
        onDelete = ForeignKey.CASCADE
)])
data class Document(
        @ColumnInfo(index = true) var courseCode: String? = null,
        @ColumnInfo var title: String? = null,
        @ColumnInfo var author: String? = null,
        @ColumnInfo var size: String? = null,
        @PrimaryKey var url: String = ""
) {
    val type: Int
        @DrawableRes get() {
            return when (extension) {
                "pdf" -> R.drawable.ic_pdf
                "doc" -> R.drawable.ic_doc
                else -> R.drawable.ic_pdf
            }
        }

    val extension: String
        get() {
            val words = url.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            return if (words.isNotEmpty()) words[words.size - 1].toLowerCase() else "pdf"
        }
}

fun Document.toDocumentView() = ItemDocument(courseCode, title, author, size, url, type, extension)