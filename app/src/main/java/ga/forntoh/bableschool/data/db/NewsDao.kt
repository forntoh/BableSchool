package ga.forntoh.bableschool.data.db

import androidx.lifecycle.LiveData
import ga.forntoh.bableschool.data.model.main.Comment
import ga.forntoh.bableschool.data.model.main.News

interface NewsDao {

    fun retrieveAllNews(): MutableList<News>

    fun retrieveSingleNews(id: Long): News?

    fun retrieveComments(id: Long): LiveData<MutableList<Comment>>

    fun saveNews(news: List<News>)

    fun saveComment(comment: Comment)

    fun saveLike(id: Long, liked: Boolean)
}