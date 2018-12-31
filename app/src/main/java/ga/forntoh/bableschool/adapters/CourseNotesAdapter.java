package ga.forntoh.bableschool.adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ga.forntoh.bableschool.CategoryActivity;
import ga.forntoh.bableschool.R;
import ga.forntoh.bableschool.fragments.CourseNoteFragment;
import ga.forntoh.bableschool.model.Course;

public class CourseNotesAdapter extends CategoryAdapter {

    public CourseNotesAdapter(ArrayList list) {
        super(list);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (startColors == null)
            startColors = parent.getContext().getResources().getStringArray(R.array.start_colors);
        if (endColors == null)
            endColors = parent.getContext().getResources().getStringArray(R.array.end_colors);
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_note, parent, false));
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, int position) {
        Course note = (Course) list.get(position);
        holder.title.setText(note.getTitle());

        holder.abbreviation.setText(note.getAbbr());
        holder.stats.setText(String.format("%d Videos | %d Documents", note.getVideos().size(), note.getDocuments().size()));

        GradientDrawable bg = new GradientDrawable(GradientDrawable.Orientation.TR_BL, new int[]{Color.parseColor(startColors[position % startColors.length]), Color.parseColor(endColors[position % startColors.length])});
        bg.setShape(GradientDrawable.OVAL);
        holder.color_circle.setBackground(bg);
    }

    class MyViewHolder extends CategoryAdapter.MyViewHolder {

        MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.subject_title);
            abbreviation = itemView.findViewById(R.id.subject_abbr);
            stats = itemView.findViewById(R.id.tv);
            color_circle = itemView.findViewById(R.id.subject_circle);
        }

        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putString("course", ((Course) list.get(getAdapterPosition())).getCode());
            bundle.putInt("index", getAdapterPosition() % startColors.length);
            CourseNoteFragment fragment = new CourseNoteFragment();
            fragment.setArguments(bundle);
            ((CategoryActivity) view.getContext()).loadFragment(fragment).addToBackStack(null);
        }
    }

}
