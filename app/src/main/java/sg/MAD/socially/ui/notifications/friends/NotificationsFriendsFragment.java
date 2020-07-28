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
import java.util.Collection;
import java.util.Collections;

import sg.MAD.socially.Class.NotificationFriend;
import sg.MAD.socially.Class.User;
import sg.MAD.socially.MainActivity;
import sg.MAD.socially.R;
import sg.MAD.socially.ui.Friends.FriendsAdapter;

public class NotificationsFriendsFragment extends Fragment {

    View root;
    ImageView image;
    TextView content;
    TextView duration;
    TextView noNotifications;
    DatabaseReference reference;
    String currentUserId;
    NotificationFriendsAdapter adapter;
    ArrayList notifList;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_notifications_friends, container, false);
        image = root.findViewById(R.id.notification_friend_image);
        content = root.findViewById(R.id.notification_friend_content);
        duration = root.findViewById(R.id.notification_friend_duration);
        recyclerView = root.findViewById(R.id.rv_notifications_friends);
        noNotifications = root.findViewById(R.id.nonotifications);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        notifList = new ArrayList<>();
        adapter = new NotificationFriendsAdapter(notifList); //pass list into adapter
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter); //display adapter's notifList in recyclerview

        reference = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId).child("Notifications");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notifList.clear(); //so that there are no duplicates
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    NotificationFriend notif = snapshot.getValue(NotificationFriend.class);
                    notifList.add(0, notif); //add notif to the front of the list.
                }
                if (notifList.size() == 0){
                    noNotifications.setText("You have no notifications."); //display text stating that there are no notifications
                }
                adapter.notifyDataSetChanged(); //this updates the adapter's notifList
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return root;
    }
}