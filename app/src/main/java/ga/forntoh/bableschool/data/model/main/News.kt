package ga.forntoh.bableschool.data.model.main

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.OneToMany
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.kotlinextensions.*
import com.raizlabs.android.dbflow.structure.BaseModel
import ga.forntoh.bableschool.data.db.AppDatabase

@Table(database = AppDatabase::class)
data class News(
        @PrimaryKey var id: Long = 0,
        @Column var title: String? = null,
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