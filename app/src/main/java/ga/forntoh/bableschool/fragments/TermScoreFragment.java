package ga.forntoh.bableschool.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ga.forntoh.bableschool.R;
import ga.forntoh.bableschool.adapters.TermScoresAdapter;
import ga.forntoh.bableschool.model.Score;
import ga.forntoh.bableschool.utils.InsetDecoration;

import static ga.forntoh.bableschool.utils.Utils.setupVerticalDisplay;

/**
 * A simple {@link Fragment} subclass.
 */
public class TermScoreFragment extends Fragment {


    private ArrayList<Score> scores = new ArrayList<>();
    private TermScoresAdapter termScoresAdapter;

    public TermScoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_term_score, container, false);

        if (getArguments() != null) {

            scores.add(0, null);
            termScoresAdapter = new TermScoresAdapter(scores);
            setupVerticalDisplay(termScoresAdapter, v.findViewById(R.id.rv_term_scores), false, new InsetDecoration(getContext(), 16));

            fetchItems(getArguments().getInt("term"));
        }

        return v;
    }

    private void fetchItems(int term) {
        scores.clear();
        scores.addAll(Score.getDummyScores());
        termScoresAdapter.notifyDataSetChanged();
    }

}
