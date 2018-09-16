package ga.forntoh.bableschool.model;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

import static ga.forntoh.bableschool.utils.Utils.TAG;

public class Course {

    public String startColor, endColor;
    private String title;
    private ArrayList<Video> videos = new ArrayList<>();
    private ArrayList<Document> documents = new ArrayList<>();

    Course(String title, ArrayList<Video> videos, ArrayList<Document> documents, String startColor, String endColor) {
        this.title = title;
        this.videos.clear();
        if (videos != null) this.videos.addAll(videos);
        this.documents.clear();
        if (documents != null) this.documents.addAll(documents);
        this.startColor = startColor;
        this.endColor = endColor;
    }

    public static ArrayList<Course> getDummyCourseNotes() {
        ArrayList<Course> notes = new ArrayList<>();

        ArrayList<Video> videos = new ArrayList<>();
        ArrayList<Document> documents = new ArrayList<>();

        videos.add(new Video("AP® Biology - Part 1: The Cell", "edX", "1:07", "https://www.nhc.noaa.gov/video/DOLLY.mp4", "https://www.edx.org/sites/default/files/course/image/promoted/ap-biology-part-2_378x225_0.jpg"));
        videos.add(new Video("Biology: Cell Structure", "Nucleus Medical Media", "7:22", "https://ftp.gnu.org/video/A_Digital_Media_Primer_For_Geeks-480p.webm", "https://i.ytimg.com/vi/URUJD5NEXC8/hqdefault.jpg?sqp=-oaymwEZCPYBEIoBSFXyq4qpAwsIARUAAIhCGAFwAQ==&rs=AOn4CLCOG1lXD80KkBbJe60OC-mBLC3e3w"));
        videos.add(new Video("Map of Biology", "Domain of Science", "8:41", "http://103.114.38.38/HDD1/English%20Movies/Almost%20Ablaze%20(2014).mp4", "https://i.ytimg.com/vi/wENhHnJI1ys/hqdefault.jpg?sqp=-oaymwEZCPYBEIoBSFXyq4qpAwsIARUAAIhCGAFwAQ==&rs=AOn4CLCQUMU_bK4BkrJXdXqzvvtpz6OEaQ"));
        videos.add(new Video("GENE MAPPING/HOW TO DECODE 13q14.3", "Medinaz", "2:33", "https://www.nhc.noaa.gov/video/DOLLY.mp4", "https://i.ytimg.com/vi/gGUxf-aL1DA/hqdefault.jpg?sqp=-oaymwEZCPYBEIoBSFXyq4qpAwsIARUAAIhCGAFwAQ==&rs=AOn4CLDyumXATXnqLQdkaCgLziBG0q8WzQ"));
        videos.add(new Video("What Is Biology?", "MonkeySee", "2:11", "https://www.nhc.noaa.gov/video/DOLLY.mp4", "https://i.ytimg.com/vi/oFtVDLFXKLA/hqdefault.jpg?sqp=-oaymwEZCPYBEIoBSFXyq4qpAwsIARUAAIhCGAFwAQ==&rs=AOn4CLBqxFcmX6Y2AZiRUVLdzz8jTgCG9g"));
        documents.add(new Document("The Science of Biology", "Raven Johnson McGraw-Hill", "2.93 MB", "1996", "http://pdf995.com/samples/pdf.pdf"));
        documents.add(new Document("Advanced Biology (2nd Edition)", "Raven Johnson McGraw-Hill", "2.93 MB", "1996", "http://pdf995.com/samples/pdfeditsample.pdf"));
        documents.add(new Document("Modern Biology", "ESJ Wasmann", "1.20 MB", "1996", "http://pdf995.com/samples/widgets.pdf"));
        documents.add(new Document("Molecular Biology of the Cell", "Garland Science", "321 KB", "2001", "http://www.snee.com/xml/xslt/sample.doc"));
        documents.add(new Document("Campbell Biology (11th Revised Edition)", "Jane Reece, Lisa A. Urry, Michael L. Cain", "11 MB", "2013", "http://www.africau.edu/images/default/sample.pdf"));

        notes.add(new Course("Biology", videos, documents, "#74ebd5", "#9face6"));
        notes.add(new Course("Physics", videos, documents, "#6a85b6", "#bac8e0"));
        notes.add(new Course("Geography", videos, documents, "#0ba360", "#3cba92"));
        notes.add(new Course("Biology", videos, documents, "#00c6fb", "#005bea"));
        notes.add(new Course("History", videos, documents, "#7028e4", "#e5b2ca"));
        notes.add(new Course("Chemistry", videos, documents, "#ff9a9e", "#fad0c4"));
        notes.add(new Course("Literature", videos, documents, "#a18cd1", "#fbc2eb"));
        notes.add(new Course("Further Math", videos, documents, "#84fab0", "#8fd3f4"));
        notes.add(new Course("Economics", videos, documents, "#74ebd5", "#9face6"));
        notes.add(new Course("French", videos, documents, "#CBBACC", "#2580B3"));
        notes.add(new Course("Physical Education", videos, documents, "#fc6076", "#ff9a44"));

        Log.d(TAG, "getDummyCourseNotes() returned: " + new Gson().toJson(notes));
        return notes;
    }

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
}
