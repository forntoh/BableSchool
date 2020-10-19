package ga.forntoh.bableschool.utilities

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.format.DateUtils
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.ProgressBar
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.google.android.material.tabs.TabLayout
import com.tripl3dev.prettystates.StatesConstants
import com.tripl3dev.prettystates.setState
import com.xwray.groupie.Section
import ga.forntoh.bableschool.R
import org.threeten.bp.ZonedDateTime
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    var currentPopupWindow: PopupWindow? = null

    val termYear: String
        get() = with(Calendar.getInstance()) {
            return if (get(Calendar.MONTH) >= Calendar.SEPTEMBER && get(Calendar.MONTH) <= Calendar.DECEMBER) get(Calendar.YEAR).toString() + "-" + (get(Calendar.YEAR) + 1)
            else (get(Calendar.YEAR) - 1).toString() + "-" + get(Calendar.YEAR)
        }

    fun getRelativeTimeSpanString(date: String?, simpleDateFormat: SimpleDateFormat): String =
            with(simpleDateFormat) {
                if (date == null) return@with ""
                isLenient = false
                return try {
                    val oldDate = parse(date) as Date
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

    fun getTime(value: String, dayOfWeek: Int): Calendar {
        return Calendar.getInstance().apply {
            val hour = value.substring(0, 2).toInt()
            val minute = value.substring(3, 5).toInt()
            set(Calendar.HOUR_OF_DAY, hour);set(Calendar.MINUTE, minute);set(Calendar.DAY_OF_WEEK, dayOfWeek + 1)
        }
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

fun Context.getLoadingDialog() = MaterialDialog(this).show {
    customView(view = ProgressBar(context).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            indeterminateDrawable.colorFilter = BlendModeColorFilter(Color.WHITE, BlendMode.MULTIPLY)
        } else indeterminateDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY)
    })
    cancelOnTouchOutside(false)
    this.view.background = ColorDrawable(Color.TRANSPARENT)
}
