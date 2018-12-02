package ga.forntoh.bableschool.adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ga.forntoh.bableschool.R;
import ga.forntoh.bableschool.model.Course;
import ga.forntoh.bableschool.model.Score;
import ga.forntoh.bableschool.utils.Utils;

public class TermScoresAdapter extends CategoryAdapter {

    public TermScoresAdapter(ArrayList list) {
        super(list);
    }

    @NonNull
    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (startColors == null)
            startColors = parent.getContext().getResources().getStringArray(R.array.start_colors);
        if (endColors == null)
            endColors = parent.getContext().getResources().getStringArray(R.array.end_colors);
        View v;
        switch (viewType) {
            case 0:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_summary, parent, false);
                return new MyViewHolder(v, viewType);
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_term_subject, parent, false);
                return new MyViewHolder(v, viewType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                double total = 0, average;
                for (int i = 1; i < list.size(); i++)
                    if (list.get(i) != null)
                        total += ((Score) list.get(i)).getScoreAverage();
                average = total / (list.size() - 1);
                holder.rank.setText(((Score) list.get(list.size() - 1)).getTermRank());
                holder.average.setText(Utils.INSTANCE.formatScore(average));
                holder.average.setTextColor(holder.average.getContext().getResources().getColor(average > 10 ? R.color.textBlue : R.color.bad));
                break;
            case 1:
                Score score = (Score) list.get(position);
                if (score == null) return;

                Course course = score.getCourse();

                holder.title.setText(course.getTitle());
                holder.abbreviation.setText(course.getAbbr());
                holder.rank.setText(score.getRank());
                holder.firstAv.setText(Utils.INSTANCE.formatScore(score.getFirstSequenceMark()));
                holder.secondAv.setText(Utils.INSTANCE.formatScore(score.getSecondSequenceMark()));
                holder.average.setText(Utils.INSTANCE.formatScore(score.getScoreAverage()));

                holder.firstAv.setTextColor(holder.title.getContext().getResources().getColor(score.getFirstSequenceMark() > 10 ? R.color.good : R.color.bad));
                holder.secondAv.setTextColor(holder.title.getContext().getResources().getColor(score.getSecondSequenceMark() > 10 ? R.color.good : R.color.bad));
                holder.average.setTextColor(holder.title.getContext().getResources().getColor(score.getScoreAverage() > 10 ? R.color.textBlue : R.color.bad));

                GradientDrawable bg = new GradientDrawable(GradientDrawable.Orientation.TR_BL, new int[]{Color.parseColor(startColors[position % startColors.length]), Color.parseColor(endColors[position % startColors.length])});
                bg.setShape(GradientDrawable.OVAL);
                holder.color_circle.setBackground(bg);
                break;
        }
    }

    class MyViewHolder extends CategoryAdapter.MyViewHolder {

        MyViewHolder(View itemView, int viewType) {
            super(itemView);
            switch (viewType) {
                case 0:
                    average = itemView.findViewById(R.id.tv_average);
                    rank = itemView.findViewById(R.id.tv_position);
                    break;
                default:
                    color_circle = itemView.findViewById(R.id.subject_circle);
                    abbreviation = itemView.findViewById(R.id.subject_abbr);
                    title = itemView.findViewById(R.id.subject_title);
                    average = itemView.findViewById(R.id.subject_average);
                    firstAv = itemView.findViewById(R.id.subject_first);
                    secondAv = itemView.findViewById(R.id.subject_second);
                    rank = itemView.findViewById(R.id.subject_rank);
                    break;
            }
        }

        @Override
        public void onClick(View view) {

        }
    }
}
