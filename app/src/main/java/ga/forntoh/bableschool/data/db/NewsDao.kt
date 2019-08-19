package ga.forntoh.bableschool.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ga.forntoh.bableschool.data.model.main.Comment
import ga.forntoh.bableschool.data.model.main.News

@Dao
interface NewsDao {

    @Query("SELECT * FROM News")
    fun retrieveAllNews(): LiveData<MutableList<News>>

    @Query("SELECT * FROM News WHERE News.id LIKE :id LIMIT 1")
    fun retrieveSingleNews(id: Long): News?

    @Query("SELECT * FROM Comment WHERE Comment.newsId LIKE :id")
    fun retrieveComments(id: Long): LiveData<MutableList<Comment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNews(vararg news: News)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveComments(vararg comment: Comment)

    @Query("UPDATE News SET liked = :liked WHERE id = :id")
    suspend fun saveLike(id: Long, liked: Boolean)

    @Query("SELECT COUNT(id) FROM News")
    fun numberOfItems(): Int
}