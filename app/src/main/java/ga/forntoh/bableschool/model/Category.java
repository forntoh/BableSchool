package ga.forntoh.bableschool.model;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

import static ga.forntoh.bableschool.utils.Utils.TAG;

public class Category {

    private int id;
    private String title;
    private String thumbnail;
    private String color;

    private Category(int id, String title, String thumbnail, String color) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.color = color;
    }

    public static ArrayList<Category> getDummyCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category(0, "News", "", "#9C27B0"));
        categories.add(new Category(1, "Course Notes", "", "#2196F3"));
        categories.add(new Category(2, "Rankings", "", "#009688"));
        categories.add(new Category(3, "Forum", "", "#FF9800"));
        categories.add(new Category(4, "My Profile", "", "#795548"));
        Log.d(TAG, "getDummyCategories() returned: " + new Gson().toJson(categories));
        return categories;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getColor() {
        return color;
    }

    public int getId() {
        return id;
    }
}
