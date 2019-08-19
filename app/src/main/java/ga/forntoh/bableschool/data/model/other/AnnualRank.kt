package ga.forntoh.bableschool.data.model.other

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AnnualRank(
        @PrimaryKey var id: Int = 0,
        @ColumnInfo var average: String? = null,
        @ColumnInfo var position: String? = null
)

