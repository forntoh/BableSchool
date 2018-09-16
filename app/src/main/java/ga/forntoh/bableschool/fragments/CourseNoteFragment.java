package ga.forntoh.bableschool.fragments;


import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import ga.forntoh.bableschool.R;
import ga.forntoh.bableschool.adapters.DocumentsAdapter;
import ga.forntoh.bableschool.adapters.VideosAdapter;
import ga.forntoh.bableschool.model.Course;
import ga.forntoh.bableschool.utils.InsetDecoration;
import ga.forntoh.bableschool.utils.SquareConstraintLayout;

import static ga.forntoh.bableschool.utils.Utils.setupHorizontalDisplay;
import static ga.forntoh.bableschool.utils.Utils.setupVerticalDisplay;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseNoteFragment extends Fragment {

    VideosAdapter videosAdapter;
    DocumentsAdapter documentsAdapter;

    public CourseNoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_course_note, container, false);
        SquareConstraintLayout subject_circle = v.findViewById(R.id.subject_circle);
        TextView subject_abbr = v.findViewById(R.id.subject_abbr);
        TextView subject_title = v.findViewById(R.id.subject_title);
        TextView subject_class = v.findViewById(R.id.subject_class);

        if (getArguments() != null) {
            Course course = new Gson().fromJson(getArguments().getString("course"), Course.class);

            subject_title.setText(course.getTitle());

            subject_abbr.setText(course.getAbbr());
            subject_class.setText("F1");

            GradientDrawable bg = new GradientDrawable(GradientDrawable.Orientation.TR_BL, new int[]{Color.parseColor(course.startColor), Color.parseColor(course.endColor)});
            bg.setShape(GradientDrawable.OVAL);
            subject_circle.setBackground(bg);

            videosAdapter = new VideosAdapter(course.getVideos());
            setupHorizontalDisplay(videosAdapter, null, v.findViewById(R.id.rv_videos), 0, false, new InsetDecoration(getContext(), 16));

            documentsAdapter = new DocumentsAdapter(course.getDocuments());
            setupVerticalDisplay(documentsAdapter, v.findViewById(R.id.rv_documents), false, new InsetDecoration(getContext(), 16));
        }

        return v;
    }


}
