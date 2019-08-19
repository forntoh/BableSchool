package ga.forntoh.bableschool.data.model.main

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ga.forntoh.bableschool.data.model.groupie.ItemVideo

@Entity(
        foreignKeys = [ForeignKey(
                entity = Course::class,
                parentColumns = ["code"],
                childColumns = ["courseCode"]
        )]
)
data class Video(
        @ColumnInfo(index = true) var courseCode: String? = null,
        @ColumnInfo var title: String? = null,
        @ColumnInfo var author: String? = null,
        @ColumnInfo var duration: String? = null,
        @PrimaryKey var url: String = "",
        @ColumnInfo var thumbnail: String? = null
)

fun Video.toVideoView() = ItemVideo(courseCode, title, author, duration, url, thumbnail)
