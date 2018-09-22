package ga.forntoh.bableschool.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.forntoh.EasyRecyclerView.EasyRecyclerView;
import com.google.gson.reflect.TypeToken;
import com.nytimes.android.external.store3.base.impl.Store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import ga.forntoh.bableschool.R;
import ga.forntoh.bableschool.adapters.TermScoresAdapter;
import ga.forntoh.bableschool.model.Score;
import ga.forntoh.bableschool.network.ApiService;
import ga.forntoh.bableschool.network.RetrofitBuilder;
import ga.forntoh.bableschool.store.MyStores;
import ga.forntoh.bableschool.store.StorageUtil;
import ga.forntoh.bableschool.store.StoreRepository;
import ga.forntoh.bableschool.utils.Utils;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings("unchecked")
public class TermScoreFragment extends Fragment {


    private boolean fetched = false;
    private ArrayList<Score> scores = new ArrayList<>();
    private TermScoresAdapter termScoresAdapter;

    public TermScoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_term_score, container, false);

        if (getArguments() != null) {

            termScoresAdapter = new TermScoresAdapter(scores);
            new EasyRecyclerView()
                    .setType(EasyRecyclerView.Type.VERTICAL)
                    .setAdapter(termScoresAdapter)
                    .setRecyclerView(v.findViewById(R.id.rv_term_scores))
                    .setItemSpacing(16, null)
                    .initialize();

            fetchItems(getArguments().getInt("term"));
        }

        return v;
    }

    @SuppressLint("CheckResult")
    private void fetchItems(int term) {
        ApiService service = RetrofitBuilder.createService(ApiService.class);
        StoreRepository repository = new StoreRepository(Objects.requireNonNull(getActivity()).getApplication());

        Store store = repository
                .create(service.getTermScores(StorageUtil.with(getActivity()).loadMatriculation(), term, Utils.getTermYear()), new TypeToken<List<Score>>() {
                }.getType());

        if (fetched)
            store
                    .get(MyStores.TERM_SCORE(getContext(), term))
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::dealWithScores, t -> ((Throwable) t).printStackTrace());
        else {
            store
                    .fetch(MyStores.TERM_SCORE(getContext(), term))
                    .subscribeOn(Schedulers.io())
                    .subscribe(scores -> {
                        this.dealWithScores(scores);
                        fetched = true;
                    }, t -> ((Throwable) t).printStackTrace());
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void dealWithScores(Object o) {
        this.scores.clear();
        this.scores.addAll((Collection<? extends Score>) o);
        this.scores.add(0, null);
        getActivity().runOnUiThread(() -> termScoresAdapter.notifyDataSetChanged());
    }

}
