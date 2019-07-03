package ga.forntoh.bableschool.data.model.other

import com.dbflow5.annotation.Column
import com.dbflow5.annotation.PrimaryKey
import com.dbflow5.annotation.Table
import ga.forntoh.bableschool.data.db.AppDatabase

@Table(database = AppDatabase::class)
data class TopSchool(
        @PrimaryKey var schoolName: String? = "",
        @PrimaryKey var topStudentName: String = "",
        @Column var image: String? = "",
        @Column var average: Double = 0.toDouble()
)