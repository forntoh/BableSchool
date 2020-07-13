package ga.forntoh.bableschool.ui.category.profile.timeTable

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alamkanak.weekview.MonthLoader
import com.alamkanak.weekview.WeekViewEvent
import com.tripl3dev.prettystates.StatesConstants
import com.tripl3dev.prettystates.setState
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.model.main.toWeekViewEvent
import ga.forntoh.bableschool.ui.base.BaseActivity
import ga.forntoh.bableschool.utilities.enableWhiteStatusBar
import ga.forntoh.bableschool.utilities.inPx
import kotlinx.android.synthetic.main.activity_time_table.*
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance
import java.util.*
import kotlin.collections.ArrayList

class TimeTableActivity : BaseActivity(), MonthLoader.MonthChangeListener {

    private val viewModelFactory: TimeTableViewModelFactory by instance()
    private lateinit var viewModel: TimeTableViewModel

    private var calledNetwork = false

    private val events = ArrayList<WeekViewEvent>()

    override fun onMonthChange(newYear: Int, newMonth: Int): MutableList<out WeekViewEvent>? {
        if (!calledNetwork) {
            events.sortBy { weekViewEvent -> weekViewEvent.startTime }
            calledNetwork = true
        }
        // Return only the events that matches newYear and newMonth.
        val matchedEvents = ArrayList<WeekViewEvent>()
        for (event in events)
            if (eventMatches(event, newYear, newMonth))
                matchedEvents.add(event)
        return matchedEvents
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_table)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TimeTableViewModel::class.java)
        buildUI()
    }

    private fun buildUI() = launch {
        findViewById<TextView>(R.id.title).text = getString(R.string.my_timetable)

        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        disableFlags(true)
        enableWhiteStatusBar()

        weekView.apply {
            eventCornerRadius = 4.inPx.toFloat()
            eventPadding = 6.inPx
            columnGap = 4.inPx
            hourHeight = 72.inPx
            monthChangeListener = this@TimeTableActivity
            goToToday()
            goToHour(08.0)
            setState(StatesConstants.LOADING_STATE)
        }


        viewModel.periods.await().observe(this@TimeTableActivity, Observer { list ->
            if (!list.isNullOrEmpty()) {
                list.forEach { events.add(it.toWeekViewEvent()) }
                weekView.apply {
                    setState(StatesConstants.NORMAL_STATE)
                    maxDate = events.last().endTime
                    notifyDataSetChanged()
                    goToHour((events.first().startTime.get(Calendar.HOUR_OF_DAY)).toDouble())
                }
            } else weekView.setState(StatesConstants.EMPTY_STATE)
        })
    }

    private fun eventMatches(event: WeekViewEvent, year: Int, month: Int): Boolean {
        return event.startTime.get(Calendar.YEAR) == year && event.startTime.get(Calendar.MONTH) == month - 1 || event.endTime.get(Calendar.YEAR) == year && event.endTime.get(Calendar.MONTH) == month - 1
    }
}