package ga.forntoh.bableschool.data.model.main

import com.dbflow5.annotation.*
import com.dbflow5.database.DatabaseWrapper
import com.dbflow5.query.select
import com.dbflow5.structure.BaseModel
import com.dbflow5.structure.oneToMany
import com.dbflow5.structure.save
import ga.forntoh.bableschool.data.db.AppDatabase
import ga.forntoh.bableschool.data.model.groupie.ItemNews

@Table(database = AppDatabase::class, allFields = false)
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
    @get:OneToMany(oneToManyMethods = [OneToManyMethod.ALL])
    var comments by oneToMany { select from Comment::class where (Comment_Table.newsId.eq(id)) }

    val isLiked: Boolean
        get() {
            return liked
        }

    private var cmts: List<Comment>? = null

    override fun save(wrapper: DatabaseWrapper): Boolean {
        val res = super.save(wrapper)
        if (!cmts.isNullOrEmpty()) cmts!!.forEach { it.newsId = id; it.save() }
        return res
    }
}

fun News.toNewsView() = ItemNews(id, title, isLiked, author, description, date, thumbnail, category, likes, isTop, comments)