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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import ga.forntoh.bableschool.R;
import ga.forntoh.bableschool.adapters.TopSchoolsAdapter;
import ga.forntoh.bableschool.adapters.TopStudentsAdapter;
import ga.forntoh.bableschool.model.misc.TopSchool;
import ga.forntoh.bableschool.model.misc.TopStudent;
import ga.forntoh.bableschool.network.ApiService;
import ga.forntoh.bableschool.network.RetrofitBuilder;
import ga.forntoh.bableschool.store.MyStores;
import ga.forntoh.bableschool.store.StorageUtil;
import ga.forntoh.bableschool.store.StoreRepository;
import io.reactivex.schedulers.Schedulers;

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
        t1 = new EasyRecyclerView()
                .setType(EasyRecyclerView.Type.HORIZONTAL)
                .setAdapter(topStudentsAdapter)
                .setRecyclerView(view.findViewById(R.id.rv_top_students))
                .setItemSpacing(16, null)
                .initialize(topStudents.size(), 5000, false);

        topSchoolsAdapter = new TopSchoolsAdapter(topSchools);
        new EasyRecyclerView()
                .setType(EasyRecyclerView.Type.VERTICAL)
                .setAdapter(topSchoolsAdapter)
                .setRecyclerView(view.findViewById(R.id.rv_school_ranking))
                .setItemSpacing(16, null)
                .initialize();

        fetchItems();

        return view;
    }

    @SuppressWarnings("unchecked")
    @SuppressLint("CheckResult")
    private void fetchItems() {
        ApiService service = RetrofitBuilder.createService(ApiService.class);
        StoreRepository repository = new StoreRepository(Objects.requireNonNull(getActivity()).getApplication());
        repository.create(
                service.getTopSchools(), new TypeToken<List<TopSchool>>() {
                }.getType())
                .fetch(MyStores.RANKING)
                .flatMap(list -> {
                    topSchools.clear();
                    topSchools.addAll((Collection<? extends TopSchool>) list);
                    getActivity().runOnUiThread(() -> topSchoolsAdapter.notifyDataSetChanged());
                    return service.getTopStudents(StorageUtil.with(getActivity()).loadMatriculation());
                })
                .subscribeOn(Schedulers.io())
                .subscribe(
                        list -> {
                            topStudents.clear();
                            topStudents.addAll((Collection<? extends TopStudent>) list);
                            getActivity().runOnUiThread(() -> topStudentsAdapter.notifyDataSetChanged());
                        },
                        t -> ((Throwable) t).printStackTrace()
                );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (t1 != null) t1.interrupt();
    }

}
