package ga.forntoh.bableschool.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import ga.forntoh.bableschool.R;

@SuppressWarnings("unchecked")
public class ProfileDataAdapter extends RecyclerView.Adapter<ProfileDataAdapter.MyViewHolder> {

    private Object[] list;

    public ProfileDataAdapter(LinkedHashMap<String, String> list) {
        Set<Map.Entry<String, String>> mapSet = list.entrySet();
        this.list = mapSet.toArray();
    }

    @NonNull
    @Override
    public ProfileDataAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProfileDataAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_data, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileDataAdapter.MyViewHolder holder, int position) {
        Map.Entry<String, String> item = (Map.Entry<String, String>) list[position];
        holder.title.setText(item.getKey());
        holder.message.setText(item.getValue());

        if (item.getKey().equals("Password"))
            holder.message.setTransformationMethod(new PasswordTransformationMethod());
        else holder.message.setTransformationMethod(null);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView title, message;

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
