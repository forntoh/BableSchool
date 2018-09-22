package ga.forntoh.bableschool.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nytimes.android.external.store3.base.impl.Store;

import java.util.Objects;

import ga.forntoh.bableschool.R;
import ga.forntoh.bableschool.model.misc.AnnualRank;
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
public class YearScoreFragment extends Fragment {


    private boolean fetched = false;
    private TextView average, position;

    public YearScoreFragment() {
        // Required empty public constructor
    }


    @SuppressLint("CheckResult")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_year_score, container, false);

        average = view.findViewById(R.id.student_average);
        position = view.findViewById(R.id.student_position);

        ApiService service = RetrofitBuilder.createService(ApiService.class);
        StoreRepository repository = new StoreRepository(Objects.requireNonNull(getActivity()).getApplication());

        Store store = repository
                .create(service.annualRank(StorageUtil.with(getActivity()).loadMatriculation(), Utils.getTermYear()), AnnualRank.class);

        if (fetched)
            store
                    .get(MyStores.TERM_RANK(getContext()))
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::dealWithRank, t -> ((Throwable) t).printStackTrace());
        else {
            store
                    .fetch(MyStores.TERM_RANK(getContext()))
                    .subscribeOn(Schedulers.io())
                    .subscribe(rank -> {
                        this.dealWithRank(rank);
                        fetched = true;
                    }, t -> ((Throwable) t).printStackTrace());
        }

        return view;
    }

    private void dealWithRank(Object o) {
        AnnualRank rank = (AnnualRank) o;
        position.post(() -> position.setText(rank.position));
        average.post(() -> average.setText(Utils.formatScore(Double.parseDouble(rank.average))));
    }

}
