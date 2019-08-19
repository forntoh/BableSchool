package ga.forntoh.bableschool.data.repository

import androidx.lifecycle.LiveData
import ga.forntoh.bableschool.data.model.main.ScoreWithCourse
import ga.forntoh.bableschool.data.model.other.AnnualRank

abstract class ScoreRepository : BaseRepository() {

    abstract suspend fun retrieveTermScores(term: Int): LiveData<MutableList<ScoreWithCourse>>

    abstract suspend fun retrieveYearScore(): AnnualRank?
}