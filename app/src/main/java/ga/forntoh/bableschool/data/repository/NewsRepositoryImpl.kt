package ga.forntoh.bableschool.data.repository

import androidx.lifecycle.LiveData
import ga.forntoh.bableschool.data.AppStorage
import ga.forntoh.bableschool.data.db.NewsDao
import ga.forntoh.bableschool.data.model.main.Comment
import ga.forntoh.bableschool.data.model.main.News
import ga.forntoh.bableschool.data.network.BableSchoolDataSource
import ga.forntoh.bableschool.internal.DataKey
import ga.forntoh.bableschool.utilities.isFetchNeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class NewsRepositoryImpl(
        private val newsDao: NewsDao,
        private val bableSchoolDataSource: BableSchoolDataSource,
        private val appStorage: AppStorage
) : NewsRepository {

    init {
        bableSchoolDataSource.downloadedNews.observeForever {
            saveNews(it)
        }
        bableSchoolDataSource.downloadedComment.observeForever {
            newsDao.saveComment(it)
        }
    }

    // Main
    override suspend fun retrieveAllNews(): MutableList<News> {
        return withContext(Dispatchers.IO) {
            initNewsData()
            return@withContext newsDao.retrieveAllNews()
        }
    }

    // Main
    override suspend fun retrieveSingleNews(id: Long) = withContext(Dispatchers.IO) {
        return@withContext newsDao.retrieveSingleNews(id)
    }

    // Main
    override suspend fun retrieveComments(id: Long): LiveData<MutableList<Comment>> = withContext(Dispatchers.IO) {
        return@withContext newsDao.retrieveComments(id)
    }

    // Main
    override suspend fun postComment(comment: Comment) {
        bableSchoolDataSource.postComment(comment.apply { comment.sender = appStorage.loadUser()?.profileData?.matriculation })
    }

    // Main
    override suspend fun likeNews(id: Long, liked: Boolean) {
        appStorage.loadUser()?.profileData?.matriculation?.let { bableSchoolDataSource.likeNews(it, id) }
    }

    override fun observableLikes() = bableSchoolDataSource.downloadedLikes

    private suspend fun initNewsData() {
        if (isFetchNeeded(appStorage.getLastSaved(DataKey.NEWS))) {
            bableSchoolDataSource.getNews(appStorage.loadUser()?.profileData?.matriculation
                    ?: return)
            appStorage.setLastSaved(DataKey.NEWS, ZonedDateTime.now())
            delay(500)
        }
    }

    private fun saveNews(news: List<News>) = newsDao.saveNews(news)
}