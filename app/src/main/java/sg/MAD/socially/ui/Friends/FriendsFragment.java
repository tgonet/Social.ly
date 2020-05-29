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
        //DisplayFindFriends();
        /*
        profile_pic = root.findViewById(R.id.imageView);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Log.d("Check", user.getName());
                Glide.with(FriendsFragment.this).load(user.getImageURL()).into(profile_pic); //set/resize image of profile pic
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/
        return root;
    }

    public ArrayList<String> getFriendList(DatabaseReference reference){
        final ArrayList<String> FriendList = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userFriends = dataSnapshot.getKey();
                if(userFriends.length() > 2){
                    String[] friendList = userFriends.split(",");
                    for(String i : friendList){
                        FriendList.add(i);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return FriendList;
    }

    public ArrayList<User> getUsers(){
        final ArrayList<User> userList = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    User user = snapshot.getValue(User.class);
                    userList.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return userList;
    }

    public ArrayList<User> GenerateFindFriends(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("Friends");
        ArrayList<String> userFriendList = getFriendList(ref);
        ArrayList<User> userList = getUsers();
        ArrayList<User> potentialFriendList = null;

        for (User u: userList) {
            if (userFriendList.contains(u.getId()) == false && u.getId() != user.getUid()) {
                potentialFriendList.add(u);
            }
        }
        return potentialFriendList;
    }

    public void DisplayFindFriends(){
        /* current user details */

        //current user Id
        final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //current user PendingFriends
        ref = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId).child("Pending Friends");
        final ArrayList<String> currUserPendingFriendList = getFriendList(ref);

        //current user Friends
        ref = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId).child("Friends");
        final ArrayList<String> currUserFriendList = getFriendList(ref);


        //potential friends to populate the frame layout
        final ArrayList<User> potentialFriendList = GenerateFindFriends();


        //buttons to accept or reject
        //addFriend = getView().findViewById(R.id.addfriend_yes);
        //notFriend = getView().findViewById(R.id.addfriend_no);


        //adapter for the potential friends' details
        adapter = new FriendsAdapter(getContext(), R.layout.fragment_home_addfriend, potentialFriendList);
        SwipeFlingAdapterView swipeContainer =(SwipeFlingAdapterView) getView().findViewById(R.id.frame);
        Log.d("test", "value of" +getView().findViewById(R.id.frame));

        //implementing the swipe function of the frames
        swipeContainer.setAdapter(adapter);
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
                User user = (User)o;
                String userId = user.getId();
                String pendingfriends = user.getPendingFriends();
                ArrayList <String> pendingFriendList = new ArrayList<>();
                if(pendingfriends.length() > 2){
                    String[] pending = pendingfriends.split(",");
                    for(String i : pending){
                        pendingFriendList.add(i);
                    }
                }
                String friend = user.getFriends();
                ArrayList <String> friendList = new ArrayList<>();
                if(friend.length() > 2){
                    String[] friends = friend.split(",");
                    for(String i : friends){
                        friendList.add(i);
                    }
                }

                boolean isPendingFriend = false;
                for (String i: currUserPendingFriendList){
                    if (i == userId){
                        currUserFriendList.add(i);
                        friendList.add(currentUserId);
                        pendingFriendList.remove(i);
                        isPendingFriend = true;
                        break;
                    }
                }
                if (isPendingFriend != true){
                    pendingFriendList.add(currentUserId);
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

}
