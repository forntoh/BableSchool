package ga.forntoh.bableschool.data.model.other

import com.dbflow5.annotation.Column
import com.dbflow5.annotation.PrimaryKey
import com.dbflow5.annotation.Table
import ga.forntoh.bableschool.data.db.AppDatabase

@Table(database = AppDatabase::class)
data class TopStudent(
        @PrimaryKey var name: String = "",
        @PrimaryKey var surname: String? = "",
        @Column var image: String? = "",
        @Column var school: String? = "",
        @Column var average: Double = 0.toDouble()
)