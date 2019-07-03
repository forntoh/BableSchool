package ga.forntoh.bableschool.data.repository

import ga.forntoh.bableschool.data.model.main.Score
import ga.forntoh.bableschool.data.model.other.AnnualRank

interface ScoreRepository {

    suspend fun retrieveTermScores(term: Int): MutableList<Score>

    suspend fun retrieveYearScore(): AnnualRank?
}