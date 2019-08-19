package ga.forntoh.bableschool.data.model.other

import ga.forntoh.bableschool.data.model.main.Document
import ga.forntoh.bableschool.data.model.main.Video

data class CourseResponse(
        var code: String = "",
        var title: String? = null,
        var vids: List<Video> = emptyList(),
        var docs: List<Document> = emptyList()
)