package ga.forntoh.bableschool.model;

import java.util.ArrayList;
import java.util.Calendar;

public class Period {

    private String start;
    private String end;
    private String course;
    private String color;
    private int dayOfWeek; // 1 Sun, 2 Mon, 3 Tue, 4 Wed, 5 Thu, 6 Fri, 7 Sat

    private Period(String start, String end, String course, int dayOfWeek, String color) {
        this.start = start;
        this.end = end;
        this.course = course;
        this.dayOfWeek = dayOfWeek;
        this.color = color;
    }

    public static ArrayList<Period> getDummyPeriods() {
        ArrayList<Period> periods = new ArrayList<>();

        periods.add(new Period("08:00", "10:00", "Electrocinétique", Calendar.MONDAY, "#59dbe0"));
        periods.add(new Period("12:00", "12:30", "Break", Calendar.MONDAY, "#3c3c3c"));
        periods.add(new Period("12:30", "14:30", "Algo et structure données", Calendar.MONDAY, "#f57f68"));
        periods.add(new Period("14:30", "16:30", "Electrostatique & Electromag", Calendar.MONDAY, "#87d288"));

        periods.add(new Period("08:00", "10:00", "Electrocinétique", Calendar.TUESDAY, "#59dbe0"));
        periods.add(new Period("10:00", "12:00", "Algèbre linéaire I", Calendar.TUESDAY, "#f8b552"));
        periods.add(new Period("12:00", "12:30", "Break", Calendar.TUESDAY, "#3c3c3c"));
        periods.add(new Period("12:30", "14:30", "Architecture ordinateurs et SE", Calendar.TUESDAY, "#FF4081"));
        periods.add(new Period("14:30", "16:30", "Electrostatique & Electromag", Calendar.TUESDAY, "#87d288"));

        periods.add(new Period("08:00", "10:00", "Analyse I", Calendar.WEDNESDAY, "#66A6FF"));
        periods.add(new Period("10:00", "12:00", "Optique", Calendar.WEDNESDAY, "#078B75"));
        periods.add(new Period("12:00", "12:30", "Break", Calendar.WEDNESDAY, "#3c3c3c"));
        periods.add(new Period("12:30", "16:30", "SEMINAIRE-ATELIER (HUAWEI) : Formation des talents aux TICS ", Calendar.WEDNESDAY, "#9C27B0"));

        periods.add(new Period("08:00", "10:00", "Architecture ordinateurs et SE", Calendar.THURSDAY, "#FF4081"));
        periods.add(new Period("10:00", "12:00", "Algèbre linéaire I", Calendar.THURSDAY, "#f8b552"));
        periods.add(new Period("12:00", "12:30", "Break", Calendar.THURSDAY, "#3c3c3c"));
        periods.add(new Period("12:30", "14:30", "Langues", Calendar.THURSDAY, "#f57f68"));

        periods.add(new Period("08:00", "10:00", "Optique", Calendar.FRIDAY, "#078B75"));
        periods.add(new Period("10:00", "12:00", "Algo et structure données", Calendar.FRIDAY, "#f57f68"));
        periods.add(new Period("12:00", "12:30", "Break", Calendar.FRIDAY, "#3c3c3c"));
        periods.add(new Period("12:30", "14:30", "Algo et structure données", Calendar.FRIDAY, "#f57f68"));
        periods.add(new Period("14:30", "16:30", "Sport", Calendar.FRIDAY, "#FF4081"));

        return periods;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getColor() {
        return color;
    }

    public String getCourse() {
        return course;
    }

    public Calendar getTime(int newMonth, int newYear, boolean isStart) {
        Calendar time = Calendar.getInstance();
        String string = isStart ? start : end;

        int hour = Integer.parseInt(string.split(":")[0]);
        int minute = Integer.parseInt(string.split(":")[1]);

        time.set(Calendar.HOUR_OF_DAY, hour);
        time.set(Calendar.MINUTE, minute);
        time.set(Calendar.MONTH, newMonth - 1);
        time.set(Calendar.YEAR, newYear);
        time.set(Calendar.DAY_OF_WEEK, dayOfWeek);

        return time;
    }

    public String getEventSubTitle() {
        return String.format("%s to %s", start, end);
    }
}
