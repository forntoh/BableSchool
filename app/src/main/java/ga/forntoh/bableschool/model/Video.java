package ga.forntoh.bableschool.model;

public class Video {

    private String title, author, duration, url, thumbnail;

    Video(String title, String author, String duration, String url, String thumbnail) {
        this.title = title;
        this.author = author;
        this.duration = duration;
        this.url = url;
        this.thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDuration() {
        return duration;
    }

    public String getUrl() {
        return url;
    }
}
