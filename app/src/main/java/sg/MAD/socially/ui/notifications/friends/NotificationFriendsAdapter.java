package sg.MAD.socially.ui.notifications.friends;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        Glide.with(holder.itemView).load(notif.getImageURL()).into(holder.Image);
        holder.Content.setText(notif.getInfo());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String now = sdf.format(new Date());
        Date notifTime;
        Date currentTime;
        String duration;
        try {
            notifTime  = sdf.parse(notif.getTime());
            currentTime= sdf.parse(now);
            Log.d("Notification Friends", "current time:" + currentTime);
            Log.d("Notification Friends", "notifTime: " + notifTime);
            long diff = currentTime.getTime() - notifTime.getTime();

            int diffDays = (int) (diff / (24 * 60 * 60 * 1000));

            int diffHours = (int) (diff / (60 * 60 * 1000));

            int diffMin = (int) (diff / (60 * 1000));

            int diffSec = (int) (diff / (1000));

            if (diffDays > 0){
                duration = diffDays + "d";
            }
            else if (diffHours > 0){
                duration = diffHours + "h";
            }
            else if (diffMin > 0){
                duration = diffMin + "m";
            }
            else {
                duration = diffSec + "s";
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
            duration = "no";
        }

        holder.Duration.setText(duration);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
