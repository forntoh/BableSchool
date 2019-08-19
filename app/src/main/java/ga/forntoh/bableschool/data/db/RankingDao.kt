package ga.forntoh.bableschool.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ga.forntoh.bableschool.data.model.other.TopSchool
import ga.forntoh.bableschool.data.model.other.TopStudent

@Dao
interface RankingDao {

    @Query("SELECT * FROM TopSchool")
    fun retrieveTopSchools(): LiveData<MutableList<TopSchool>>

    @Query("SELECT * FROM TopStudent")
    fun retrieveTopStudents(): LiveData<MutableList<TopStudent>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTopSchools(vararg topSchools: TopSchool)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTopStudents(vararg topStudents: TopStudent)

    @Query("SELECT COUNT(*) FROM TopSchool")
    fun numberOfItemsTopSchools(): Int

    @Query("SELECT COUNT(*) FROM TopStudent")
    fun numberOfItemsTopStudents(): Int
}