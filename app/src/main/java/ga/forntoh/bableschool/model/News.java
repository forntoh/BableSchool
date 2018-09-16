package ga.forntoh.bableschool.model;

import android.annotation.SuppressLint;
import android.text.format.DateUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static ga.forntoh.bableschool.utils.Utils.TAG;

public class News {

    private String title, author, description, date, thumbnail, category;
    private ArrayList<Comment> comments;
    private int likes;

    private News(String title, String author, String description, String date, ArrayList<Comment> comments, int likes, String category, String thumbnail) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.date = date;
        this.comments = comments;
        this.likes = likes;
        this.category = category;
        this.thumbnail = thumbnail;
    }

    public static ArrayList<News> getDummyNews() {
        ArrayList<News> news = new ArrayList<>();
        news.add(new News(
                "Contraception app advert banned by UK regulator",
                "Jane Wakefield",
                "An advert on Facebook for an app that provides a natural alternative to contraception has been banned by the UK's Advertising Standards Authority.\n" +
                        "\n" +
                        "Claims that it was \"highly accurate\" and \"provided a clinically tested alternative to other birth control methods\" were found to be misleading.\n" +
                        "\n" +
                        "The Swedish firm behind the Natural Cycles app was warned \"not to exaggerate\" its efficacy.\n" +
                        "\n" +
                        "In response, the firm said it respected the outcome of the investigation.\n" +
                        "\n" +
                        "It told the BBC that it removed the ad, which ran for approximately four weeks in mid-2017, as soon as it was notified of the complaint.\n" +
                        "\n" +
                        "\"We are committed to being open and transparent in our communications to ensure our message is clear and provides women with the information they need to determine if Natural Cycles is right for them. As part of these efforts, every advertisement undergoes a strict approval process,\" the firm said in a statement.\n" +
                        "\n" +
                        "\"Natural Cycles has been independently evaluated and cleared by regulators in Europe and the US based on clinical evidence demonstrating its effectiveness as a method of contraception.\"",
                "2018-08-29 21:23:00", Comment.getDummyComments(),
                203,
                "News",
                "https://images.pexels.com/photos/114820/pexels-photo-114820.jpeg?auto=compress&cs=tinysrgb&h=512"));
        news.add(new News(
                "Stars hit Venice Film Festival red carpet",
                "Anita",
                "Ryan Gosling and Claire Foy join fellow stars at the opening ceremony of the 75th Venice Film Festival.",
                "2018-08-29 21:23:00", Comment.getDummyComments(),
                98,
                "News",
                "https://images.pexels.com/photos/545331/pexels-photo-545331.jpeg?auto=compress&cs=tinysrgb&h=512"));
        news.add(new News(
                "Some Amazon employees upset at response to defaced LGBT office posters",
                "Eugene Kim",
                "About 10 LGBT posters in elevators at Amazon headquarters were defaced, according to an email chain seen by CNBC. Some employees thought Amazon's response, including a poster warning employees not to deface company property, did not go far enough.",
                "2018-08-29 21:23:00", Comment.getDummyComments(),
                23,
                "News",
                "https://images.pexels.com/photos/1083622/pexels-photo-1083622.jpeg?auto=compress&cs=tinysrgb&h=512"));
        news.add(new News(
                "How do you market a country?",
                "Ben King",
                "There are nearly 200 countries in the world today, all competing for resources, trade and attention. So if you're a nation in a remote corner of the planet, how do you go about telling everyone that you exist?\n" +
                        "\n" +
                        "Countries are increasingly copying the marketing tactics that companies use to raise their profiles, and let people know that they are open for business. Welcome to the world of nation branding.\n" +
                        "\n" +
                        "A strong country brand should encourage tourists, trading partners and investors all at once. But having a snazzy logo, and an advertising budget won't sell a product that people don't want.\n" +
                        "\n" +
                        "So what should a country do to try to boost its profile? The South American country Chile is a good case study.",
                "2018-08-29 21:23:00", Comment.getDummyComments(),
                1000,
                "News",
                "https://ichef.bbci.co.uk/news/660/cpsprodpb/16C9E/production/_103224339_gettyimages-898204232.jpg"));
        news.add(new News(
                "Trump says White House counsel to depart",
                "Don McGahn",
                "US President Donald Trump has said White House lawyer Don McGahn will leave his post in the coming months.\n" +
                        "\n" +
                        "The president tweeted that Mr McGahn would depart after the confirmation of a Supreme Court nominee in the autumn.\n" +
                        "\n" +
                        "His exit follows reports of White House unease at the extent of Mr McGahn's co-operation with an ongoing inquiry into alleged Russia election meddling.\n" +
                        "\n" +
                        "It is not unusual for White House counsels to come and go during an administration.",
                "2018-08-29 21:23:00", Comment.getDummyComments(),
                32,
                "News",
                "https://ichef.bbci.co.uk/news/660/cpsprodpb/AF9D/production/_103075944_a24f76b9-86e2-4782-a5d3-18f39d55fe5e.jpg"));
        news.add(new News(
                "Chasing quakes with machine learning",
                "Mary Halton",
                "Scientists have used machine learning to calculate the pattern of aftershocks following an earthquake.\n" +
                        "\n" +
                        "Aftershocks are further quakes that follow the \"main shock\". They are by definition smaller, but sometimes not by much.\n" +
                        "\n" +
                        "This is the first time a machine learning method has been used to work out where they might happen.\n" +
                        "\n" +
                        "Researchers hope this and similar techniques will improve our understanding of earthquake behaviour.",
                "2018-08-29 21:23:00", Comment.getDummyComments(),
                59,
                "News",
                "https://ichef.bbci.co.uk/news/660/cpsprodpb/146EB/production/_103219638_gettyimages-623040372.jpg"));
        news.add(new News(
                "Title goes here.....",
                "Anita",
                "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam.",
                "2018-08-29 21:23:00", Comment.getDummyComments(),
                3,
                "News",
                "https://images.pexels.com/photos/256219/pexels-photo-256219.jpeg?auto=compress&cs=tinysrgb&h=512"));
        news.add(new News(
                "Fewer families where no-one working",
                "Sean Coughlan",
                "The proportion of households in the UK where no-one is working is at its lowest point for over 20 years, the Office for National Statistics says.\n" +
                        "\n" +
                        "The figures show 14.3% of households containing working-age adults are \"workless\" - down 0.2% compared with the same point last year.\n" +
                        "\n" +
                        "Fewer children were living in families where no-one was currently working.\n" +
                        "\n" +
                        "But more children were living in households where no-one had ever worked - up by 32,000 to 204,000.\n" +
                        "\n" +
                        "The employment figures show a picture of rising levels of work in the 21 million households with people aged between 16 and 64.",
                "2018-08-29 21:23:00", Comment.getDummyComments(),
                10,
                "News",
                "https://ichef.bbci.co.uk/news/660/cpsprodpb/00C9/production/_103210200_commuting.jpg"));
        news.add(new News(
                "Wiesbaden in Germany removes gold statue of Turkey's President Erdogan",
                "Anita",
                "The German city of Wiesbaden has removed a 4m (13ft) golden statue of Turkey's president after it was defaced with the words \"Turkish Hitler\".\n" +
                        "\n" +
                        "The statue of Recep Tayyip Erdogan was installed in a square as part of Wiesbaden's Biennale arts festival.\n" +
                        "\n" +
                        "Organisers said they had hoped it would spark discussions linked to this year's theme - \"bad news\".\n" +
                        "\n" +
                        "Instead, it prompted conflict between Mr Erdogan's supporters and critics. Firefighters moved it on Tuesday night.\n" +
                        "\n" +
                        "City councillor Oliver Franz told the Wiesbadener Kurier newspaper that angry words had escalated into physical scuffles, and \"bladed weapons were spotted\".\n" +
                        "\n" +
                        "\"In agreement with state police, Mayor Sven Gerich decided to have the statue removed as security could no longer be guaranteed,\" the city's government tweeted.",
                "2018-08-29 21:23:00", Comment.getDummyComments(),
                149,
                "News",
                "https://ichef.bbci.co.uk/news/660/cpsprodpb/7F8B/production/_103215623_erdoganeffigy.jpg"));
        news.add(new News(
                "Uncle and niece die at caravan park in Mansfield",
                "BBC East Midlands",
                "A man and his niece have died at a caravan park in Nottinghamshire.\n" +
                        "\n" +
                        "Richard Thompson, 66, and 48-year-old Lisa Butler were treated by paramedics but pronounced dead at Tall Trees Park Homes in Forest Town, Mansfield, soon afterwards.\n" +
                        "\n" +
                        "Nottinghamshire Police said officers were called to the scene at about 22:00 BST on Tuesday.\n" +
                        "\n" +
                        "Some residents told the BBC they had heard a violent assault, but this has not been confirmed by police.\n" +
                        "\n" +
                        "Nottinghamshire Police said post-mortems are taking place to establish the cause of their deaths.\n" +
                        "\n" +
                        "Det Insp Rich Monk said: \"Following our initial investigations we now believe this is an isolated incident and we are not looking for anyone else in connection with the deaths.\"",
                "2018-08-29 21:23:00", Comment.getDummyComments(),
                623,
                "News",
                "https://ichef.bbci.co.uk/news/660/cpsprodpb/17A3F/production/_103213869_img_3898.jpg"));
        Log.d(TAG, "getDummyNews() returned: " + new Gson().toJson(news));
        return news;
    }

    public String getCategory() {
        return category;
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

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public int getLikes() {
        return likes;
    }

    public long getLongDate() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setLenient(false);

        try {
            Date oldDate = formatter.parse(date);
            return oldDate.getTime();
        } catch (ParseException ignored) {
            return -1;
        }
    }

    public String getRelativeDate() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setLenient(false);
        try {
            Date oldDate = formatter.parse(date);
            return (String) DateUtils.getRelativeTimeSpanString(oldDate.getTime(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
        } catch (ParseException ignored) {
            return "";
        }
    }
}
