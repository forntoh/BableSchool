package ga.forntoh.bableschool.model;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static ga.forntoh.bableschool.utils.Utils.TAG;

public class Score {

    private double firstSequenceMark, secondSequenceMark;
    private Course course;

    private Score(float firstSequenceMark, float secondSequenceMark, Course course) {
        this.firstSequenceMark = firstSequenceMark;
        this.secondSequenceMark = secondSequenceMark;
        this.course = course;
    }

    public static List<Score> getDummyScores() {
        ArrayList<Score> scores = new ArrayList<>();
        scores.add(new Score(10.56f, 11.25f, new Course("Biology", null, null, "#74ebd5", "#9face6")));
        scores.add(new Score(9.5f, 15f, new Course("Physics", null, null, "#6a85b6", "#bac8e0")));
        scores.add(new Score(12.5f, 13.75f, new Course("Geography", null, null, "#0ba360", "#3cba92")));
        scores.add(new Score(4f, 16f, new Course("Biology", null, null, "#00c6fb", "#005bea")));
        scores.add(new Score(18f, 17.5f, new Course("History", null, null, "#7028e4", "#e5b2ca")));
        scores.add(new Score(3f, 7.5f, new Course("Chemistry", null, null, "#ff9a9e", "#fad0c4")));
        scores.add(new Score(11f, 12f, new Course("Literature", null, null, "#a18cd1", "#fbc2eb")));
        scores.add(new Score(19f, 16.5f, new Course("Further Math", null, null, "#84fab0", "#8fd3f4")));
        scores.add(new Score(10.5f, 12f, new Course("Economics", null, null, "#74ebd5", "#9face6")));
        scores.add(new Score(7f, 12.75f, new Course("French", null, null, "#CBBACC", "#2580B3")));
        scores.add(new Score(15f, 15.5f, new Course("Physical Education", null, null, "#fc6076", "#ff9a44")));
        Log.d(TAG, "getDummyScores() returned: " + new Gson().toJson(scores));
        return scores;
    }

    public double getFirstSequenceMark() {
        return firstSequenceMark;
    }

    public double getSecondSequenceMark() {
        return secondSequenceMark;
    }

    public Course getCourse() {
        return course;
    }

    public double getScoreAverage() {
        return (firstSequenceMark + secondSequenceMark) / 2.0;
    }
}
