package ga.forntoh.bableschool.model

import android.support.annotation.DrawableRes
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.annotation.OneToMany
import com.raizlabs.android.dbflow.kotlinextensions.*
import com.raizlabs.android.dbflow.structure.BaseModel
import ga.forntoh.bableschool.AppDatabase
import ga.forntoh.bableschool.R

@Table(database = AppDatabase::class)
data class News(
        @PrimaryKey var id: Long = 0,
        @Column var title: String? = null,
        @SerializedName("liked")
        @Column var liked: Boolean = false,
        @Column var author: String? = null,
        @Column var description: String? = null,
        @Column var date: String? = null,
        @Column var thumbnail: String? = null,
        @Column var category: String? = null,
        @Column var likes: Int = 0,
        @Column var isTop: Boolean = false
) : BaseModel() {
    @get:OneToMany(methods = [OneToMany.Method.ALL])
    var comments by oneToMany { select from Comment::class where (Comment_Table.newsId.eq(id)) }

    val isLiked: Boolean
        get() {
            return liked
        }

    private var cmts: List<Comment>? = null

    override fun save(): Boolean {
        val res = super.save()
        if (!cmts.isNullOrEmpty()) cmts!!.forEach { it.newsId = id; it.save() }
        return res
    }
}

@Table(database = AppDatabase::class)
data class Score(
        @PrimaryKey(autoincrement = true) var id: Long = 0,
        @ForeignKey(saveForeignKeyModel = true) var course: Course? = null,
        @Column var firstSequenceMark: Double = 0.toDouble(),
        @Column var secondSequenceMark: Double = 0.toDouble(),
        @Column var rank: String? = null,
        @Column var termRank: String? = null
) {
    val scoreAverage: Double get() = if (firstSequenceMark < 0 || secondSequenceMark < 0) -1.0 else (firstSequenceMark + secondSequenceMark) / 2.0
    @Column
    var term: Int = 1
}

@Table(database = AppDatabase::class)
data class Course(
        @PrimaryKey var code: String? = null,
        @Column var title: String? = null
) : BaseModel() {

    @get:OneToMany(methods = [OneToMany.Method.ALL])
    var videos by oneToMany { select from Video::class where (Video_Table.courseCode.eq(code)) }

    @get:OneToMany(methods = [OneToMany.Method.ALL])
    var documents by oneToMany { select from Document::class where (Document_Table.courseCode.eq(code)) }

    private var docs: List<Document>? = null

    private var vids: List<Video>? = null

    override fun save(): Boolean {
        val res = super.save()
        if (!docs.isNullOrEmpty()) docs!!.forEach { it.courseCode = code; it.save() }
        if (!vids.isNullOrEmpty()) vids!!.forEach { it.courseCode = code; it.save() }
        return res
    }

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
        @ForeignKey(tableClass = Course::class, references = [ForeignKeyReference(columnName = "courseCode", foreignKeyColumnName = "code")]) var courseCode: String? = null,
        @Column var title: String? = null,
        @Column var author: String? = null,
        @Column var duration: String? = null,
        @Column var url: String? = null,
        @Column var thumbnail: String? = null
)

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
        @Column var message: String? = null,
        @Column var thumbnail: String? = null
) {
    @PrimaryKey
    var id: Long = 0
    @ForeignKey(tableClass = News::class, references = [ForeignKeyReference(columnName = "newsId", foreignKeyColumnName = "id")])
    var newsId: Long? = 0
    @Column
    var date: String? = null
}

@Table(database = AppDatabase::class)
data class Period(
        @PrimaryKey(autoincrement = true) var id: Long = 0,
        @Column var start: String = "",
        @Column var end: String = "",
        @Column var course: String? = "",
        @Column var dayOfWeek: Int = 0,
        @Column var color: String = "#000000"
)

data class User(
        var username: String? = null,
        var classe: String? = null,
        var picture: String? = null,
        var profileData: LinkedHashMap<String, String>? = null
)