package ga.forntoh.bableschool.data.model.main

import com.dbflow5.annotation.Column
import com.dbflow5.annotation.PrimaryKey
import com.dbflow5.annotation.Table
import ga.forntoh.bableschool.data.db.AppDatabase

@Table(database = AppDatabase::class)
data class Period(
        @PrimaryKey var start: String = "",
        @PrimaryKey var end: String = "",
        @PrimaryKey var course: String? = "",
        @Column var dayOfWeek: Int = 0,
        @Column var color: String = "#000000"
)