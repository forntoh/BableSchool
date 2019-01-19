package ga.forntoh.bableschool.data.model.other

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import ga.forntoh.bableschool.data.db.AppDatabase

@Table(database = AppDatabase::class)
data class AnnualRank(
        @PrimaryKey var id: Int = 0,
        @Column var average: String? = null,
        @Column var position: String? = null
)

