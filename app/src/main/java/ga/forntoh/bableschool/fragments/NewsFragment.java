package ga.forntoh.bableschool.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.forntoh.EasyRecyclerView.EasyRecyclerView;

import java.util.ArrayList;

import ga.forntoh.bableschool.R;
import ga.forntoh.bableschool.adapters.NewsAdapter;
import ga.forntoh.bableschool.model.News;

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
        t1 = new EasyRecyclerView()
                .setType(EasyRecyclerView.Type.HORIZONTAL)
                .setAdapter(newsAdapter1)
                .setRecyclerView(view.findViewById(R.id.rv_top_news))
                .setItemSpacing(16, null)
                .initialize(topNewsList.size(), 5000, true);

        newsAdapter2 = new NewsAdapter(allNewsList, true);
        new EasyRecyclerView()
                .setType(EasyRecyclerView.Type.VERTICAL)
                .setAdapter(newsAdapter2)
                .setRecyclerView(view.findViewById(R.id.rv_all_news))
                .setItemSpacing(16, null)
                .initialize();

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
