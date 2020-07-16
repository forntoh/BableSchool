@file:Suppress("DeferredResultUnused")

package ga.forntoh.bableschool.ui.category.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ga.forntoh.bableschool.data.model.main.Comment
import ga.forntoh.bableschool.data.repository.NewsRepository
import ga.forntoh.bableschool.internal.lazyDeferred

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    init {
        newsRepository.scope = viewModelScope
    }

    var id: Long = 0

    suspend fun getAllNews() = newsRepository.retrieveAllNews()

    val singleNews by lazyDeferred {
        newsRepository.retrieveSingleNews(id)
    }

    val comments by lazyDeferred {
        newsRepository.retrieveComments(id)
    }

    val likes = newsRepository.observableLikes()

    suspend fun postComment(comment: Comment) =
            newsRepository.postComment(comment)

    suspend fun likeNews(id: Long, liked: Boolean) =
            newsRepository.likeNews(id, liked)

    fun resetState() = newsRepository.resetState()
}