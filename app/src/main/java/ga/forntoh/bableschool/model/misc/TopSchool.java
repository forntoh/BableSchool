package ga.forntoh.bableschool.model.misc;

import ga.forntoh.bableschool.utils.Utils;

@SuppressWarnings("unused")
public class TopSchool {

    private String image, schoolName, topStudentName;
    private double average;

    public String getImage() {
        return image;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public String getTopStudentName() {
        return Utils.capEachWord(topStudentName);
    }

    public double getAverage() {
        return average;
    }
}
