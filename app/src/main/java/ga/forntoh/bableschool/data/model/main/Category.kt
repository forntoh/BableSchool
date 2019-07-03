package ga.forntoh.bableschool.data.model.main

import com.dbflow5.annotation.Column
import com.dbflow5.annotation.PrimaryKey
import com.dbflow5.annotation.Table
import ga.forntoh.bableschool.data.db.AppDatabase
import ga.forntoh.bableschool.data.model.groupie.ItemCategory

@Table(database = AppDatabase::class)
data class Category(
        @PrimaryKey var id: Long = 0,
        @Column var title: String? = null,
        @Column var thumbnail: String? = null,
        @Column var color: String? = null
)

fun Category.toCategoryView() = ItemCategory(id, title, thumbnail, color)