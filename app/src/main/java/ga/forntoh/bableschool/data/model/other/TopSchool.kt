package ga.forntoh.bableschool.data.model.other

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import ga.forntoh.bableschool.data.db.AppDatabase

@Table(database = AppDatabase::class)
data class TopSchool(
        @PrimaryKey(autoincrement = true) var id: Long = 0,
        @Column var schoolName: String? = "",
        @Column var image: String? = "",
        @Column var topStudentName: String = "",
        @Column var average: Double = 0.toDouble()
)