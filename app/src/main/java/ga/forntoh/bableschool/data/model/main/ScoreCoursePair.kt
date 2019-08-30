package ga.forntoh.bableschool.data.model.main

import androidx.room.Embedded
import ga.forntoh.bableschool.data.model.groupie.ItemScore

class ScoreCoursePair {
    @Embedded
    lateinit var score: Score

    @Embedded(prefix = "Course_")
    lateinit var course: Course
}

fun ScoreCoursePair.toScoreView() = ItemScore(
        course,
        score.firstSequenceMark,
        score.secondSequenceMark,
        score.rank,
        score.termRank,
        (score.firstSequenceMark + score.secondSequenceMark) / 2.0
)