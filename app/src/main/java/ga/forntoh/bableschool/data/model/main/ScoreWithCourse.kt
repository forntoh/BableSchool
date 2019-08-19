package ga.forntoh.bableschool.data.model.main

import androidx.room.Embedded
import androidx.room.Relation
import ga.forntoh.bableschool.data.model.groupie.ItemScore

data class ScoreWithCourse(
        @Embedded var score: Score? = null,
        @Relation(parentColumn = "term", entityColumn = "code") var course: Course? = null
)

fun ScoreWithCourse.toScoreView() = ItemScore(course, score!!.firstSequenceMark, score!!.secondSequenceMark, score!!.rank, score!!.termRank, (score!!.firstSequenceMark + score!!.secondSequenceMark) / 2.0)
