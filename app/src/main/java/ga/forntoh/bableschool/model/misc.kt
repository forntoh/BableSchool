package ga.forntoh.bableschool.model

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import ga.forntoh.bableschool.AppDatabase

@Table(database = AppDatabase::class)
data class AnnualRank(
        @PrimaryKey var id: Int = 0,
        @Column var average: String? = null,
        @Column var position: String? = null
)

@Table(database = AppDatabase::class)
data class TopSchool(
        @PrimaryKey(autoincrement = true) var id: Long = 0,
        @Column var schoolName: String? = "",
        @Column var image: String? = "",
        @Column var topStudentName: String = "",
        @Column var average: Double = 0.toDouble()
)

@Table(database = AppDatabase::class)
data class TopStudent(
        @PrimaryKey var name: String = "",
        @PrimaryKey var surname: String? = "",
        @Column var image: String? = "",
        @Column var school: String? = "",
        @Column var average: Double = 0.toDouble()
)
