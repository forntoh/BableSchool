package ga.forntoh.bableschool.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ga.forntoh.bableschool.data.model.main.Score
import ga.forntoh.bableschool.data.model.main.ScoreCoursePair
import ga.forntoh.bableschool.data.model.other.AnnualRank

@Dao
interface ScoreDao {

    @Query("SELECT Course.code as Course_code, Course.title as Course_title, Score.* FROM Course LEFT OUTER JOIN Score ON Score.course_code = Course.code WHERE term like :term")
    fun retrieveTermScores(term: Int): LiveData<MutableList<ScoreCoursePair>>

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

    @Query("SELECT Course.code as Course_code, Course.title as Course_title, Score.* FROM Course LEFT OUTER JOIN Score ON Score.course_code = Course.code WHERE term like :term")
    fun retrieveTermScoresAsync(term: Int): MutableList<ScoreCoursePair>
}