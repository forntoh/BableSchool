package ga.forntoh.bableschool.adapters;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ga.forntoh.bableschool.R;
import ga.forntoh.bableschool.model.misc.TopStudent;
import ga.forntoh.bableschool.utils.Utils;

public class TopStudentsAdapter extends CategoryAdapter {

    public TopStudentsAdapter(ArrayList list) {
        super(list);
    }

    @NonNull
    @Override
    public TopStudentsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TopStudentsAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_students, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, int position) {
        TopStudent topStudent = (TopStudent) list.get(position);
        holder.name.setText(topStudent.getName());
        holder.school.setText(topStudent.getSchool());
        holder.average.setText(Utils.formatScore(topStudent.getAverage()));
        if (TextUtils.isEmpty(topStudent.getImage()))
            Picasso.get().load(R.drawable.placeholder).fit().centerCrop().into(holder.thumbnail);
        else
            Picasso.get().load(topStudent.getImage()).placeholder(R.drawable.placeholder).fit().centerCrop().into(holder.thumbnail);
    }

    class MyViewHolder extends CategoryAdapter.MyViewHolder {

        MyViewHolder(View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.top_student_image);
            name = itemView.findViewById(R.id.top_student_name);
            school = itemView.findViewById(R.id.top_student_school);
            average = itemView.findViewById(R.id.top_student_average);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
