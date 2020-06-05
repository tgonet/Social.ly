package sg.MAD.socially.ui.Friends;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;

import sg.MAD.socially.R;
import sg.MAD.socially.User;

public class FriendsFragment extends Fragment {

    //Reference to firebase
    DatabaseReference ref;
    DatabaseReference ref1;
    DatabaseReference ref2;
    DatabaseReference ref3;
    DatabaseReference ref4;

    //All users
    ArrayList<User> allUsersList;

    //Current Users
    String currentUserId;
    String currFriends;
    String currPendingFriends;
    ArrayList<String> currFriendList;
    ArrayList<String> currPendingFriendList;
    ArrayList<User> currPotentialFriendList;

    View root;

    FloatingActionButton notFriend;
    FloatingActionButton addFriend;

    FriendsAdapter adapter;

    //External library
    SwipeFlingAdapterView swipeContainer;

    private FriendsViewModel friendsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //for the fragment view
        friendsViewModel =
                ViewModelProviders.of(this).get(FriendsViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);

        //instantiating all users ArrayList
        allUsersList = new ArrayList<>();

        //instantiating current User's ArrayList
        currFriendList = new ArrayList<>();
        currPendingFriendList = new ArrayList<>();
        currPotentialFriendList = new ArrayList<>();

        //Setting up the adapter and external library for each card (i.e. user)
        adapter = new FriendsAdapter(getContext(), R.layout.fragment_home_addfriend, currPotentialFriendList);
        swipeContainer = (SwipeFlingAdapterView) root.findViewById(R.id.frame);


        /*getting Current User Details */

        //Current User ID:
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("Current UserId", "" + currentUserId);

        //Firebase: Populate allUsersList
        // Use id to get current user's friends and pending friends
        ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    //Populating each user in allUsersList
                    User user = snapshot.getValue(User.class);
                    allUsersList.add(user);

                    //Check if user is current user by Id
                    if (user.getId() == currentUserId){
                        currFriends = user.getFriends();
                        currPendingFriends = user.getPendingFriends();

                        //Populate Current User FriendList
                        if (!currFriends.isEmpty()){
                            //More than 1 friend
                            if (currFriends.contains(",")) {
                                String[] friendList = currFriends.split(",");
                                for (String i : friendList) {
                                    currFriendList.add(i);
                                }
                            }
                            //1 friend
                            else{
                                currFriendList.add(currFriends);
                            }
                        }
                        Log.d("Current User", "FriendsList: " + currFriendList);

                        //Populate Current User PendingFriendList
                        if (!currPendingFriends.isEmpty()) {
                            //More than 1 friend
                            if (currPendingFriends.contains(",")) {
                                String[] pendingFriendList = currPendingFriends.split(",");
                                for (String i : pendingFriendList) {
                                    currPendingFriendList.add(i);
                                }
                            }
                            //1 friend
                            else {
                                currPendingFriendList.add(currPendingFriends);
                            }
                        }
                        Log.d("Current User", "PendingFriendsList: " + currPendingFriendList);
                    }
                    Log.d("All Users: ", String.valueOf(allUsersList));
                    //Potential Friends: Populate the frame layout
                    //adapter.notifyDataSetChanged();
                    //Log.d("User Potential Friends", "Potential friend List:" + potentialFriendList);
                    //DisplayFindFriends(root);
                };

                //Clearing list so that there are no duplicates of a user
                currPotentialFriendList.clear();

                //Populating currPotentialFriendList
                for (User u: allUsersList) {
                    if (currFriendList.contains(u.getId()) == false && !currentUserId.equals(u.getId())) {
                        currPotentialFriendList.add(u);
                    }
                }
                Log.d("User Potential Friends", "Potential friend List:" + currPotentialFriendList);

                //Updates the users displayed
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //Method: use external library for display
        DisplayFindFriends(root);

        //buttons to accept or reject
        addFriend = root.findViewById(R.id.addfriend_yes);
        notFriend = root.findViewById(R.id.addfriend_no);
