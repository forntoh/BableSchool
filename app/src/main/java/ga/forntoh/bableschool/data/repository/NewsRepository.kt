package ga.forntoh.bableschool.data.repository

import androidx.lifecycle.LiveData
import ga.forntoh.bableschool.data.model.main.Comment
import ga.forntoh.bableschool.data.model.main.News
import ga.forntoh.bableschool.data.model.other.Likes

abstract class NewsRepository : BaseRepository() {

    abstract suspend fun retrieveAllNews(): LiveData<MutableList<News>>

    abstract suspend fun retrieveSingleNews(id: Long): News?

    abstract suspend fun retrieveComments(id: Long): LiveData<MutableList<Comment>>

    abstract suspend fun postComment(comment: Comment)

    abstract suspend fun likeNews(id: Long, liked: Boolean)

    abstract fun observableLikes(): LiveData<Likes>
    abstract fun resetState()
}