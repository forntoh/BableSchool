package ga.forntoh.bableschool.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ga.forntoh.bableschool.data.model.main.Period

@Dao
interface PeriodDao {

    @Query("SELECT * FROM Period")
    fun retrievePeriods(): LiveData<MutableList<Period>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePeriods(periods: List<Period>)

    @Query("SELECT COUNT(course) FROM Period")
    fun numberOfItems(): Int
}