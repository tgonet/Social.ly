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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;

import sg.MAD.socially.Class.NotificationFriend;
import sg.MAD.socially.Class.User;
import sg.MAD.socially.R;
import sg.MAD.socially.ui.Friends.FriendsAdapter;

public class NotificationsFriendsFragment extends Fragment {
    View root;
    ImageView image;
    TextView content;
    TextView duration;
    DatabaseReference reference;
    String currentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_notifications_friends, container, false);
        image = root.findViewById(R.id.notification_friend_image);
        content = root.findViewById(R.id.notification_friend_content);
        duration = root.findViewById(R.id.notification_friend_duration);
        final RecyclerView recyclerView = root.findViewById(R.id.rv_notifications_friends);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final ArrayList notifList = new ArrayList<>();
        final NotificationFriendsAdapter adapter = new NotificationFriendsAdapter(notifList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        reference = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId).child("Notifications");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notifList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    NotificationFriend notif = snapshot.getValue(NotificationFriend.class);

                    notifList.add(notif);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return root;
    }
}