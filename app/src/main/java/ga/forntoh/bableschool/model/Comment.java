package ga.forntoh.bableschool.model;

import android.annotation.SuppressLint;
import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Comment {

    private String sender, date, message, thumbnail;

    public Comment(String sender, String date, String message, String thumbnail) {
        this.sender = sender;
        this.date = date;
        this.message = message;
        this.thumbnail = thumbnail;
    }

    public static ArrayList<Comment> getDummyComments() {
        ArrayList<Comment> list = new ArrayList<>();
        list.add(new Comment("Michy", "2018-08-29 21:23:00", "Nice designs :)", "https://images.pexels.com/photos/450271/pexels-photo-450271.jpeg?auto=compress&cs=tinysrgb&h=250"));
        list.add(new Comment("Manug", "2018-08-29 19:55:00", "Lorem ipsum dolor sit amet, consectetuer ", "https://images.pexels.com/photos/415829/pexels-photo-415829.jpeg?auto=compress&cs=tinysrgb&h=250"));
        list.add(new Comment("ExpertIHM", "2018-08-28 06:00:00", "Very essential bro", "https://images.pexels.com/photos/91227/pexels-photo-91227.jpeg?auto=compress&cs=tinysrgb&h=250"));
        list.add(new Comment("Senor", "2018-08-27 22:00:00", "Wow, I didn\'t know about this ♥", "https://images.pexels.com/photos/462680/pexels-photo-462680.jpeg?auto=compress&cs=tinysrgb&h=250"));
        list.add(new Comment("admin", "2018-04-29 21:23:00", "Thanks for the information, it was very very helpful", "https://images.pexels.com/photos/1222271/pexels-photo-1222271.jpeg?auto=compress&cs=tinysrgb&h=250"));
        return list;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getSender() {
        return sender;
    }

    public String getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public String getRelativeDate() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setLenient(false);
        try {
            Date oldDate = formatter.parse(date);
            return (String) DateUtils.getRelativeTimeSpanString(oldDate.getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        } catch (ParseException ignored) {
            return "";
        }
    }
}
