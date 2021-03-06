package ga.forntoh.bableschool.data.repository

import androidx.lifecycle.LiveData
import ga.forntoh.bableschool.data.model.main.ScoreCoursePair
import ga.forntoh.bableschool.data.model.other.AnnualRank

abstract class ScoreRepository : BaseRepository() {

    abstract suspend fun retrieveTermScores(term: Int): LiveData<MutableList<ScoreCoursePair>>

    abstract suspend fun retrieveTermScoresAsync(term: Int): MutableList<ScoreCoursePair>

    abstract suspend fun retrieveYearScore(): AnnualRank?

    abstract fun resetState(term: Int)
}