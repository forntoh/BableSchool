package ga.forntoh.bableschool.data.model.main

import android.graphics.Color
import com.alamkanak.weekview.WeekViewEvent
import com.dbflow5.annotation.Column
import com.dbflow5.annotation.PrimaryKey
import com.dbflow5.annotation.Table
import ga.forntoh.bableschool.data.db.AppDatabase
import ga.forntoh.bableschool.utilities.Utils

@Table(database = AppDatabase::class)
data class Period(
        @PrimaryKey var start: String = "",
        @PrimaryKey var end: String = "",
        @PrimaryKey var course: String? = "",
        @Column var dayOfWeek: Int = 0,
        @Column var color: String = "#000000"
)

fun Period.toWeekViewEvent(newYear: Int, newMonth: Int) = WeekViewEvent(
        course,
        course,
        Utils.getTime(start, newMonth, newYear, dayOfWeek),
        Utils.getTime(end, newMonth, newYear, dayOfWeek)
).apply {
    location = String.format("- %s to %s", start, end)
    color = Color.parseColor(this@toWeekViewEvent.color)
}