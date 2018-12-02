package ga.forntoh.bableschool.model

import android.support.annotation.DrawableRes
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.oneToMany
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.kotlinextensions.where
import ga.forntoh.bableschool.AppDatabase
import ga.forntoh.bableschool.R

@Table(database = AppDatabase::class)
data class News(
        @PrimaryKey(autoincrement = true) var id: Long = 0,
        @Column var title: String? = null,
        @Column var author: String? = null,
        @Column var description: String? = null,
        @Column var date: String? = null,
        @Column var thumbnail: String? = null,
        @Column var category: String? = null,
        @Column var likes: Int = 0
) {
    @get:OneToMany(methods = [OneToMany.Method.ALL])
    var comments by oneToMany { select from Comment::class where (Comment_Table.newsId.eq(id)) }
}

@Table(database = AppDatabase::class)
data class Score(
        @PrimaryKey(autoincrement = true) var id: Long = 0,
        @ForeignKey var course: Course? = null,
        @Column var firstSequenceMark: Double = 0.toDouble(),
        @Column var secondSequenceMark: Double = 0.toDouble(),
        @Column var rank: String? = null,
        @Column var termRank: String? = null
) {
    val scoreAverage: Double get() = if (firstSequenceMark < 0 || secondSequenceMark < 0) -1.0 else (firstSequenceMark + secondSequenceMark) / 2.0
    var term: Int = 1
}

@Table(database = AppDatabase::class)
data class Course(
        @PrimaryKey(autoincrement = true) var id: Long = 0,
        @Column var code: String? = null,
        @Column var title: String? = null
) {

    @get:OneToMany(methods = [OneToMany.Method.ALL])
    var videos by oneToMany { select from Video::class where (Video_Table.courseId.eq(id)) }

    @get:OneToMany(methods = [OneToMany.Method.ALL])
    var documents by oneToMany { select from Document::class where (Document_Table.courseId.eq(id)) }

    val abbr: String
        get() {
            val words = this.title!!.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            var title = ""
            return if (words.size > 1) {
                for (s in words) title += s[0]; title
            } else this.title!!.substring(0, 3)
        }
}

@Table(database = AppDatabase::class)
data class Video(
        @PrimaryKey(autoincrement = true) var id: Long = 0,
        @ForeignKey(tableClass = Course::class, references = [ForeignKeyReference(columnName = "courseId", foreignKeyColumnName = "id")]) var courseId: Long? = 0,
        @Column var title: String? = null,
        @Column var author: String? = null,
        @Column var duration: String? = null,
        @Column var url: String? = null,
        @Column var thumbnail: String? = null
)

@Table(database = AppDatabase::class)
data class Document(
        @PrimaryKey(autoincrement = true) var id: Long = 0,
        @ForeignKey(tableClass = Course::class, references = [ForeignKeyReference(columnName = "courseId", foreignKeyColumnName = "id")]) var courseId: Long? = 0,
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

@Table(database = AppDatabase::class)
data class Category(
        @PrimaryKey(autoincrement = true) var id: Long = 0,
        @Column var title: String? = null,
        @Column var thumbnail: String? = null,
        @Column var color: String? = null
)

@Table(database = AppDatabase::class)
data class Comment(
        @Column var sender: String? = null,
        @Column var date: String? = null,
        @Column var message: String? = null,
        @Column var thumbnail: String? = null
) {
    @PrimaryKey(autoincrement = true)
    var id: Long = 0
    @ForeignKey(tableClass = News::class, references = [ForeignKeyReference(columnName = "newsId", foreignKeyColumnName = "id")])
    var newsId: Long? = 0
}

@Table(database = AppDatabase::class)
data class Period(
        @PrimaryKey(autoincrement = true) var id: Long = 0,
        @Column var start: String = "",
        @Column var end: String = "",
        @Column var course: String? = "",
        @Column var dayOfWeek: Int = 0,
        @Column var color: String? = ""
)

data class User(
        var username: String? = null,
        var classe: String? = null,
        var picture: String? = null,
        var profileData: LinkedHashMap<String, String>? = null
)