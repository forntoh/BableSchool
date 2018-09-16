package ga.forntoh.bableschool.model;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

import static ga.forntoh.bableschool.utils.Utils.TAG;

public class User {

    private String username, classe, picture;
    private ArrayList<ProfileData> profileData;

    private User(String username, String classe, String picture, ArrayList<ProfileData> profileData) {
        this.username = username;
        this.classe = classe;
        this.picture = picture;
        this.profileData = profileData;
    }

    public static User getDummyUser() {
        ArrayList<ProfileData> profileData = new ArrayList<>();
        User user = new User("johndoe", "Form 5A", "https://images.pexels.com/photos/450271/pexels-photo-450271.jpeg?auto=compress&cs=tinysrgb&h=250", profileData);
        profileData.add(user.new ProfileData("Full name", "John Doe"));
        profileData.add(user.new ProfileData("School", "GBHS Somewhere"));
        profileData.add(user.new ProfileData("Class", "Form 5A"));
        profileData.add(user.new ProfileData("Matriculation", "#12H5T29"));
        profileData.add(user.new ProfileData("Password", "******"));
        profileData.add(user.new ProfileData("Phone", "690 000 000 "));
        Log.d(TAG, "getDummyUser() returned: " + new Gson().toJson(user));
        return user;
    }

    public String getUsername() {
        return "@" + username;
    }

    public String getClasse() {
        return classe;
    }

    public String getPicture() {
        return picture;
    }

    public ArrayList<ProfileData> getProfileData() {
        return profileData;
    }

    public class ProfileData {

        private String key, value;

        ProfileData(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }
}
