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
    ArrayList<String> FriendList;
    /*
    ImageView profile_pic;
    ImageView profilepic;
    TextView name;
    TextView friendCount;
    TextView DateOfBirth;
    TextView ShortDesc;
    Button notFriend;
    Button addFriend;
     */
    FriendsAdapter adapter;

    private FriendsViewModel friendsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        friendsViewModel =
                ViewModelProviders.of(this).get(FriendsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        DisplayFindFriends(root);

        return root;
    }
    public ArrayList<String> getFriendList(DatabaseReference reference){
        //final ArrayList<String> FriendList = new ArrayList<>();
        Log.d("getFriendList","Method used");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FriendList = new ArrayList<>();
                User userFriends = dataSnapshot.getValue(User.class);
                Log.d("check", "onDataChange: ");
                String getTypeofFriend = userFriends.getFriends();
                if(getTypeofFriend.contains(",")){
                    String[] friendList = getTypeofFriend.split(",");
                    for(String i : friendList){
                        FriendList.add(i);
                        //Log.d("getFriendList", "friendList: " + FriendList);
                    }
                }
                else if(getTypeofFriend != "") {
                    FriendList.add(getTypeofFriend);
                    //Log.d("1 friend", "friendList: " + FriendList);
                }
                //Log.d("getFriendList", "friendList: " + FriendList);
                /*
                Log.d("check", "onDataChange: ");
                if(userFriends.contains(",")){
                    String[] friendList = userFriends.split(",");
                    for(String i : friendList){
                        FriendList.add(i);
                        Log.d("getFriendList", "friendList: " + FriendList);
                    }
                }
                else if(userFriends != "") {
                    FriendList.add(userFriends);
                    Log.d("1 friennd", "friendList: " + FriendList.get(0));
                }
                 */
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        //Log.d("GetFriends display list", String.valueOf(FriendList));
        return FriendList;
    }


    public ArrayList<String> getPendingFriendList(DatabaseReference reference){
        Log.d("getFriendList","Method used");
        final ArrayList<String> FriendList = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userFriends = dataSnapshot.getValue(User.class);
                String getTypeofFriend = userFriends.getPendingFriends();
                if(getTypeofFriend.contains(",")){
                    String[] friendList = getTypeofFriend.split(",");
                    for(String i : friendList){
                        FriendList.add(i);
                        //Log.d("getFriendList", "friendList: " + FriendList);
                    }
                }
                else if(getTypeofFriend != "") {
                    FriendList.add(getTypeofFriend);
                    //Log.d("1 friend", "friendList: " + FriendList);
                }
                //Log.d("check", "onDataChange: ");
                //Log.d("getFriendList", "friendList: " + FriendList);
                /*
                Log.d("check", "onDataChange: ");
                if(userFriends.contains(",")){
                    String[] friendList = userFriends.split(",");
                    for(String i : friendList){
                        FriendList.add(i);
                        Log.d("getFriendList", "friendList: " + FriendList);
                    }
                }
                else if(userFriends != "") {
                    FriendList.add(userFriends);
                    Log.d("1 friennd", "friendList: " + FriendList.get(0));
                }
                 */
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        //Log.d("GetFriends display list", String.valueOf(FriendList));
        return FriendList;
    }

    public ArrayList<User> getUsers(){
        Log.d("getUsers","Method used");
        final ArrayList<User> userList = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    User user = snapshot.getValue(User.class);
                    userList.add(user);
                    Log.d("User List display list", String.valueOf(userList));
                };
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //Log.d("getUsers display list: ", String.valueOf(userList));
        return userList;
    }

    public ArrayList<User> GenerateFindFriends(ArrayList<String> userFriendList){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        ArrayList<User> userList = getUsers();
        ArrayList<User> potentialFriendList = new ArrayList<>();

        for (User u: userList) {
            if (userFriendList.contains(u.getId()) == false && u.getId() != user.getUid()) {
                potentialFriendList.add(u);
            }
        }
        //Log.d("GeneratePotentialFriend", "Potential friend List:" + potentialFriendList);
        return potentialFriendList;
    }

    public void DisplayFindFriends(View v){
        /* current user details */

        //current user Id
        final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //current user PendingFriends
        ref = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId);
        //String whatFriend = "PendingFriends";
        //ref = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId).child("PendingFriends");
        final ArrayList<String> currUserPendingFriendList = getPendingFriendList(ref);
        Log.d("currUserPendingFriends", String.valueOf(currUserPendingFriendList));

        //current user Friends
        ref = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId);
        //ref = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId).child("Friends");
        final ArrayList<String> currUserFriendList = getFriendList(ref);
        Log.d("currUserFriendList", String.valueOf(currUserFriendList));


        //potential friends to populate the frame layout
        final ArrayList<User> potentialFriendList = GenerateFindFriends(currUserFriendList);
        Log.d("PotentialFriends", String.valueOf(potentialFriendList));


        //buttons to accept or reject
        //addFriend = getView().findViewById(R.id.addfriend_yes);
        //notFriend = getView().findViewById(R.id.addfriend_no);


        //adapter for the potential friends' details
        adapter = new FriendsAdapter(getContext(), R.layout.fragment_home_addfriend, potentialFriendList);
        SwipeFlingAdapterView swipeContainer =(SwipeFlingAdapterView) v.findViewById(R.id.frame);

        //implementing the swipe function of the frames
        swipeContainer.setAdapter(adapter);
        Log.d("Find Friends", "Adapter set");
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
                    /* getting user details of the current frame */
                    User user = (User) o;
                    String userId = user.getId();
                    String pendingfriends = user.getPendingFriends();
                    ArrayList<String> pendingFriendList = new ArrayList<>();

                    if(pendingfriends.contains(",")){
                        String[] pending = pendingfriends.split(",");
                        for(String i : pending){
                            pendingFriendList.add(i);
                        }
                    }
                    else if(pendingfriends != "") {
                        pendingFriendList.add(pendingfriends);
                    }

                    String friend = user.getFriends();
                    ArrayList<String> friendList = new ArrayList<>();
                    if(friend.contains(",")){
                        String[] friends = friend.split(",");
                        for(String i : friendList){
                            friendList.add(i);
                            Log.d("getFriendList", "friendList: " + friendList);
                        }
                    }
                    else if(friend != "") {
                        friendList.add(friend);
                    }
                    if (friendList.size() != 0) {
                        boolean isPendingFriend = false;
                        for (String i : currUserPendingFriendList) {
                            if (i == userId) {
                                currUserFriendList.add(i);
                                friendList.add(currentUserId);
                                pendingFriendList.remove(i);
                                isPendingFriend = true;
                                break;
                            }
                        }
                        if (isPendingFriend != true) {
                            pendingFriendList.add(currentUserId);
                        }
                    }
                }

                @Override
                public void onAdapterAboutToEmpty(int i) {

                }

                @Override
                public void onScroll(float v) {

                }
            });
        }
    }


    /*public void GenerateFindFriends(){
        ArrayList<User> FriendList = new ArrayList<>();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                /*for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        })
    } */
