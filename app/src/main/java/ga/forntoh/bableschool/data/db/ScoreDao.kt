package ga.forntoh.bableschool.data.db

import ga.forntoh.bableschool.data.model.main.Score
import ga.forntoh.bableschool.data.model.other.AnnualRank

interface ScoreDao {

    fun retrieveTermScores(term: Int): MutableList<Score>

    fun retrieveYearScore(): AnnualRank?

    fun saveTermScores(scores: List<Score>)

    fun saveYearScore(annualRank: AnnualRank)
}