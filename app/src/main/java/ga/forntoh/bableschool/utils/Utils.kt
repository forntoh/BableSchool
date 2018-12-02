package ga.forntoh.bableschool.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.text.format.DateUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.model.Period
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    val screenWidth: Int get() = Resources.getSystem().displayMetrics.widthPixels

    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnected ?: false
    }

    fun formatScore(d: Double): String = if (d < 0) "N/A" else DecimalFormat("##.##").format(d)

    fun dpToPixels(c: Context, value: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, c.resources.displayMetrics)

    val termYear: String
        get() = with(Calendar.getInstance()) {
            return if (get(Calendar.MONTH) >= Calendar.SEPTEMBER && get(Calendar.MONTH) <= Calendar.DECEMBER) get(Calendar.YEAR).toString() + "-" + (get(Calendar.YEAR) + 1)
            else (get(Calendar.YEAR) - 1).toString() + "-" + get(Calendar.YEAR)
        }

    fun setTabWidthAsWrapContent(tabLayout: TabLayout, tabPosition: Int) {
        val layout = (tabLayout.getChildAt(0) as LinearLayout).getChildAt(tabPosition) as LinearLayout
        val layoutParams = layout.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 0f
        layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        layout.layoutParams = layoutParams
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
        }
    }

    fun capEachWord(sentence: String): String {
        val words = sentence.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val inCaps = StringBuilder()
        for (word in words) inCaps.append(word[0].toString().toUpperCase()).append(word.substring(1, word.length).toLowerCase()).append(" ")
        return inCaps.toString()
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

    val dummyPeriods: ArrayList<Period>
        get() {
            val periods = ArrayList<Period>()

            periods.add(Period(0, "08:00", "10:00", "Electrocinétique", Calendar.MONDAY, "#59dbe0"))
            periods.add(Period(0, "12:00", "12:30", "Break", Calendar.MONDAY, "#3c3c3c"))
            periods.add(Period(0, "12:30", "14:30", "Algo et structure données", Calendar.MONDAY, "#f57f68"))
            periods.add(Period(0, "14:30", "16:30", "Electrostatique & Electromag", Calendar.MONDAY, "#87d288"))

            periods.add(Period(0, "08:00", "10:00", "Electrocinétique", Calendar.TUESDAY, "#59dbe0"))
            periods.add(Period(0, "10:00", "12:00", "Algèbre linéaire I", Calendar.TUESDAY, "#f8b552"))
            periods.add(Period(0, "12:00", "12:30", "Break", Calendar.TUESDAY, "#3c3c3c"))
            periods.add(Period(0, "12:30", "14:30", "Architecture ordinateurs et SE", Calendar.TUESDAY, "#FF4081"))
            periods.add(Period(0, "14:30", "16:30", "Electrostatique & Electromag", Calendar.TUESDAY, "#87d288"))

            periods.add(Period(0, "08:00", "10:00", "Analyse I", Calendar.WEDNESDAY, "#66A6FF"))
            periods.add(Period(0, "10:00", "12:00", "Optique", Calendar.WEDNESDAY, "#078B75"))
            periods.add(Period(0, "12:00", "12:30", "Break", Calendar.WEDNESDAY, "#3c3c3c"))
            periods.add(Period(0, "12:30", "16:30", "SEMINAIRE-ATELIER (HUAWEI) : Formation des talents aux TICS ", Calendar.WEDNESDAY, "#9C27B0"))

            periods.add(Period(0, "08:00", "10:00", "Architecture ordinateurs et SE", Calendar.THURSDAY, "#FF4081"))
            periods.add(Period(0, "10:00", "12:00", "Algèbre linéaire I", Calendar.THURSDAY, "#f8b552"))
            periods.add(Period(0, "12:00", "12:30", "Break", Calendar.THURSDAY, "#3c3c3c"))
            periods.add(Period(0, "12:30", "14:30", "Langues", Calendar.THURSDAY, "#f57f68"))

            periods.add(Period(0, "08:00", "10:00", "Optique", Calendar.FRIDAY, "#078B75"))
            periods.add(Period(0, "10:00", "12:00", "Algo et structure données", Calendar.FRIDAY, "#f57f68"))
            periods.add(Period(0, "12:00", "12:30", "Break", Calendar.FRIDAY, "#3c3c3c"))
            periods.add(Period(0, "12:30", "14:30", "Algo et structure données", Calendar.FRIDAY, "#f57f68"))
            periods.add(Period(0, "14:30", "16:30", "Sport", Calendar.FRIDAY, "#FF4081"))

            return periods
        }
}
