package ga.forntoh.bableschool.model.misc;

import ga.forntoh.bableschool.utils.Utils;

@SuppressWarnings("unused")
public class TopStudent {

    private String image, name, surname, school;
    private double average;

    public String getImage() {
        return image;
    }

    public String getName() {
        return Utils.capEachWord(name);
    }

    public String getSurname() {
        return surname;
    }

    public String getSchool() {
        return school;
    }

    public double getAverage() {
        return average;
    }
}
