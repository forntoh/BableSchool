package ga.forntoh.bableschool.adapters;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ga.forntoh.bableschool.R;
import ga.forntoh.bableschool.model.TopSchool;
import ga.forntoh.bableschool.utils.Utils;

public class TopSchoolsAdapter extends CategoryAdapter {

    public TopSchoolsAdapter(ArrayList list) {
        super(list);
    }

    @NonNull
    @Override
    public TopSchoolsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TopSchoolsAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_school_ranking, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, int position) {
        TopSchool topStudent = (TopSchool) list.get(position);
        holder.name.setText(Utils.INSTANCE.capEachWord(topStudent.getTopStudentName()));
        holder.school.setText(topStudent.getSchoolName());
        holder.average.setText(Utils.INSTANCE.formatScore(topStudent.getAverage()));
        if (TextUtils.isEmpty(topStudent.getImage()))
            Picasso.get().load(R.drawable.placeholder).fit().centerCrop().into(holder.thumbnail);
        else
            Picasso.get().load(topStudent.getImage()).placeholder(R.drawable.placeholder).fit().centerCrop().into(holder.thumbnail);
        holder.school.setSelected(true);
    }

    class MyViewHolder extends CategoryAdapter.MyViewHolder {

        MyViewHolder(View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.top_school_image);
            name = itemView.findViewById(R.id.top_student_name);
            school = itemView.findViewById(R.id.top_school_name);
            average = itemView.findViewById(R.id.top_school_average);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
