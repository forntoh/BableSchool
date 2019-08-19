package ga.forntoh.bableschool.data.model.main

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Score(
        @ColumnInfo var firstSequenceMark: Double = 0.0,
        @ColumnInfo var secondSequenceMark: Double = 0.0,
        @ColumnInfo var rank: String? = null,
        @ColumnInfo var termRank: String? = null,
        @ColumnInfo var termAvg: Double = 0.0
) {
    @PrimaryKey
    var term: Int = 1
}