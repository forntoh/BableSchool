package ga.forntoh.bableschool.model;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class Course {

    private String code;
    private String title;
    private ArrayList<Video> videos = new ArrayList<>();
    private ArrayList<Document> documents = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public String getAbbr() {
        String[] words = this.title.split(" ");
        String title = "";
        if (words.length > 1) {
            for (String s : words)
                title += s.charAt(0);
            return title;
        } else return this.title.substring(0, 3);
    }

    public ArrayList<Video> getVideos() {
        return videos;
    }

    public ArrayList<Document> getDocuments() {
        return documents;
    }

    public String getCode() {
        return code;
    }
}
