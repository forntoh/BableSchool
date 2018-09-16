package ga.forntoh.bableschool.adapters;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ga.forntoh.bableschool.R;
import ga.forntoh.bableschool.model.Comment;

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
        holder.date.setText(comment.getRelativeDate());
        if (TextUtils.isEmpty(comment.getThumbnail()))
            Picasso.get().load(R.drawable.placeholder).fit().centerCrop().into(holder.thumbnail);
        else
            Picasso.get().load(comment.getThumbnail()).placeholder(R.drawable.placeholder).fit().centerCrop().into(holder.thumbnail);
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
