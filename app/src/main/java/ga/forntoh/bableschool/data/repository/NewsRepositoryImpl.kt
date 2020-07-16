package ga.forntoh.bableschool.data.repository

import androidx.lifecycle.LiveData
import ga.forntoh.bableschool.data.AppStorage
import ga.forntoh.bableschool.data.db.NewsDao
import ga.forntoh.bableschool.data.model.main.Comment
import ga.forntoh.bableschool.data.model.main.News
import ga.forntoh.bableschool.data.model.other.toNewsData
import ga.forntoh.bableschool.data.network.BableSchoolDataSource
import ga.forntoh.bableschool.internal.DataKey
import ga.forntoh.bableschool.utilities.isFetchNeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class NewsRepositoryImpl(
        private val newsDao: NewsDao,
        private val bableSchoolDataSource: BableSchoolDataSource,
        private val appStorage: AppStorage
) : NewsRepository() {

    init {
        bableSchoolDataSource.downloadedNews.observeForever { response ->
            scope.launch {
                newsDao.deleteAllNews()
                for (item in response) {
                    saveNews(item.toNewsData())
                    newsDao.saveComments(*item.cmts.map { cmt -> cmt.apply { newsId = item.id } }.toTypedArray())
                }
            }
        }
        bableSchoolDataSource.downloadedComment.observeForever {
            scope.launch { newsDao.saveComments(it) }
        }
    }

    override fun resetState() {
        appStorage.clearLastSaved(DataKey.NEWS)
    }

    // Main
    override suspend fun retrieveAllNews(): LiveData<MutableList<News>> {
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
        appStorage.loadUser()?.profileData?.matriculation?.let {
            newsDao.saveLike(id, liked)
            bableSchoolDataSource.likeNews(it, id)
        }
    }

    override fun observableLikes() = bableSchoolDataSource.downloadedLikes

    private suspend fun initNewsData() {
        if (isFetchNeeded(appStorage.getLastSaved(DataKey.NEWS), 1) || newsDao.numberOfItems() <= 0) {
            bableSchoolDataSource.getNews(appStorage.loadUser()?.profileData?.matriculation
                    ?: return)
            appStorage.setLastSaved(DataKey.NEWS, ZonedDateTime.now())
            delay(500)
        }
    }

    private suspend fun saveNews(vararg news: News) = newsDao.saveNews(*news)
}