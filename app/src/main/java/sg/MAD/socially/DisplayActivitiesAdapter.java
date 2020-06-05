package sg.MAD.socially;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DisplayActivitiesAdapter extends RecyclerView.Adapter<DisplayActivitiesAdapter.MyViewHolder> {

    Context context;
    ArrayList<Activity> activities;

    public DisplayActivitiesAdapter(Context c, ArrayList<Activity> a)
    {
        context = c;
        activities = a;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.activityitem, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.Name_register.setText(activities.get(position).getName_register());
        holder.act_location.setText(activities.get(position).getAct_location());
        holder.act_time.setText(activities.get(position).getAct_time());
        holder.activity_name.setText(activities.get(position).getActivity_name());
        holder.act_desc.setText(activities.get(position).getAct_desc());
        holder.act_date.setText(activities.get(position).getAct_date());

        Picasso.get().load(activities.get(position).getProfile_image()).into(holder.profile_image);
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView Name_register, act_location, act_time, activity_name, act_desc, act_date;
        ImageView act_picture, profile_image;

        public MyViewHolder (View itemView)
        {
            super (itemView);
            Name_register = (TextView) itemView.findViewById(R.id.Name_register);
            act_location = (TextView) itemView.findViewById(R.id.act_location);
            act_time = (TextView) itemView.findViewById(R.id.act_time);
            activity_name = (TextView) itemView.findViewById(R.id.activity_name);
            act_desc = (TextView) itemView.findViewById(R.id.act_desc);
            act_date = (TextView) itemView.findViewById(R.id.act_date);

            act_picture = (ImageView) itemView.findViewById(R.id.act_picture);
            profile_image = (ImageView) itemView.findViewById(R.id.profile_image);

        }
    }
}
