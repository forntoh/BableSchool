package ga.forntoh.bableschool.adapters;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ga.forntoh.bableschool.R;
import ga.forntoh.bableschool.model.User;

public class ProfileDataAdapter extends CategoryAdapter {

    public ProfileDataAdapter(ArrayList list) {
        super(list);
    }

    @NonNull
    @Override
    public ProfileDataAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProfileDataAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_data, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, int position) {
        User.ProfileData profileData = (User.ProfileData) list.get(position);
        holder.title.setText(profileData.getKey());
        holder.message.setText(profileData.getValue());
    }

    class MyViewHolder extends CategoryAdapter.MyViewHolder implements View.OnClickListener {

        MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.profile_data_key);
            message = itemView.findViewById(R.id.profile_data_value);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
