package ga.forntoh.bableschool.data.model.other

import com.dbflow5.annotation.Column
import com.dbflow5.annotation.PrimaryKey
import com.dbflow5.annotation.Table
import ga.forntoh.bableschool.data.db.AppDatabase

@Table(database = AppDatabase::class)
data class AnnualRank(
        @PrimaryKey var id: Int = 0,
        @Column var average: String? = null,
        @Column var position: String? = null
)

