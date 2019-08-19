package ga.forntoh.bableschool.ui.category.profile.timeTable

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alamkanak.weekview.MonthLoader
import com.alamkanak.weekview.WeekViewEvent
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.model.main.toWeekViewEvent
import ga.forntoh.bableschool.ui.base.BaseActivity
import ga.forntoh.bableschool.utilities.enableWhiteStatusBar
import ga.forntoh.bableschool.utilities.inPx
import kotlinx.android.synthetic.main.activity_time_table.*
import kotlinx.coroutines.runBlocking
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.util.*
import kotlin.collections.ArrayList

class TimeTableActivity : BaseActivity(), KodeinAware, MonthLoader.MonthChangeListener {

    override val kodein by closestKodein()

    private val viewModelFactory: TimeTableViewModelFactory by instance()
    private lateinit var viewModel: TimeTableViewModel

    private var calledNetwork = false

    override fun onMonthChange(newYear: Int, newMonth: Int): MutableList<out WeekViewEvent>? {
        val events = ArrayList<WeekViewEvent>()
        if (!calledNetwork) {
            runBlocking {

                viewModel.periods.await().observe(this@TimeTableActivity, Observer { list ->
                    list?.forEach { events.add(it.toWeekViewEvent(newYear, newMonth)) }
                    weekView.notifyDataSetChanged()
                })

                events.sortBy { weekViewEvent -> weekViewEvent.startTime }
                if (events.isNotEmpty())
                    weekView.goToHour((events[0].startTime.get(Calendar.HOUR_OF_DAY) + events[0].startTime.get(Calendar.MINUTE) / 60f).toDouble())
            }
            calledNetwork = true
        }
        return events
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_table)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TimeTableViewModel::class.java)
        buildUI()
    }

    private fun buildUI() {
        findViewById<TextView>(R.id.title).text = getString(R.string.my_timetable)

        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        disableFlags(true)
        enableWhiteStatusBar()

        weekView.apply {
            //eventCornerRadius = 4.inPx.toFloat()
            //eventPadding = 6.inPx
            //columnGap = 4.inPx
            hourHeight = 78.inPx
            monthChangeListener = this@TimeTableActivity
            goToToday()
            goToHour(08.0)
        }
    }
}