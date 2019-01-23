package ga.forntoh.bableschool.ui.category.profile.timeTable

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import com.alamkanak.weekview.MonthLoader
import com.alamkanak.weekview.WeekViewEvent
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.model.main.Period
import ga.forntoh.bableschool.ui.base.BaseActivity
import ga.forntoh.bableschool.utilities.Utils
import ga.forntoh.bableschool.utilities.enableWhiteStatusBar
import ga.forntoh.bableschool.utilities.inPx
import kotlinx.android.synthetic.main.activity_time_table.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class TimeTableActivity : BaseActivity(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: TimeTableViewModelFactory by instance()
    private lateinit var viewModel: TimeTableViewModel


    private val mMonthChangeListener = MonthLoader.MonthChangeListener { newYear, newMonth ->
        var qqc: ArrayList<WeekViewEvent> = ArrayList()
        launch {
            qqc = getWeekViewEventList(viewModel.periods.await(), newMonth, newYear)
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
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TimeTableViewModel::class.java)
        buildUI()
    }

    private fun buildUI() {
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
