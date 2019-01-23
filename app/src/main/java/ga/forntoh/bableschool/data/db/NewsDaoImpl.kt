package ga.forntoh.bableschool.data.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dbflow5.query.select
import com.dbflow5.query.set
import com.dbflow5.query.update
import com.dbflow5.structure.save
import ga.forntoh.bableschool.data.model.main.Comment
import ga.forntoh.bableschool.data.model.main.Comment_Table
import ga.forntoh.bableschool.data.model.main.News
import ga.forntoh.bableschool.data.model.main.News_Table

class NewsDaoImpl(private val database: AppDatabase) : NewsDao {

    private val comments = MutableLiveData<MutableList<Comment>>()

    override fun retrieveAllNews(): MutableList<News> =
            (select from News::class).queryList(database)

    override fun retrieveSingleNews(id: Long): News? =
            (select from News::class
                    where (News_Table.id eq id)).querySingle(database)

    override fun retrieveComments(id: Long): LiveData<MutableList<Comment>> {
        comments.postValue((select from Comment::class
                where (Comment_Table.newsId eq id)).queryList(database))
        return comments
    }

    override fun saveNews(news: List<News>) =
            news.forEach { it.save(database) }

    override fun saveComment(comment: Comment) {
        comment.save(database)
        comments.postValue(comments.value?.apply { add(comment) })
    }

    override fun saveLike(id: Long, liked: Boolean) {
        (update<News>()
                set (News_Table.liked eq liked)
                where (News_Table.id eq id)).execute(database)
    }
}