package ga.forntoh.bableschool.data.model.main

import com.dbflow5.annotation.*
import ga.forntoh.bableschool.data.db.AppDatabase

@Table(database = AppDatabase::class)
data class Video(
        @ForeignKey(tableClass = Course::class, references = [ForeignKeyReference(columnName = "courseCode", foreignKeyColumnName = "code")]) var courseCode: String? = null,
        @Column var title: String? = null,
        @Column var author: String? = null,
        @Column var duration: String? = null,
        @PrimaryKey var url: String? = null,
        @Column var thumbnail: String? = null
)