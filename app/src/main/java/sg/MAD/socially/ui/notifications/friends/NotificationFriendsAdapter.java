package sg.MAD.socially.ui.notifications.friends;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import sg.MAD.socially.Class.NotificationFriend;
import sg.MAD.socially.R;

public class NotificationFriendsAdapter extends RecyclerView.Adapter<NotificationsFriendsViewHolder>{
    ArrayList<NotificationFriend> data;

    public NotificationFriendsAdapter(ArrayList<NotificationFriend> input) {
        data = input;
    }

    @NonNull
    @Override
    public NotificationsFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.fragment_notifications_friends_list_item, parent, false);
        return new NotificationsFriendsViewHolder(item);
    }

    @Override
    public void onBindViewHolder(NotificationsFriendsViewHolder holder, int position) {
        NotificationFriend notif = data.get(position);
        holder.Image.setImageURI(Uri.parse(notif.getImageURL()));
        holder.Content.setText(notif.getInfo());

        Date currentTime = new Date();
        long time = Long.parseLong(notif.getTime());
        Date notifTime = new Date(time);

        long diffInMillis = currentTime.getTime() - notifTime.getTime();
        String duration;

        if (diffInMillis < 60000){
            duration = TimeUnit.SECONDS.convert(diffInMillis, TimeUnit.MILLISECONDS) + "s";
        }
        else if (diffInMillis < 3.6e+6)
        {
            duration = TimeUnit.MINUTES.convert(diffInMillis, TimeUnit.MILLISECONDS) + "m";
        }
        else if (diffInMillis < 8.64e+7){
            duration = TimeUnit.HOURS.convert(diffInMillis, TimeUnit.MILLISECONDS) + "h";
        }
        else {
            long temp;
            temp = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
            if (diffInMillis < 6.048e+8) {
                duration = temp + "d";
            }
            else{
                duration = temp + "w";
            }
        }

        holder.Duration.setText(duration);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
