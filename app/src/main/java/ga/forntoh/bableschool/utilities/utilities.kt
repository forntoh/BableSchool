package ga.forntoh.bableschool.utilities

import android.app.Activity
import android.content.res.Resources
import android.os.Build
import android.text.format.DateUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.tripl3dev.prettystates.StatesConstants
import com.tripl3dev.prettystates.setState
import com.xwray.groupie.Section
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.model.main.Period
import org.threeten.bp.ZonedDateTime
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object Utils {

    var currentPopupWindow: PopupWindow? = null

    val termYear: String
        get() = with(Calendar.getInstance()) {
            return if (get(Calendar.MONTH) >= Calendar.SEPTEMBER && get(Calendar.MONTH) <= Calendar.DECEMBER) get(Calendar.YEAR).toString() + "-" + (get(Calendar.YEAR) + 1)
            else (get(Calendar.YEAR) - 1).toString() + "-" + get(Calendar.YEAR)
        }

    fun startPopUpWindow(layout: View, root: View, onTouch: View.OnTouchListener?): PopupWindow {
        return PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false).apply {
            showAtLocation(root, Gravity.CENTER, 0, 0)
            isTouchable = true
            setTouchInterceptor(onTouch)
            animationStyle = R.style.PopUpAnimation
            setOnDismissListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) (layout.context as Activity).window.statusBarColor = root.context.resources.getColor(R.color.bgLightGrey)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                (layout.context as Activity).window.statusBarColor = ContextCompat.getColor(root.context, android.R.color.black)
        }.also { currentPopupWindow = it }
    }

    fun getRelativeTimeSpanString(date: String?, simpleDateFormat: SimpleDateFormat): String =
            with(simpleDateFormat) {
                isLenient = false
                return try {
                    val oldDate = parse(date)
                    DateUtils.getRelativeTimeSpanString(oldDate.time, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS) as String
                } catch (ignored: ParseException) {
                    ""
                }
            }

    fun getLongDate(date: String?): Long {
        with(SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())) {
            isLenient = false
            return try {
                val oldDate = parse(date)
                oldDate.time
            } catch (ignored: ParseException) {
                -1
            }
        }
    }

    fun getTime(value: String, newMonth: Int, newYear: Int, dayOfWeek: Int): Calendar {
        return Calendar.getInstance().apply {
            val hour = value.substring(0, 2).toInt()
            val minute = value.substring(3, 5).toInt()
            set(Calendar.HOUR_OF_DAY, hour);set(Calendar.MINUTE, minute);set(Calendar.MONTH, newMonth - 1);set(Calendar.YEAR, newYear);set(Calendar.DAY_OF_WEEK, dayOfWeek)
        }
    }

    val dummyPeriods: List<Period>
        get() {
            val periods = ArrayList<Period>()

            periods.add(Period("08:00", "10:00", "Electrocinétique", Calendar.MONDAY, "#59dbe0"))
            periods.add(Period("12:00", "12:30", "Break", Calendar.MONDAY, "#3c3c3c"))
            periods.add(Period("12:30", "14:30", "Algo et structure données", Calendar.MONDAY, "#f57f68"))
            periods.add(Period("14:30", "16:30", "Electrostatique & Electromag", Calendar.MONDAY, "#87d288"))

            periods.add(Period("08:00", "10:00", "Electrocinétique", Calendar.TUESDAY, "#59dbe0"))
            periods.add(Period("10:00", "12:00", "Algèbre linéaire I", Calendar.TUESDAY, "#f8b552"))
            periods.add(Period("12:00", "12:30", "Break", Calendar.TUESDAY, "#3c3c3c"))
            periods.add(Period("12:30", "14:30", "Architecture ordinateurs et SE", Calendar.TUESDAY, "#FF4081"))
            periods.add(Period("14:30", "16:30", "Electrostatique & Electromag", Calendar.TUESDAY, "#87d288"))

            periods.add(Period("08:00", "10:00", "Analyse I", Calendar.WEDNESDAY, "#66A6FF"))
            periods.add(Period("10:00", "12:00", "Optique", Calendar.WEDNESDAY, "#078B75"))
            periods.add(Period("12:00", "12:30", "Break", Calendar.WEDNESDAY, "#3c3c3c"))
            periods.add(Period("12:30", "16:30", "SEMINAIRE-ATELIER (HUAWEI) : Formation des talents aux TICS ", Calendar.WEDNESDAY, "#9C27B0"))

            periods.add(Period("08:00", "10:00", "Architecture ordinateurs et SE", Calendar.THURSDAY, "#FF4081"))
            periods.add(Period("10:00", "12:00", "Algèbre linéaire I", Calendar.THURSDAY, "#f8b552"))
            periods.add(Period("12:00", "12:30", "Break", Calendar.THURSDAY, "#3c3c3c"))
            periods.add(Period("12:30", "14:30", "Langues", Calendar.THURSDAY, "#f57f68"))

            periods.add(Period("08:00", "10:00", "Optique", Calendar.FRIDAY, "#078B75"))
            periods.add(Period("10:00", "12:00", "Algo et structure données", Calendar.FRIDAY, "#f57f68"))
            periods.add(Period("12:00", "12:30", "Break", Calendar.FRIDAY, "#3c3c3c"))
            periods.add(Period("12:30", "14:30", "Algo et structure données", Calendar.FRIDAY, "#f57f68"))
            periods.add(Period("14:30", "16:30", "Sport", Calendar.FRIDAY, "#FF4081"))

            return periods
        }
}

val Double.in2Dp: String get() = if (this < 0) "N/A" else DecimalFormat("##.##").format(this)

val Int.inPx: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Float.inPx: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val screenWidth: Int get() = Resources.getSystem().displayMetrics.widthPixels

val String.capitalizeEachWord: String
    get() {
        val words = this.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val inCaps = StringBuilder()
        for (word in words) inCaps.append(word[0].toString().toUpperCase()).append(word.substring(1, word.length).toLowerCase()).append(" ")
        return inCaps.toString()
    }

fun TabLayout.setTabWidthAsWrapContent(tabPosition: Int) {
    val layout = (this.getChildAt(0) as LinearLayout).getChildAt(tabPosition) as LinearLayout
    val layoutParams = layout.layoutParams as LinearLayout.LayoutParams
    layoutParams.weight = 0f
    layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
    layout.layoutParams = layoutParams
}

fun Activity.enableWhiteStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ResourcesCompat.getColor(resources, R.color.bgLightGrey, null)
    }
}

fun View.clipAllParents() {
    var view = this
    while (view.parent != null && view.parent is ViewGroup) {
        val viewGroup = view.parent as ViewGroup
        viewGroup.clipToPadding = false
        viewGroup.clipChildren = false
        view = viewGroup
    }
}

fun isFetchNeeded(lastFetchedTime: ZonedDateTime, minutes: Long = 5): Boolean {
    val fiveMinutesAgo = ZonedDateTime.now().minusMinutes(minutes)
    return lastFetchedTime.isBefore(fiveMinutesAgo)
}

fun RecyclerView.toggleViewState(section: Section) {
    if (section.itemCount < 1) this.setState(StatesConstants.EMPTY_STATE)
    else this.setState(StatesConstants.NORMAL_STATE)
}

fun RecyclerView.invalidateViewState() {
    this.setState(StatesConstants.NORMAL_STATE)
    this.setState(StatesConstants.LOADING_STATE)
}
