package ga.forntoh.bableschool.data.model.main

import androidx.annotation.DrawableRes
import com.raizlabs.android.dbflow.annotation.*
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.db.AppDatabase

@Table(database = AppDatabase::class)
data class Document(
        @PrimaryKey(autoincrement = true) var id: Long = 0,
        @ForeignKey(tableClass = Course::class, references = [ForeignKeyReference(columnName = "courseCode", foreignKeyColumnName = "code")]) var courseCode: String? = null,
        @Column var title: String? = null,
        @Column var author: String? = null,
        @Column var size: String? = null,
        @Column var url: String? = null
) {
    val type: Int
        @DrawableRes get() {
            val words = url!!.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val ext = words[words.size - 1].toLowerCase()
            return when (ext) {
                "pdf" -> R.drawable.ic_pdf
                "doc" -> R.drawable.ic_doc
                else -> -1
            }
        }
}