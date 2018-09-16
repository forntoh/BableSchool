package ga.forntoh.bableschool.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ga.forntoh.bableschool.CategoryActivity;
import ga.forntoh.bableschool.R;
import ga.forntoh.bableschool.model.Category;
import ga.forntoh.bableschool.utils.SquareConstraintLayout;
import ga.forntoh.bableschool.utils.Utils;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    ArrayList list;

    public CategoryAdapter(ArrayList list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Category category = (Category) list.get(position);
        holder.title.setText(category.getTitle());
        holder.title.setTextColor(Color.parseColor(category.getColor()));
        holder.thumbnail.setColorFilter(Color.parseColor(category.getColor()), PorterDuff.Mode.SRC_ATOP);
        if (TextUtils.isEmpty(category.getThumbnail()))
            Picasso.get().load(R.drawable.category).fit().centerInside().into(holder.thumbnail);
        else
            Picasso.get().load(category.getThumbnail()).placeholder(R.drawable.category).fit().centerInside().into(holder.thumbnail);

        GradientDrawable drawable = (GradientDrawable) holder.parent.getBackground();
        drawable.setStroke((int) Utils.dpToPixels(holder.thumbnail.getContext(), 2), Color.parseColor(category.getColor()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @SuppressWarnings("unchecked")
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, abbreviation, stats, author, duration, message, date, name, school, average, firstAv, secondAv;
        ImageView thumbnail, download_thumbnail;
        SquareConstraintLayout parent, color_circle;

        MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.category_title);
            thumbnail = itemView.findViewById(R.id.category_icon);
            parent = itemView.findViewById(R.id.parent);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), CategoryActivity.class);
            intent.putExtra("category", new Gson().toJson(list.get(getAdapterPosition())));
            view.getContext().startActivity(intent);
        }
    }
}
