package ga.forntoh.bableschool.data.model.main

import com.dbflow5.annotation.Column
import com.dbflow5.annotation.ForeignKey
import com.dbflow5.annotation.PrimaryKey
import com.dbflow5.annotation.Table
import ga.forntoh.bableschool.data.db.AppDatabase

@Table(database = AppDatabase::class)
data class Score(
        @PrimaryKey
        @ForeignKey(saveForeignKeyModel = true) var course: Course? = null,
        @Column var firstSequenceMark: Double = 0.toDouble(),
        @Column var secondSequenceMark: Double = 0.toDouble(),
        @Column var rank: String? = null,
        @Column var termRank: String? = null
) {
    val scoreAverage: Double get() = if (firstSequenceMark < 0 || secondSequenceMark < 0) -1.0 else (firstSequenceMark + secondSequenceMark) / 2.0
    @PrimaryKey
    var term: Int = 1
}
