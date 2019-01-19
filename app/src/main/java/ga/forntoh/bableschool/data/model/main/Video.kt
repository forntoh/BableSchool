package ga.forntoh.bableschool.data.model.main

import com.raizlabs.android.dbflow.annotation.*
import ga.forntoh.bableschool.data.db.AppDatabase

@Table(database = AppDatabase::class)
data class Video(
        @PrimaryKey(autoincrement = true) var id: Long = 0,
        @ForeignKey(tableClass = Course::class, references = [ForeignKeyReference(columnName = "courseCode", foreignKeyColumnName = "code")]) var courseCode: String? = null,
        @Column var title: String? = null,
        @Column var author: String? = null,
        @Column var duration: String? = null,
        @Column var url: String? = null,
        @Column var thumbnail: String? = null
)