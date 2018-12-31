package ga.forntoh.bableschool

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.TextView
import com.alamkanak.weekview.MonthLoader
import com.alamkanak.weekview.WeekViewEvent
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.list
import com.raizlabs.android.dbflow.kotlinextensions.save
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.sql.language.Delete
import ga.forntoh.bableschool.model.Period
import ga.forntoh.bableschool.utils.Utils
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_time_table.*

class TimeTableActivity : BaseActivity() {

    private val mMonthChangeListener = MonthLoader.MonthChangeListener { newYear, newMonth ->
        var qqc: ArrayList<WeekViewEvent> = ArrayList()

        val service = RetrofitBuilder.createService(ApiService::class.java)
        if (Utils.isConnected(this))
            service.getTimetable(StorageUtil.getInstance(this.baseContext).loadClass())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ periods ->
                        Delete.table(Period::class.java)
                        periods.forEach { it.save() }

                        qqc = getWeekViewEventList(periods, newMonth, newYear)
                        weekView.post { weekView.notifyDatasetChanged() }
                    }, {
                        qqc = getWeekViewEventList((select from Period::class).list, newMonth, newYear)
                        weekView.post { weekView.notifyDatasetChanged() }
                    })
        else return@MonthChangeListener getWeekViewEventList((select from Period::class).list, newMonth, newYear)

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
            eventMarginVertical = Utils.dpToPixels(context, 2f).toInt()
            eventCornerRadius = Utils.dpToPixels(context, 4f).toInt()
            eventPadding = Utils.dpToPixels(context, 6f).toInt()
            hourHeight = Utils.dpToPixels(context, 72f).toInt()
            monthChangeListener = mMonthChangeListener
            goToToday()
            goToHour(08.0)
        }
    }
}
