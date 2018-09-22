package ga.forntoh.bableschool.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.forntoh.EasyRecyclerView.EasyRecyclerView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import ga.forntoh.bableschool.R;
import ga.forntoh.bableschool.adapters.CommentsAdapter;
import ga.forntoh.bableschool.model.Comment;
import ga.forntoh.bableschool.model.News;

/**
 * A simple {@link Fragment} subclass.
 */
public class SingleNewsFragment extends Fragment {

    CommentsAdapter adapter;
    EditText commentBox;
    Button postBtn;
    News news;

    public SingleNewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_single_news, container, false);
        if (getArguments() != null) {
            news = new Gson().fromJson(getArguments().getString("news"), News.class);

            TextView title = v.findViewById(R.id.news_title);
            TextView meta = v.findViewById(R.id.news_meta);
            TextView description = v.findViewById(R.id.news_description);
            TextView comment_count = v.findViewById(R.id.comment_count);
            TextView like_count = v.findViewById(R.id.like_count);
            ImageView thumbnail = v.findViewById(R.id.news_thumbnail);

            commentBox = v.findViewById(R.id.et_message);
            postBtn = v.findViewById(R.id.btn_post);
            postBtn.setOnClickListener(view -> onPostClicked());

            title.setText(news.getTitle());
            description.setText(news.getDescription());
            comment_count.setText(getString(R.string.comment_counter, news.getComments().size()));
            meta.setText(getString(R.string.news_meta, news.getAuthor(), DateFormat.format("EEE, MMM d, yyyy", news.getLongDate()), news.getCategory()));
            like_count.setText(getString(R.string.like_counter, news.getLikes()));
            if (TextUtils.isEmpty(news.getThumbnail()))
                Picasso.get().load(R.drawable.placeholder).fit().centerCrop().into(thumbnail);
            else
                Picasso.get().load(news.getThumbnail()).placeholder(R.drawable.placeholder).fit().centerCrop().into(thumbnail);

            adapter = new CommentsAdapter(news.getComments());
            new EasyRecyclerView()
                    .setType(EasyRecyclerView.Type.VERTICAL)
                    .setAdapter(adapter)
                    .setRecyclerView(v.findViewById(R.id.rv_comments))
                    .setItemSpacing(16, null)
                    .initialize();

            adapter.notifyDataSetChanged();
        }
        return v;
    }

    private void onPostClicked() {
        String text = commentBox.getText().toString();
        if (!TextUtils.isEmpty(text)) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Comment comment = new Comment("Michy", formatter.format(new Date()), text, "https://images.pexels.com/photos/450271/pexels-photo-450271.jpeg?auto=compress&cs=tinysrgb&h=250");
            news.getComments().add(comment);
            adapter.notifyItemInserted(news.getComments().size());
            commentBox.setText("");
        }
    }

}
