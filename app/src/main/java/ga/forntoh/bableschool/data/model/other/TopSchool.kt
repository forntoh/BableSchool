package ga.forntoh.bableschool.data.model.other

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["schoolName", "topStudentName"])
data class TopSchool(
        @ColumnInfo var schoolName: String = "",
        @ColumnInfo var topStudentName: String = "",
        @ColumnInfo var image: String? = "",
        @ColumnInfo var average: Double = 0.toDouble()
)