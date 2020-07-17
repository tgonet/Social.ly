package sg.MAD.socially.ui.notifications.friends;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.MAD.socially.Class.NotificationFriend;
import sg.MAD.socially.Class.User;
import sg.MAD.socially.R;

public class NotificationsFriendsFragment extends Fragment {
    ImageView image;
    TextView content;
    TextView duration;
    DatabaseReference reference;
    String currentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications_friends, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*
        image = getView().findViewById(R.id.notification_friend_image);
        content = getView().findViewById(R.id.notification_friend_content);
        duration = getView().findViewById(R.id.notification_friend_duration);
        RecyclerView recyclerView = getView().findViewById(R.id.rv_notifications_friends);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final ArrayList<NotificationFriend> notifList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId).child("Notifications");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    NotificationFriend notif = snapshot.getValue(NotificationFriend.class);

                    notifList.add(notif);
                }
                NotificationFriendsAdapter adapter = new NotificationFriendsAdapter(notifList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
 */
    }
}