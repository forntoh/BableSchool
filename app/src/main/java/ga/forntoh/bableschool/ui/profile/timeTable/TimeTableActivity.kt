package ga.forntoh.bableschool.ui.profile.timeTable

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.alamkanak.weekview.MonthLoader
import com.alamkanak.weekview.WeekViewEvent
import com.raizlabs.android.dbflow.kotlinextensions.save
import com.raizlabs.android.dbflow.sql.language.Delete
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.ApiService
import ga.forntoh.bableschool.data.model.Period
import ga.forntoh.bableschool.ui.BaseActivity
import ga.forntoh.bableschool.utils.StorageUtil
import ga.forntoh.bableschool.utils.Utils
import ga.forntoh.bableschool.utils.enableWhiteStatusBar
import ga.forntoh.bableschool.utils.inPx
import kotlinx.android.synthetic.main.activity_time_table.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class TimeTableActivity : BaseActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val mMonthChangeListener = MonthLoader.MonthChangeListener { newYear, newMonth ->
        var qqc: ArrayList<WeekViewEvent> = ArrayList()

        launch {
            val periods = ApiService().getTimetable(StorageUtil.getInstance(baseContext).loadClass()).await()
            Delete.table(Period::class.java)
            periods.forEach { it.save() }
            qqc = getWeekViewEventList(periods, newMonth, newYear)
            weekView.notifyDatasetChanged()
        }

        return@MonthChangeListener qqc
    }

    private fun getWeekViewEventList(periods: List<Period>, newMonth: Int, newYear: Int): ArrayList<WeekViewEvent> =
            ArrayList<WeekViewEvent>().apply {
                periods.forEach {
                    this.add(WeekViewEvent((periods.indexOf(it) + 1).toLong(), it.course, Utils.getTime(it.start, newMonth, newYear, it.dayOfWeek), Utils.getTime(it.end, newMonth, newYear, it.dayOfWeek)).apply {
                        location = String.format("%s to %s", it.start, it.end)
                        color = Color.parseColor(it.color)
                    })
                }
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_table)

        findViewById<TextView>(R.id.title).text = getString(R.string.my_timetable)

        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        disableFlags(true)
        enableWhiteStatusBar()

        weekView.apply {
            eventMarginVertical = 2.inPx
            eventCornerRadius = 4.inPx
            eventPadding = 6.inPx
            hourHeight = 72.inPx
            monthChangeListener = mMonthChangeListener
            goToToday()
            goToHour(08.0)
        }
    }
}
