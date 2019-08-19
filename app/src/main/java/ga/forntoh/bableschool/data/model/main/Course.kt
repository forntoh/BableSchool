package ga.forntoh.bableschool.data.model.main

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ga.forntoh.bableschool.data.model.groupie.ItemCourse

@Entity
data class Course(
        @PrimaryKey var code: String = "",
        @ColumnInfo var title: String? = null
) {

    val abbr: String
        get() {
            val words = this.title!!.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            var title = ""
            return if (words.size > 1) {
                for (s in words) title += s[0]; title
            } else this.title!!.substring(0, 3)
        }
}

fun Course.toCourseView(videosSize: Int, documentsSize: Int) = ItemCourse(code, title, videosSize, documentsSize, abbr)