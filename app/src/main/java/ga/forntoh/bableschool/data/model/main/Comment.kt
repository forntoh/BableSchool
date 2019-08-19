package ga.forntoh.bableschool.data.model.main

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ga.forntoh.bableschool.data.model.groupie.ItemComment

@Entity(
        foreignKeys = [ForeignKey(
                entity = News::class,
                parentColumns = ["id"],
                childColumns = ["newsId"],
                onDelete = ForeignKey.CASCADE
        )]
)
data class Comment(
        @ColumnInfo var sender: String? = null,
        @ColumnInfo var message: String? = null,
        @ColumnInfo var thumbnail: String? = null
) {
    @PrimaryKey
    var id: Long = 0
    @ColumnInfo(index = true)
    var newsId: Long? = 0
    @ColumnInfo
    var date: String? = null
}

fun Comment.toCommentView() = ItemComment(sender, message, thumbnail, date)