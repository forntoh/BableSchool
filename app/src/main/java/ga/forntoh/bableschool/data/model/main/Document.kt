package ga.forntoh.bableschool.data.model.main

import androidx.annotation.DrawableRes
import com.dbflow5.annotation.*
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.db.AppDatabase
import ga.forntoh.bableschool.data.model.groupie.ItemDocument

@Table(database = AppDatabase::class)
data class Document(
        @ForeignKey(tableClass = Course::class, references = [ForeignKeyReference(columnName = "courseCode", foreignKeyColumnName = "code")]) var courseCode: String? = null,
        @Column var title: String? = null,
        @Column var author: String? = null,
        @Column var size: String? = null,
        @PrimaryKey var url: String? = null
) {
    val type: Int
        @DrawableRes get() {
            return when (extension) {
                "pdf" -> R.drawable.ic_pdf
                "doc" -> R.drawable.ic_doc
                else -> -1
            }
        }

    val extension: String
        get() {
            val words = url!!.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            return words[words.size - 1].toLowerCase()
        }
}

fun Document.toDocumentView() = ItemDocument(courseCode, title, author, size, url, type, extension)