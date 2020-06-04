package sg.MAD.socially.ui.Friends;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import sg.MAD.socially.MainActivity;
import sg.MAD.socially.R;
import sg.MAD.socially.User;

public class FriendsFragment extends Fragment {

    DatabaseReference ref;
    DatabaseReference ref1;

    String currentUserId;
    ArrayList<String> FriendList;
    ArrayList<String> PendingFriendList;
    String friends;
    String pendingFriends;

    ArrayList<User> userList;
    ArrayList<User> potentialFriendList;

    View root;

    SwipeFlingAdapterView swipeContainer;
    /*
    Button notFriend;
    Button addFriend;
     */
    FriendsAdapter adapter;

    private FriendsViewModel friendsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        friendsViewModel =
                ViewModelProviders.of(this).get(FriendsViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);

        FriendList = new ArrayList<>();
        PendingFriendList = new ArrayList<>();
        potentialFriendList = new ArrayList<>();

        adapter = new FriendsAdapter(getContext(), R.layout.fragment_home_addfriend, potentialFriendList);
        swipeContainer = (SwipeFlingAdapterView) root.findViewById(R.id.frame);

        /*getting Current User Details */

        //Current User ID:
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("Current UserId", "" + currentUserId);

        //Current User Friends and Pending Friends:

        /* Getting all users details */

        //userList
        Log.d("getUsers","Method used");
        userList = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    User user = snapshot.getValue(User.class);
                    userList.add(user);
                    Log.d("User List display list", String.valueOf(userList));
                    if (user.getId() == currentUserId){
                        friends = user.getFriends();
                        pendingFriends = user.getPendingFriends();

                        //Current User FriendList
                        if (!friends.isEmpty()){
                            if (friends.contains(",")) {
                                String[] friendList = friends.split(",");
                                for (String i : friendList) {
                                    FriendList.add(i);
                                }
                            }
                            else{
                                FriendList.add(friends);
                            }
                        }
                        Log.d("Current User", "FriendsList: " + FriendList);

                        //Current User PendingFriendList
                        if (!pendingFriends.isEmpty()) {
                            if (pendingFriends.contains(",")) {
                                String[] pendingFriendList = pendingFriends.split(",");
                                for (String i : pendingFriendList) {
                                    PendingFriendList.add(i);
                                }
                            }
                            else {
                                PendingFriendList.add(pendingFriends);
                            }
                        }
                        Log.d("Current User", "PendingFriendsList: " + PendingFriendList);
                    }
                    //Potential Friends: Populate the frame layout
                    //adapter.notifyDataSetChanged();
                    //Log.d("User Potential Friends", "Potential friend List:" + potentialFriendList);
                    //DisplayFindFriends(root);
                };
                potentialFriendList.clear();

                for (User u: userList) {
                    if (FriendList.contains(u.getId()) == false && !currentUserId.equals(u.getId())) {
                        potentialFriendList.add(u);
                    }
                }
                Log.d("User Potential Friends", "Potential friend List:" + potentialFriendList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        DisplayFindFriends(root);
        return root;
    }

    public void DisplayFindFriends(View v) {
        //buttons to accept or reject
        //addFriend = getView().findViewById(R.id.addfriend_yes);
        //notFriend = getView().findViewById(R.id.addfriend_no);


        //adapter for the potential friends' details

        //implementing the swipe function of the frames
        //adapter.notifyDataSetChanged();
        swipeContainer.setAdapter(adapter);
        Log.d("Adapter", "Adapter set!" + adapter);
        swipeContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("Find Friends", "Removed object");
                potentialFriendList.remove(0);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o) {
            }

            @Override
            public void onRightCardExit(Object o) {
                // getting user details of the current frame
                User user = (User) o;
                String userId = user.getId();
                String pendingfriends = user.getPendingFriends();
                ArrayList<String> pendingFriendList = new ArrayList<>();

                if (pendingfriends.contains(",")) {
                    String[] pending = pendingfriends.split(",");
                    for (String i : pending) {
                        pendingFriendList.add(i);
                    }
                }
                else if (pendingfriends != "") {
                    pendingFriendList.add(pendingfriends);
                }

                String friend = user.getFriends();
                ArrayList<String> friendList = new ArrayList<>();
                if (friend.contains(",")) {
                    String[] friends = friend.split(",");
                    for (String i : friendList) {
                        friendList.add(i);
                        Log.d("getFriendList", "friendList: " + friendList);
                    }
                }

                else if (!friend.isEmpty()) {
                    friendList.add(friend);
                }

                boolean isPendingFriend = true;
                for (String i : PendingFriendList) {
                    if (i == userId) {
                        FriendList.add(i);
                        friendList.add(currentUserId);
                        PendingFriendList.remove(i);
                        isPendingFriend = false;
                        break;
                    }
                }
                if (isPendingFriend == true) {
                    pendingFriendList.add(currentUserId);
                    pendingfriends ="";
                    for (String i: pendingFriendList){
                        int count = 0;
                        if (count <= pendingFriendList.size() - 1) {
                            pendingfriends += i + ",";
                        }
                        else{
                            pendingfriends += i;
                        }
                        count++;
                        Log.d("Other user list", i + "added to friendList");
                    }
                    ref1 = FirebaseDatabase.getInstance().getReference();
                    ref1.child("Users").child(userId).child("PendingFriends").setValue(pendingfriends);
                    Log.d("Other user list", pendingfriends);
                }
                else{
                    friend ="";
                    for (String i: friendList){
                        int count = 0;
                        if (count <= friendList.size() - 1) {
                            friend += i + ",";
                        }
                        else{
                            friend += i;
                        }
                        Log.d("Other user list", i + "added to friendList");
                        count++;
                    }
                    ref1 = FirebaseDatabase.getInstance().getReference();
                    ref1.child("Users").child(userId).child("Friends").setValue(friend);
                    Log.d("Other user list", friend);


                    friends ="";
                    for (String i: FriendList){
                        int count = 0;
                        if (count <= FriendList.size() - 1) {
                            friend += i + ",";
                        }
                        else{
                            friend += i;
                        }
                        Log.d("Current user list", i + "added to friendList");
                        count++;
                    }
                    ref1.child("Users").child(currentUserId).child("Friends").setValue(friends);
                    Log.d("Current user list", friends);
                }
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {
                Log.d("Adapter","Going to be EMPTY!");
            }

            @Override
            public void onScroll(float v) {

            }
        });
    }
}
