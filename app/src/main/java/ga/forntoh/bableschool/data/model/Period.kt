package ga.forntoh.bableschool.data.model

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import ga.forntoh.bableschool.data.db.AppDatabase

@Table(database = AppDatabase::class)
data class Period(
        @PrimaryKey(autoincrement = true) var id: Long = 0,
        @Column var start: String = "",
        @Column var end: String = "",
        @Column var course: String? = "",
        @Column var dayOfWeek: Int = 0,
        @Column var color: String = "#000000"
)