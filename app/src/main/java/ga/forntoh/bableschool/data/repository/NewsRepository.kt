package ga.forntoh.bableschool.data.repository

import androidx.lifecycle.LiveData
import ga.forntoh.bableschool.data.model.main.Comment
import ga.forntoh.bableschool.data.model.main.News
import ga.forntoh.bableschool.data.model.other.Likes

interface NewsRepository {

    suspend fun retrieveAllNews(): MutableList<News>

    suspend fun retrieveSingleNews(id: Long): News?

    suspend fun retrieveComments(id: Long): LiveData<MutableList<Comment>>

    suspend fun postComment(comment: Comment)

    suspend fun likeNews(id: Long, liked: Boolean)

    fun observableLikes(): LiveData<Likes>
}