package ga.forntoh.bableschool.model;

import android.support.annotation.DrawableRes;

import ga.forntoh.bableschool.R;

public class Document {

    private String title, author, size, published, url;

    Document(String title, String author, String size, String published, String url) {
        this.title = title;
        this.author = author;
        this.size = size;
        this.published = published;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getSize() {
        return size;
    }

    public String getPublished() {
        return published;
    }

    public String getUrl() {
        return url;
    }

    public @DrawableRes
    int getType() {
        String[] words = url.split("\\.");
        String ext = words[words.length - 1].toLowerCase();
        switch (ext) {
            case "pdf":
                return R.drawable.ic_pdf;
            case "doc":
                return R.drawable.ic_doc;
            default:
                return -1;
        }
    }
}
