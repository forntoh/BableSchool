package ga.forntoh.bableschool.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.text.DecimalFormat;
import java.util.Calendar;

import ga.forntoh.bableschool.R;

public class Utils {

    public static final String TAG = "BS_LOG";

    public static String formatScore(double d) {
        if (d < 0) return "N/A";
        return new DecimalFormat("##.##").format(d);
    }

    public static void setTabWidthAsWrapContent(TabLayout tabLayout, int tabPosition) {
        LinearLayout layout = (LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(tabPosition);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layoutParams.weight = 0f;
        layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        layout.setLayoutParams(layoutParams);
    }

    public static PopupWindow startPopUpWindow(View layout, View root, View.OnTouchListener onTouch) {
        PopupWindow popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);
        popupWindow.showAtLocation(root, Gravity.CENTER, 0, 0);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(onTouch);
        popupWindow.setAnimationStyle(R.style.PopUpAnimation);
        popupWindow.setOnDismissListener(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                ((Activity) layout.getContext()).getWindow().setStatusBarColor(root.getContext().getResources().getColor(R.color.bgLightGrey));
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            ((Activity) layout.getContext()).getWindow().setStatusBarColor(ContextCompat.getColor(root.getContext(), android.R.color.black));
        return popupWindow;
    }

    public static float dpToPixels(Context c, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, c.getResources().getDisplayMetrics());
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static String getKey(Object o, String val) {
        return o.getClass().getSimpleName() + "-" + val;
    }


    public static String getKey(Object o, String val, long i) {
        return o.getClass().getSimpleName() + "-" + val + "-" + i;
    }

    public static String getTermYear() {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.MONTH) >= Calendar.SEPTEMBER && calendar.get(Calendar.MONTH) <= Calendar.DECEMBER)
            return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.YEAR) + 1);
        else return (calendar.get(Calendar.YEAR) - 1) + "-" + (calendar.get(Calendar.YEAR));
    }

    public static String capEachWord(String sentence) {
        String[] words = sentence.split(" ");
        StringBuilder inCaps = new StringBuilder();
        for (String word : words)
            inCaps.append(String.valueOf(word.charAt(0)).toUpperCase()).append(word.substring(1, word.length()).toLowerCase()).append(" ");
        return inCaps.toString();
    }
}
