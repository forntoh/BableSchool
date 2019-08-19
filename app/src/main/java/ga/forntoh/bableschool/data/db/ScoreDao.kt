package ga.forntoh.bableschool.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import ga.forntoh.bableschool.data.model.main.Score
import ga.forntoh.bableschool.data.model.main.ScoreWithCourse
import ga.forntoh.bableschool.data.model.other.AnnualRank

@Dao
interface ScoreDao {

    @Transaction
    @Query("SELECT * FROM Score WHERE Score.term LIKE :term")
    fun retrieveTermScores(term: Int): LiveData<MutableList<ScoreWithCourse>>

    @Query("SELECT * FROM AnnualRank LIMIT 1")
    fun retrieveYearScore(): AnnualRank?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTermScores(scores: List<Score>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveYearScore(annualRank: AnnualRank)

    @Query("SELECT COUNT(*) FROM Score")
    fun numberOfItemsTermScores(): Int

    @Query("SELECT COUNT(id) FROM AnnualRank")
    fun numberOfItemsYearScore(): Int
}