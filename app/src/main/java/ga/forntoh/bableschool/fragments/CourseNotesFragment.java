package ga.forntoh.bableschool.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ga.forntoh.bableschool.R;
import ga.forntoh.bableschool.adapters.CourseNotesAdapter;
import ga.forntoh.bableschool.model.Course;
import ga.forntoh.bableschool.utils.InsetDecoration;

import static ga.forntoh.bableschool.utils.Utils.setupVerticalDisplay;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseNotesFragment extends Fragment {


    private RecyclerView.Adapter adapter;
    private ArrayList<Course> list = new ArrayList<>();

    public CourseNotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course_notes, container, false);

        adapter = new CourseNotesAdapter(list);
        setupVerticalDisplay(adapter, view.findViewById(R.id.rv_course_notes), false, new InsetDecoration(getContext(), 16));

        fetchItems();
        return view;
    }

    private void fetchItems() {
        list.clear();
        list.addAll(Course.getDummyCourseNotes());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adapter != null) adapter = null;
    }

}
