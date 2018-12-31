package ga.forntoh.bableschool.adapters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import ga.forntoh.bableschool.CategoryActivity;
import ga.forntoh.bableschool.R;
import ga.forntoh.bableschool.fragments.SingleNewsFragment;
import ga.forntoh.bableschool.model.News;
import ga.forntoh.bableschool.utils.Utils;

public class NewsAdapter extends CategoryAdapter {

    private final boolean isNormal;

    public NewsAdapter(ArrayList list, boolean isNormal) {
        super(list);
        this.isNormal = isNormal;
    }

    @NonNull
    @Override
    public NewsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(isNormal ? R.layout.item_news : R.layout.item_news_top, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, int position) {
        News news = (News) list.get(position);
        holder.title.setText(news.getTitle());
        holder.author.setText(isNormal ? Utils.INSTANCE.getRelativeTimeSpanString(news.getDate(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())) + "  |  " + "@" + news.getAuthor() : "By " + news.getAuthor() + " on " + DateFormat.format("EEE, MMM d, yyyy", Utils.INSTANCE.getLongDate(news.getDate())));
        if (TextUtils.isEmpty(news.getThumbnail()))
            Picasso.get().load(R.drawable.placeholder).fit().centerCrop().into(holder.thumbnail);
        else
            Picasso.get().load(news.getThumbnail()).placeholder(R.drawable.placeholder).fit().centerCrop().into(holder.thumbnail);
    }

    class MyViewHolder extends CategoryAdapter.MyViewHolder {

        MyViewHolder(View itemView) {
            super(itemView);
            CardView parent = itemView.findViewById(R.id.card_parent);
            title = itemView.findViewById(R.id.news_title);
            author = itemView.findViewById(R.id.news_meta);
            thumbnail = itemView.findViewById(R.id.news_thumbnail);
            if (!isNormal)
                parent.getLayoutParams().width = (int) (Utils.INSTANCE.getScreenWidth() - Utils.INSTANCE.dpToPixels(itemView.getContext(), 56));
        }

        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putLong("news", ((News) list.get(getAdapterPosition())).getId());
            SingleNewsFragment fragment = new SingleNewsFragment();
            fragment.setArguments(bundle);
            ((CategoryActivity) view.getContext()).loadFragment(fragment).addToBackStack(null);
        }
    }
}
