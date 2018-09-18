package ga.forntoh.bableschool.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import ga.forntoh.bableschool.R;
import ga.forntoh.bableschool.ScoreSheetActivity;
import ga.forntoh.bableschool.TimeTableActivity;
import ga.forntoh.bableschool.adapters.ProfileDataAdapter;
import ga.forntoh.bableschool.model.User;
import ga.forntoh.bableschool.network.ApiService;
import ga.forntoh.bableschool.network.RetrofitBuilder;
import ga.forntoh.bableschool.store.MyStores;
import ga.forntoh.bableschool.store.StorageUtil;
import ga.forntoh.bableschool.store.StoreRepository;
import ga.forntoh.bableschool.utils.InsetDecoration;
import io.reactivex.schedulers.Schedulers;

import static ga.forntoh.bableschool.utils.Utils.setupVerticalDisplay;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings({"ConstantConditions", "unchecked"})
public class MyProfileFragment extends Fragment {


    private TextView profile_class, profile_username;
    private CircleImageView profile_image;
    private RecyclerView rv_profile_data;
    private ProfileDataAdapter adapter;

    public MyProfileFragment() {
        // Required empty public constructor
    }


    @SuppressLint("CheckResult")
    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_profile, container, false);

        profile_class = v.findViewById(R.id.profile_class);
        profile_username = v.findViewById(R.id.profile_username);
        profile_image = v.findViewById(R.id.profile_image);
        rv_profile_data = v.findViewById(R.id.rv_profile_data);

        v.findViewById(R.id.btn_score_sheet).setOnClickListener(v1 -> getActivity().startActivity(new Intent(getActivity(), ScoreSheetActivity.class)));
        v.findViewById(R.id.btn_time_table).setOnClickListener(v2 -> getActivity().startActivity(new Intent(getContext(), TimeTableActivity.class)));

        ApiService service = RetrofitBuilder.createService(ApiService.class);
        StoreRepository repository = new StoreRepository(getActivity().getApplication());
        repository
                .create(service.getUserProfile(StorageUtil.with(getContext()).loadMatriculation(), StorageUtil.with(getContext()).loadPassword()), User.class)
                .get(MyStores.USER)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        user -> getActivity().runOnUiThread(() -> showItems((User) user)),
                        t -> ((Throwable) t).printStackTrace()
                );
        return v;
    }

    private void showItems(User user) {
        profile_username.setText(user.getUsername());
        profile_class.setText(user.getClasse());
        if (TextUtils.isEmpty(user.getPicture()))
            Picasso.get().load(R.drawable.placeholder).fit().centerCrop().into(profile_image);
        else
            Picasso.get().load(user.getPicture()).placeholder(R.drawable.placeholder).fit().centerCrop().into(profile_image);
        adapter = new ProfileDataAdapter(user.getProfileData());
        setupVerticalDisplay(adapter, rv_profile_data, false, new InsetDecoration(getContext(), 16));
    }
}
