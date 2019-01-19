package ga.forntoh.bableschool.data.db

import ga.forntoh.bableschool.data.model.main.News
import kotlinx.coroutines.Deferred

interface NewsDao : DBProvider<AppDatabase> {

    fun saveNews(news: News) = news.save()

    fun retrieveNews(): Deferred<MutableList<News>>
}