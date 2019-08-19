package ga.forntoh.bableschool.data.model.other

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["name", "surname"])
data class TopStudent(
        @ColumnInfo var name: String = "",
        @ColumnInfo var surname: String = "",
        @ColumnInfo var image: String? = "",
        @ColumnInfo var school: String? = "",
        @ColumnInfo var average: Double = 0.toDouble()
)