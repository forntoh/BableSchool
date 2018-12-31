package ga.forntoh.bableschool.adapters;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import ga.forntoh.bableschool.R;
import ga.forntoh.bableschool.model.Comment;
import ga.forntoh.bableschool.utils.Utils;

public class CommentsAdapter extends CategoryAdapter {

    public CommentsAdapter(ArrayList list) {
        super(list);
    }

    @NonNull
    @Override
    public CommentsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentsAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, int position) {
        Comment comment = (Comment) list.get(position);
        holder.author.setText(comment.getSender());
        holder.message.setText(comment.getMessage());
        holder.date.setText(Utils.INSTANCE.getRelativeTimeSpanString(comment.getDate(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())));
        if (TextUtils.isEmpty(comment.getThumbnail()))
            Picasso.get().load(R.drawable.placeholder).fit().centerCrop().into(holder.thumbnail);
        else
            Picasso.get().load(comment.getThumbnail()).placeholder(R.drawable.placeholder).fit().centerCrop().into(holder.thumbnail);
    }

    @SuppressWarnings("unchecked")
    public void addComment(@NotNull Comment comment) {
        list.add(comment);
        notifyItemInserted(list.size());
    }

    class MyViewHolder extends CategoryAdapter.MyViewHolder {

        MyViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.comment_date);
            author = itemView.findViewById(R.id.comment_sender);
            message = itemView.findViewById(R.id.comment_message);
            thumbnail = itemView.findViewById(R.id.author_image);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
