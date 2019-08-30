package ga.forntoh.bableschool.data.model.main

import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.alamkanak.weekview.WeekViewEvent
import ga.forntoh.bableschool.utilities.Utils

@Entity(primaryKeys = ["start", "end", "course"])
data class Period(
        @ColumnInfo var start: String = "",
        @ColumnInfo var end: String = "",
        @ColumnInfo var course: String = "",
        @ColumnInfo var dayOfWeek: Int = 0,
        @ColumnInfo var color: String = "#000000"
)

fun Period.toWeekViewEvent() = WeekViewEvent(
        course,
        course,
        Utils.getTime(start, dayOfWeek),
        Utils.getTime(end, dayOfWeek)
).apply {
    location = String.format("- %s to %s", start, end)
    color = Color.parseColor(this@toWeekViewEvent.color)
}