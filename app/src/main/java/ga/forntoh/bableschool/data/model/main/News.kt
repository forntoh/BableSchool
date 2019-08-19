package ga.forntoh.bableschool.data.model.main

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import ga.forntoh.bableschool.data.model.groupie.ItemNews

@Entity
data class News(
        @PrimaryKey var id: Long = 0,
        @ColumnInfo var title: String? = null,
        @ColumnInfo var liked: Boolean = false,
        @ColumnInfo var author: String? = null,
        @ColumnInfo var description: String? = null,
        @ColumnInfo var date: String? = null,
        @ColumnInfo var thumbnail: String? = null,
        @ColumnInfo var category: String? = null,
        @ColumnInfo var likes: Int = 0,
        @ColumnInfo var isTop: Boolean = false
) {
    val isLiked: Boolean
        @Ignore get() {
            return liked
        }
}

fun News.toNewsView() = ItemNews(id, title, isLiked, author, description, date, thumbnail, category, likes, isTop)