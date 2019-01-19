package ga.forntoh.bableschool.data.model.main

import com.raizlabs.android.dbflow.annotation.*
import ga.forntoh.bableschool.data.db.AppDatabase

@Table(database = AppDatabase::class)
data class Comment(
        @Column var sender: String? = null,
        @Column var message: String? = null,
        @Column var thumbnail: String? = null
) {
    @PrimaryKey
    var id: Long = 0
    @ForeignKey(tableClass = News::class, references = [ForeignKeyReference(columnName = "newsId", foreignKeyColumnName = "id")])
    var newsId: Long? = 0
    @Column
    var date: String? = null
}