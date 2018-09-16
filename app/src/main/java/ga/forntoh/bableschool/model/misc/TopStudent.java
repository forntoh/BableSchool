package ga.forntoh.bableschool.model.misc;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;

import static ga.forntoh.bableschool.utils.Utils.TAG;

public class TopStudent {

    private String image, name, surname, school, average;

    private TopStudent(String image, String name, String surname, String school, String average) {
        this.image = image;
        this.name = name;
        this.surname = surname;
        this.school = school;
        this.average = average;
    }

    public static Collection<? extends TopStudent> getDummyTopStudents() {
        ArrayList<TopStudent> list = new ArrayList<>();
        list.add(new TopStudent("https://images.pexels.com/photos/450271/pexels-photo-450271.jpeg?auto=compress&cs=tinysrgb&h=250", "John", "Doe", "Stanford", "18.96"));
        list.add(new TopStudent("https://images.pexels.com/photos/415829/pexels-photo-415829.jpeg?auto=compress&cs=tinysrgb&h=250", "Michy", "Doe", "Stanford", "18.96"));
        list.add(new TopStudent("https://images.pexels.com/photos/91227/pexels-photo-91227.jpeg?auto=compress&cs=tinysrgb&h=250", "Manug", "Doe", "Stanford", "16.45"));
        list.add(new TopStudent("https://images.pexels.com/photos/462680/pexels-photo-462680.jpeg?auto=compress&cs=tinysrgb&h=250", "Senor", "Doe", "Stanford", "16.96"));
        list.add(new TopStudent("https://images.pexels.com/photos/1222271/pexels-photo-1222271.jpeg?auto=compress&cs=tinysrgb&h=250", "Admin", "Doe", "Stanford", "18.73"));
        list.add(new TopStudent("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSIrG30eAAzdzzvL33WMKa2Du0Tc2uKt-FIpEoIfBj48suSw1zIjw", "Peter", "Doe", "Stanford", "17.00"));
        list.add(new TopStudent("http://3.bp.blogspot.com/-gAijPmdYiU0/U_Ho-OgciYI/AAAAAAAAYQ4/nG4jFNDGgx0/s1600/Martin%2BFranks.jpg", "TKC", "Doe", "Stanford", "19.02"));
        list.add(new TopStudent("https://www.tvnz.co.nz/content/dam/images/news/2015/10/13/he-was-an-awesome-coach-sonny-bill-praises-france-s-philippe", "Philippe", "Doe", "Stanford", "18.96"));
        list.add(new TopStudent("https://i.ytimg.com/vi/TMyC4PoQb_I/hqdefault.jpg", "Dollar", "Man", "Stanford", "16.35"));
        list.add(new TopStudent("https://i.pinimg.com/236x/91/7a/88/917a88a81d165461b612cc18d8ac4653--oscars-red-carpets-academy-awards.jpg", "Raspberry", "Pi", "Stanford", "17.99"));
        Log.d(TAG, "getDummyTopStudents() returned: " + new Gson().toJson(list));
        return list;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getSchool() {
        return school;
    }

    public String getAverage() {
        return average;
    }
}
