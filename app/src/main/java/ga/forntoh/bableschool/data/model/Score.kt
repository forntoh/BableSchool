package ga.forntoh.bableschool.data.model

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.ForeignKey
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import ga.forntoh.bableschool.data.db.AppDatabase
import ga.forntoh.bableschool.data.model.main.Course

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