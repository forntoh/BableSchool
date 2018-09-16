package ga.forntoh.bableschool.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ga.forntoh.bableschool.R;
import ga.forntoh.bableschool.adapters.NewsAdapter;
import ga.forntoh.bableschool.model.News;
import ga.forntoh.bableschool.utils.InsetDecoration;

import static ga.forntoh.bableschool.utils.Utils.setupHorizontalDisplay;
import static ga.forntoh.bableschool.utils.Utils.setupVerticalDisplay;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {

    private Thread t1;
    private ArrayList<News> topNewsList = new ArrayList<>(), allNewsList = new ArrayList<>();
    private NewsAdapter newsAdapter1, newsAdapter2;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        newsAdapter1 = new NewsAdapter(topNewsList, false);
        newsAdapter2 = new NewsAdapter(allNewsList, true);

        t1 = setupHorizontalDisplay(newsAdapter1, topNewsList, view.findViewById(R.id.rv_top_news), 5000, true, new InsetDecoration(getContext(), 16));
        setupVerticalDisplay(newsAdapter2, view.findViewById(R.id.rv_all_news), false, new InsetDecoration(getContext(), 16));

        fetchItems();

        return view;
    }

    private void fetchItems() {
        topNewsList.clear();
        topNewsList.addAll(News.getDummyNews());
        newsAdapter1.notifyDataSetChanged();

        allNewsList.clear();
        allNewsList.addAll(News.getDummyNews());
        newsAdapter2.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (t1 != null) t1.interrupt();
    }

}
