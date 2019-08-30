package ga.forntoh.bableschool.data.model.main

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore

@Entity(
        primaryKeys = ["course_code", "term"], foreignKeys = [ForeignKey(
        entity = Course::class,
        parentColumns = ["code"],
        childColumns = ["course_code"],
        onDelete = ForeignKey.CASCADE)]
)
data class Score(
        @ColumnInfo var firstSequenceMark: Double = 0.0,
        @ColumnInfo var secondSequenceMark: Double = 0.0,
        @ColumnInfo var rank: String? = null,
        @ColumnInfo var termRank: String? = null,
        @ColumnInfo var termAvg: Double = 0.0,
        @ColumnInfo var course_code: String = "",
        @Ignore var course: Course? = null
) {
    @ColumnInfo
    var term: Int = 1
}