package ga.forntoh.bableschool.data.model.main

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ga.forntoh.bableschool.data.model.groupie.ItemCategory

@Entity
data class Category(
        @PrimaryKey var id: Long = 0,
        @ColumnInfo var title: String? = null,
        @ColumnInfo var thumbnail: String? = null,
        @ColumnInfo var color: String? = null
)

fun Category.toCategoryView() = ItemCategory(id, title, thumbnail, color)