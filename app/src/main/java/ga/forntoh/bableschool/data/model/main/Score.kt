package ga.forntoh.bableschool.data.model.main

import com.dbflow5.annotation.Column
import com.dbflow5.annotation.ForeignKey
import com.dbflow5.annotation.PrimaryKey
import com.dbflow5.annotation.Table
import ga.forntoh.bableschool.data.db.AppDatabase
import ga.forntoh.bableschool.data.model.groupie.ItemScore

@Table(database = AppDatabase::class)
data class Score(
        @PrimaryKey
        @ForeignKey(saveForeignKeyModel = true) var course: Course? = null,
        @Column var firstSequenceMark: Double = 0.0,
        @Column var secondSequenceMark: Double = 0.0,
        @Column var rank: String? = null,
        @Column var termRank: String? = null,
        @Column var termAvg: Double = 0.0
) {
    @PrimaryKey
    var term: Int = 1
}

fun Score.toScoreView() = ItemScore(course, firstSequenceMark, secondSequenceMark, rank, termRank, (firstSequenceMark + secondSequenceMark) / 2.0)