/*
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
 */
        return root;
    }

    public void DisplayFindFriends(View v) {

        //Set adapter to the swipe function to display each card (consisting of potential friend details)
        swipeContainer.setAdapter(adapter);
        Log.d("Adapter", "Adapter set!" + adapter);

        //Set a FlingListener to respond to the action based on direction of swipe
        swipeContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            //This method is executed on every swipe
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("Find Friends", "Removed object");
                currPotentialFriendList.remove(0);
                adapter.notifyDataSetChanged();
            }

            //Swipe LEFT: Maybe Next Time...
            @Override
            public void onLeftCardExit(Object o) {
            }

            //Swipe RIGHT: Let's Be Friends!
            @Override
            public void onRightCardExit(Object o) {

                //Instantiating the displayed user as an object
                User user = (User) o;

                // getting displayed users' details
                String userId = user.getId();
                String pendingFriends = user.getPendingFriends();
                String friend = user.getFriends();

                //Instantiate ArrayList of Pending Friends and Friends
                ArrayList<String> pendingFriendList = new ArrayList<>();
                ArrayList<String> friendList = new ArrayList<>();

                //Populating pendingFriendList
                if (!pendingFriends.isEmpty()) {
                    //More than 1 friend
                    if (pendingFriends.contains(",")) {
                        String[] pending = pendingFriends.split(",");
                        for (String i : pending) {
                            pendingFriendList.add(i);
                        }
                    }
                    //1 friend
                    else {
                        pendingFriendList.add(pendingFriends);
                    }
                }

                //Populating FriendList
                if (!friend.isEmpty()) {
                    //More than 1 friend
                    if (friend.contains(",")) {
                        String[] friends = friend.split(",");
                        for (String i : friends) {
                            friendList.add(i);
                            Log.d("getFriendList", "friendList: " + friendList);
                        }
                    }
                    //1 friend
                    else {
                        friendList.add(friend);
                    }
                }

                //Check if the adding of friends is mutual.
                // If yes, isPendingFriend is false.
                // If no, isPendingFriend is true.

                if(currPendingFriendList.contains(userId)){
                        currFriendList.add(userId);
                        friendList.add(currentUserId);
                        currPendingFriendList.remove(userId);
                }
                else{
                    pendingFriendList.add(currentUserId);
                    pendingFriends = "";
                    for (String i : pendingFriendList) {
                        int count = 0;
                        if (count != 0) {
                            pendingFriends += "," + i;
                        }
                        else {
                            pendingFriends += i;
                        }
                        count++;
                    }
                }
                    //Firebase: Update display users' pendingFriends
                    ref1 = FirebaseDatabase.getInstance().getReference();
                    ref1.child("Users").child(userId).child("PendingFriends").setValue(pendingFriends);
                    Log.d("Other user list", pendingFriends);
                }


                    /* DISPLAY USER */

                    //Change display user's friendList (Add current user's Id)
                    String newfriends =""; //Ensure that there is no duplicates of Id
                    for (String i: friendList){
                        int count = 0;
                        if (count != 0) {
                            newfriends += "," + i;
                        }
                        else{
                            newfriends += i;
                        }
                        Log.d("Other user list", i + "added to friendList");
                        count++;
                    }
                    //Firebase: Update display users' pendingFriends
                    ref2 = FirebaseDatabase.getInstance().getReference();
                    ref2.child("Users").child(userId).child("Friends").setValue(newfriends);
                    Log.d("Other user list", newfriends);


                    /* CURRENT USER */

                    //Change current user's pendingFriendList (Remove display user's Id)
                    String newcurrPendingFriends = ""; //Ensure that there is no duplicates of Id
                    for (String i : currPendingFriendList) {
                        int count = 0;
                        if (count != 0) {
                            newcurrPendingFriends += "," + i;
                        } else {
                            newcurrPendingFriends += i;
                        }
                        count++;
                        Log.d("Current user list", i + "added to friendList");
                    }
                    //Firebase: Update current users' pendingFriends
                    ref3 = FirebaseDatabase.getInstance().getReference();
                    ref3.child("Users").child(currentUserId).child("PendingFriends").setValue(newcurrPendingFriends);
                    Log.d("Current user list", newcurrPendingFriends);

                    //Change current user's FriendList (Remove display user's Id)
                    String newcurrFriends =""; //Ensure that there is no duplicates of Id
                    for (String i: currFriendList){
                        int count = 0;
                        if (count != 0) {
                            newcurrFriends += "," + i;
                        }
                        else{
                            newcurrFriends += i;
                        }
                        Log.d("Current user list", i + "added to friendList");
                        count++;
                    }
                    //Firebase: Update display current users' Friends
                    ref4 = FirebaseDatabase.getInstance().getReference();
                    ref4.child("Users").child(currentUserId).child("Friends").setValue(newcurrFriends);
                    Log.d("Current user list", newcurrFriends);
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
