package ga.forntoh.bableschool.model.misc;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;

import static ga.forntoh.bableschool.utils.Utils.TAG;

public class TopSchool {

    private String image, schoolName, topStudentName, average;

    private TopSchool(String image, String topStudentName, String schoolName, String average) {
        this.image = image;
        this.schoolName = schoolName;
        this.topStudentName = topStudentName;
        this.average = average;
    }

    public static Collection<? extends TopSchool> getDummyTopSchools() {
        ArrayList<TopSchool> list = new ArrayList<>();
        list.add(new TopSchool("https://images.pexels.com/photos/450271/pexels-photo-450271.jpeg?auto=compress&cs=tinysrgb&h=250", "John", "Stanford", "18.96"));
        list.add(new TopSchool("https://images.pexels.com/photos/415829/pexels-photo-415829.jpeg?auto=compress&cs=tinysrgb&h=250", "Michy", "G.B.H.S Stanford", "18.96"));
        list.add(new TopSchool("https://images.pexels.com/photos/91227/pexels-photo-91227.jpeg?auto=compress&cs=tinysrgb&h=250", "Manug", "G.B.H.S Lakefield", "16.45"));
        list.add(new TopSchool("https://images.pexels.com/photos/462680/pexels-photo-462680.jpeg?auto=compress&cs=tinysrgb&h=250", "Senor", "G.B.H.S Somewhere", "16.96"));
        list.add(new TopSchool("https://images.pexels.com/photos/1222271/pexels-photo-1222271.jpeg?auto=compress&cs=tinysrgb&h=250", "Admin", "Saker BC", "18.73"));
        list.add(new TopSchool("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSIrG30eAAzdzzvL33WMKa2Du0Tc2uKt-FIpEoIfBj48suSw1zIjw", "Peter", "Ecole Dummy", "17.00"));
        list.add(new TopSchool("http://3.bp.blogspot.com/-gAijPmdYiU0/U_Ho-OgciYI/AAAAAAAAYQ4/nG4jFNDGgx0/s1600/Martin%2BFranks.jpg", "TKC", "MIT Tiil", "19.02"));
        list.add(new TopSchool("https://www.tvnz.co.nz/content/dam/images/news/2015/10/13/he-was-an-awesome-coach-sonny-bill-praises-france-s-philippe", "Doe School", "Stanford", "18.96"));
        list.add(new TopSchool("https://i.ytimg.com/vi/TMyC4PoQb_I/hqdefault.jpg", "Dollar", "Oxford Nursery School", "16.35"));
        list.add(new TopSchool("https://i.pinimg.com/236x/91/7a/88/917a88a81d165461b612cc18d8ac4653--oscars-red-carpets-academy-awards.jpg", "Raspberry", "Google Academy", "17.99"));
        Log.d(TAG, "getDummyTopSchools() returned: " + new Gson().toJson(list));
        return list;
    }

    public String getImage() {
        return image;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public String getTopStudentName() {
        return topStudentName;
    }

    public String getAverage() {
        return average;
    }
}
