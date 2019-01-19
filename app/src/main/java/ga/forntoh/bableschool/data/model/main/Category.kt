package ga.forntoh.bableschool.data.model.main

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import ga.forntoh.bableschool.data.db.AppDatabase

@Table(database = AppDatabase::class)
data class Category(
        @PrimaryKey var id: Long = 0,
        @Column var title: String? = null,
        @Column var thumbnail: String? = null,
        @Column var color: String? = null
)