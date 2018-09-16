package ga.forntoh.bableschool.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import ga.forntoh.bableschool.utils.InsetDecoration;

import static ga.forntoh.bableschool.utils.Utils.setupVerticalDisplay;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment {


    private ProfileDataAdapter adapter;

    public MyProfileFragment() {
        // Required empty public constructor
    }


    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_profile, container, false);

        TextView profile_class = v.findViewById(R.id.profile_class);
        TextView profile_username = v.findViewById(R.id.profile_username);
        CircleImageView profile_image = v.findViewById(R.id.profile_image);

        v.findViewById(R.id.btn_score_sheet).setOnClickListener(v1 -> getActivity().startActivity(new Intent(getActivity(), ScoreSheetActivity.class)));
        v.findViewById(R.id.btn_time_table).setOnClickListener(v2 -> getActivity().startActivity(new Intent(getContext(), TimeTableActivity.class)));

        User user = User.getDummyUser();
        profile_username.setText(user.getUsername());
        profile_class.setText(user.getClasse());
        if (TextUtils.isEmpty(user.getPicture()))
            Picasso.get().load(R.drawable.placeholder).fit().centerCrop().into(profile_image);
        else
            Picasso.get().load(user.getPicture()).placeholder(R.drawable.placeholder).fit().centerCrop().into(profile_image);

        adapter = new ProfileDataAdapter(user.getProfileData());
        setupVerticalDisplay(adapter, v.findViewById(R.id.rv_profile_data), false, new InsetDecoration(getContext(), 16));

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
