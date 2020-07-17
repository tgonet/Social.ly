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

import sg.MAD.socially.Class.NotificationFriend;
import sg.MAD.socially.R;

import static java.util.Calendar.getInstance;

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
        Calendar currentTime = Calendar.getInstance();
        NotificationFriend notif = data.get(position);
        holder.Image.setImageURI(Uri.parse(notif.getImageURL()));
        holder.Content.setText(notif.getInfo());
        /*
        int days = Duration.between(curr.toInstant(), calendar2.toInstant());
        LocalDate d1 = new LocalDate(currentTime.getTimeInMillis());
        LocalDate d2 = new LocalDate(notif.getTime().getTimeInMillis());
        int duration =currentTime.roll(notif.getTime(),false);
*/
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
