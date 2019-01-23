package ga.forntoh.bableschool.data.db

import com.dbflow5.query.select
import com.dbflow5.structure.save
import ga.forntoh.bableschool.data.model.main.Score
import ga.forntoh.bableschool.data.model.main.Score_Table
import ga.forntoh.bableschool.data.model.other.AnnualRank

class ScoreDaoImpl(private val database: AppDatabase) : ScoreDao {

    override fun retrieveTermScores(term: Int): MutableList<Score> =
            (select from Score::class where (Score_Table.term eq term)).queryList(database)

    override fun retrieveYearScore(): AnnualRank? =
            (select from AnnualRank::class).querySingle(database)

    override fun saveTermScores(scores: List<Score>) =
            scores.forEach { it.save(database) }

    override fun saveYearScore(annualRank: AnnualRank) {
        annualRank.save(database)
    }
}