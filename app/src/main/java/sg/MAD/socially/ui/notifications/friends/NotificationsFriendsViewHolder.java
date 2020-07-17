package sg.MAD.socially.ui.notifications.friends;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sg.MAD.socially.R;

public class NotificationsFriendsViewHolder extends RecyclerView.ViewHolder {
    ImageView Image;
    TextView Content;
    TextView Duration;

    public NotificationsFriendsViewHolder(final View itemView){
        super(itemView);
        Image = itemView.findViewById(R.id.notification_friend_image);
        Content = itemView.findViewById(R.id.notification_friend_content);
        Duration = itemView.findViewById(R.id.notification_friend_duration);
    }
}
