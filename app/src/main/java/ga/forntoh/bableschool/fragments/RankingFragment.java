package ga.forntoh.bableschool.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ga.forntoh.bableschool.R;
import ga.forntoh.bableschool.adapters.TopSchoolsAdapter;
import ga.forntoh.bableschool.adapters.TopStudentsAdapter;
import ga.forntoh.bableschool.model.misc.TopSchool;
import ga.forntoh.bableschool.model.misc.TopStudent;
import ga.forntoh.bableschool.utils.InsetDecoration;

import static ga.forntoh.bableschool.utils.Utils.setupHorizontalDisplay;
import static ga.forntoh.bableschool.utils.Utils.setupVerticalDisplay;

/**
 * A simple {@link Fragment} subclass.
 */
public class RankingFragment extends Fragment {

    private Thread t1;
    private TopStudentsAdapter topStudentsAdapter;
    private TopSchoolsAdapter topSchoolsAdapter;
    private ArrayList<TopStudent> topStudents = new ArrayList<>();
    private ArrayList<TopSchool> topSchools = new ArrayList<>();

    public RankingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);

        topStudentsAdapter = new TopStudentsAdapter(topStudents);
        t1 = setupHorizontalDisplay(topStudentsAdapter, topStudents, view.findViewById(R.id.rv_top_students), 5000, false, new InsetDecoration(getContext(), 16));

        topSchoolsAdapter = new TopSchoolsAdapter(topSchools);
        setupVerticalDisplay(topSchoolsAdapter, view.findViewById(R.id.rv_school_ranking), true, new InsetDecoration(getContext(), 16));

        fetchItems();

        return view;
    }

    private void fetchItems() {
        topStudents.clear();
        topStudents.addAll(TopStudent.getDummyTopStudents());
        topStudentsAdapter.notifyDataSetChanged();

        topSchools.clear();
        topSchools.addAll(TopSchool.getDummyTopSchools());
        topSchoolsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (t1 != null) t1.interrupt();
    }

}
