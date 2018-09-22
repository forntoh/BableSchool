package ga.forntoh.bableschool.fragments;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.forntoh.EasyRecyclerView.EasyRecyclerView;
import com.google.gson.Gson;

import ga.forntoh.bableschool.R;
import ga.forntoh.bableschool.adapters.DocumentsAdapter;
import ga.forntoh.bableschool.adapters.VideosAdapter;
import ga.forntoh.bableschool.model.Course;
import ga.forntoh.bableschool.model.User;
import ga.forntoh.bableschool.network.ApiService;
import ga.forntoh.bableschool.network.RetrofitBuilder;
import ga.forntoh.bableschool.store.MyStores;
import ga.forntoh.bableschool.store.StorageUtil;
import ga.forntoh.bableschool.store.StoreRepository;
import ga.forntoh.bableschool.utils.SquareConstraintLayout;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings("unchecked")
public class CourseNoteFragment extends Fragment {

    VideosAdapter videosAdapter;
    DocumentsAdapter documentsAdapter;

    public CourseNoteFragment() {
        // Required empty public constructor
    }


    @SuppressWarnings("ConstantConditions")
    @SuppressLint("CheckResult")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_course_note, container, false);
        SquareConstraintLayout subject_circle = v.findViewById(R.id.subject_circle);
        TextView subject_abbr = v.findViewById(R.id.subject_abbr);
        TextView subject_title = v.findViewById(R.id.subject_title);
        TextView subject_class = v.findViewById(R.id.subject_class);

        String[] startColors = getActivity().getResources().getStringArray(R.array.start_colors);
        String[] endColors = getActivity().getResources().getStringArray(R.array.end_colors);

        if (getArguments() != null) {
            Course course = new Gson().fromJson(getArguments().getString("course"), Course.class);
            int index = getArguments().getInt("index");

            subject_title.setText(course.getTitle());

            subject_abbr.setText(course.getAbbr());

            ApiService service = RetrofitBuilder.createService(ApiService.class);
            StoreRepository repository = new StoreRepository(getActivity().getApplication());
            repository
                    .create(service.getUserProfile(StorageUtil.with(getContext()).loadMatriculation(), StorageUtil.with(getContext()).loadPassword()), User.class)
                    .get(MyStores.USER)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            user -> getActivity().runOnUiThread(() -> subject_class.setText(((User) user).getClasse())),
                            t -> ((Throwable) t).printStackTrace()
                    );

            GradientDrawable bg = new GradientDrawable(GradientDrawable.Orientation.TR_BL, new int[]{Color.parseColor(startColors[index]), Color.parseColor(endColors[index])});
            bg.setShape(GradientDrawable.OVAL);
            subject_circle.setBackground(bg);

            videosAdapter = new VideosAdapter(course.getVideos());
            new EasyRecyclerView()
                    .setType(EasyRecyclerView.Type.HORIZONTAL)
                    .setAdapter(videosAdapter)
                    .setRecyclerView(v.findViewById(R.id.rv_videos))
                    .setItemSpacing(16, null)
                    .initialize();

            documentsAdapter = new DocumentsAdapter(course.getDocuments());
            new EasyRecyclerView()
                    .setType(EasyRecyclerView.Type.VERTICAL)
                    .setAdapter(documentsAdapter)
                    .setRecyclerView(v.findViewById(R.id.rv_documents))
                    .setItemSpacing(16, null)
                    .initialize();
        }

        return v;
    }


}
