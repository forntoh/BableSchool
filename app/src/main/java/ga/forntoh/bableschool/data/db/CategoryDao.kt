package ga.forntoh.bableschool.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ga.forntoh.bableschool.data.model.main.Category

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCategories(vararg categories: Category)

    @Query("SELECT * FROM Category")
    fun retrieveCategories(): LiveData<MutableList<Category>>

    @Query("SELECT COUNT(*) FROM Category")
    fun numberOfItems(): Int
}