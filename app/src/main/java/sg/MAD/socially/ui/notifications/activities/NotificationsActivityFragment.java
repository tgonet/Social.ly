package sg.MAD.socially.ui.notifications.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import sg.MAD.socially.Class.NotificationFriend;
import sg.MAD.socially.R;

public class NotificationsActivityFragment extends Fragment {
    View root;
    ImageView image;
    TextView content;
    TextView duration;
    TextView noNotifications;
    DatabaseReference reference;
    String currentUserId;
    NotificationsActivityAdapter adapter;
    ArrayList notifList;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_notifications_activity, container, false);
        recyclerView = root.findViewById(R.id.activity_rv);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        notifList = new ArrayList<>();
        adapter = new NotificationsActivityAdapter(notifList); //pass list into adapter
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter); //display adapter's notifList in recyclerview

        reference = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId).child("Activity_Notifications");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notifList.clear(); //clear list so that there wont be repeated values

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    NotificationFriend notif = snapshot.getValue(NotificationFriend.class);
                    notifList.add(notif);
                }
                Collections.reverse(notifList);
                adapter.notifyDataSetChanged(); //this updates the adapter's notifList
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return root;
    }
}
