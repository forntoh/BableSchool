package ga.forntoh.bableschool.model;

import java.util.LinkedHashMap;

@SuppressWarnings("ALL")
public class User {

    private String username, classe, picture;
    private LinkedHashMap<String, String> profileData;

    public String getUsername() {
        return "@" + username;
    }

    public String getClasse() {
        return classe;
    }

    public String getPicture() {
        return picture;
    }

    public LinkedHashMap<String, String> getProfileData() {
        return profileData;
    }
}
