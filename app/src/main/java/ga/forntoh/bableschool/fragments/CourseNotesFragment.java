package ga.forntoh.bableschool.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.forntoh.EasyRecyclerView.EasyRecyclerView;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ga.forntoh.bableschool.R;
import ga.forntoh.bableschool.adapters.CourseNotesAdapter;
import ga.forntoh.bableschool.model.Course;
import ga.forntoh.bableschool.network.ApiService;
import ga.forntoh.bableschool.network.RetrofitBuilder;
import ga.forntoh.bableschool.store.MyStores;
import ga.forntoh.bableschool.store.StorageUtil;
import ga.forntoh.bableschool.store.StoreRepository;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings("unchecked")
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
        new EasyRecyclerView()
                .setType(EasyRecyclerView.Type.VERTICAL)
                .setAdapter(adapter)
                .setRecyclerView(view.findViewById(R.id.rv_course_notes))
                .setItemSpacing(16, null)
                .initialize();

        fetchItems();
        return view;
    }

    @SuppressWarnings("ConstantConditions")
    @SuppressLint("CheckResult")
    private void fetchItems() {
        ApiService service = RetrofitBuilder.createService(ApiService.class);
        StoreRepository repository = new StoreRepository(getActivity().getApplication());
        repository
                .create(service.getCourseNotes(StorageUtil.with(getContext()).loadMatriculation()), new TypeToken<List<Course>>() {
                }.getType())
                .get(MyStores.COURSES(getContext()))
                .subscribeOn(Schedulers.io())
                .subscribe(
                        courses -> {
                            list.clear();
                            list.addAll((Collection<? extends Course>) courses);
                            getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
                        },
                        t -> ((Throwable) t).printStackTrace()
                );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adapter != null) adapter = null;
    }

}
