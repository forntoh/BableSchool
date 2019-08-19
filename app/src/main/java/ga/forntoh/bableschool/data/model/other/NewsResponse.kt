package ga.forntoh.bableschool.data.model.other

import ga.forntoh.bableschool.data.model.main.Comment
import ga.forntoh.bableschool.data.model.main.News

data class NewsResponse(
        var id: Long = 0,
        var title: String? = null,
        var liked: Boolean = false,
        var author: String? = null,
        var description: String? = null,
        var date: String? = null,
        var thumbnail: String? = null,
        var category: String? = null,
        var likes: Int = 0,
        var isTop: Boolean = false,
        var cmts: List<Comment> = emptyList()
)

fun NewsResponse.toNewsData() = News(id, title, liked, author, description, date, thumbnail, category, likes, isTop)