package ga.forntoh.bableschool;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ga.forntoh.bableschool.model.Period;
import ga.forntoh.bableschool.utils.Utils;

public class TimeTableActivity extends BaseActivity {

    private MonthLoader.MonthChangeListener mMonthChangeListener = (newYear, newMonth) -> {
        List<WeekViewEvent> events = new ArrayList<>();

        ArrayList<Period> periods = Utils.INSTANCE.getDummyPeriods();
        for (int i = 0; i < periods.size(); i++) {
            WeekViewEvent event = new WeekViewEvent(i + 1, periods.get(i).getCourse(), Utils.INSTANCE.getTime(periods.get(i).getStart(), newMonth, newYear, periods.get(i).getDayOfWeek()), Utils.INSTANCE.getTime(periods.get(i).getEnd(), newMonth, newYear, periods.get(i).getDayOfWeek()));
            event.setLocation(String.format("%s to %s", periods.get(i).getStart(), periods.get(i).getEnd()));
            event.setColor(Color.parseColor(periods.get(i).getColor()));
            events.add(event);
        }
        return events;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        ((TextView) findViewById(R.id.title)).setText(R.string.my_timetable);

        setSupportActionBar(findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        disableFlags(true);
        enableWhiteStatusBar();

        WeekView weekView = findViewById(R.id.weekView);
        weekView.setEventMarginVertical((int) Utils.INSTANCE.dpToPixels(this, 2));
        weekView.setEventCornerRadius((int) Utils.INSTANCE.dpToPixels(this, 4));
        weekView.setEventPadding((int) Utils.INSTANCE.dpToPixels(this, 6));
        weekView.setHourHeight((int) Utils.INSTANCE.dpToPixels(this, 72));

        weekView.setMonthChangeListener(mMonthChangeListener);
        weekView.goToToday();
        weekView.goToHour(08d);
    }
}
