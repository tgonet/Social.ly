package sg.MAD.socially.ui.Friends;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sg.MAD.socially.MainActivity;
import sg.MAD.socially.Class.Notification;
import sg.MAD.socially.Message;
import sg.MAD.socially.Notifications.APIService;
import sg.MAD.socially.Notifications.Client;
import sg.MAD.socially.Notifications.Data;
import sg.MAD.socially.Notifications.MyResponse;
import sg.MAD.socially.Notifications.Sender;
import sg.MAD.socially.Notifications.Token;
import sg.MAD.socially.R;
import sg.MAD.socially.Class.User;

public class FriendsFragment extends Fragment {

    //Reference to firebase
    DatabaseReference ref;
    DatabaseReference reference;
    DatabaseReference ref1;
    DatabaseReference ref2;
    DatabaseReference ref3;
    DatabaseReference ref4;
    DatabaseReference refNotification;

    //All users
    ArrayList<User> allUsersList;

    //Current Users
    String currentUserId;
    String currFriends;
    ArrayList<String> currPendingFriendList;
    ArrayList<String> currFriendList;
    ArrayList<User> currPotentialFriendList;

    View root;

    FloatingActionButton notFriend;
    FloatingActionButton addFriend;

    FriendsAdapter adapter;

    //External library
    SwipeFlingAdapterView swipeContainer;

    //Service for notification
    APIService apiService;

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
        currPotentialFriendList = new ArrayList<>();

        //Setting up the adapter and external library for each card (i.e. user)
        adapter = new FriendsAdapter(getContext(), R.layout.fragment_home_addfriend, currPotentialFriendList);
        swipeContainer = (SwipeFlingAdapterView) root.findViewById(R.id.frame);

        /*Getting All Users and Current User Details from Firebase */

        //Current User ID:
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("Current UserId", "" + currentUserId);

        //Instantiates API
        apiService = Client.getCilent("https://fcm.googleapis.com/").create(APIService.class);

        //Populate allUsersList
        // Use id to get current user's friends
        ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allUsersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //Populating each user in allUsersList
                    User user = snapshot.getValue(User.class);
                    allUsersList.add(user);

                    //Check if user is current user by Id
                    if (user.getId().equals(currentUserId)) {
                        currFriends = user.getFriends();

                        //Populate Current User FriendList
                        if (!currFriends.isEmpty()) {
                            //More than 1 friend
                            if (currFriends.contains(",")) {
                                String[] friendList = currFriends.split(",");
                                for (String i : friendList) {
                                    currFriendList.add(i);
                                }
                            }
                            //1 friend
                            else {
                                currFriendList.add(currFriends);
                            }
                        }
                        Log.d("Current User", "FriendsList: " + currFriendList);
                    }
                    Log.d("All Users: ", String.valueOf(allUsersList));
                }

                //Clearing list so that there are no duplicates of a user
                currPotentialFriendList.clear();

                //Populating currPotentialFriendList for the cards
                for (User u : allUsersList) {
                    if (!currFriendList.contains(u.getId()) && !currentUserId.equals(u.getId())
                            && !u.getPendingFriends().contains(currentUserId)) {
                        Log.d("Check", String.valueOf(u.getPendingFriends().contains(currentUserId)));
                        currPotentialFriendList.add(u);
                        Log.d("Potential friend", u.getName());
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


        return root;
    }

    public void DisplayFindFriends(final View v) {
        //Set adapter to the swipe function to display each card (consisting of potential friend details)
        swipeContainer.setAdapter(adapter);
        Log.d("Adapter", "Adapter set!" + adapter);

        //set text to notify user that there are no new potential friends
        final TextView noNewUsers = (TextView) v.findViewById(R.id.addfriends_none);

        //Set a FlingListener to respond to the action based on direction of swipe
        swipeContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            //This method is executed on every swipe
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("Find Friends", "Removed object");
                currPotentialFriendList.remove(0);//so that the user can see the next card
                if (currPotentialFriendList.isEmpty()){
                    noNewUsers.setText("There's no one left.\nWhy not chat with your new friends? ");
                }
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
                final User user = (User) o;
                SwipeRight(user, v); //Pass in the displayed user that is swiped right and View v to SwipeRight method
            }


            @Override
            public void onAdapterAboutToEmpty(int i) {
                Log.d("Adapter", "Going to be EMPTY!");
            }

            @Override
            public void onScroll(float v) {

            }
        });
    }

    public void SwipeRight(final User user, final View v) {
        Log.d("Right Swipe", user.getId());

        // getting displayed users' details
        final String userId = user.getId();
        String pendingFriends = user.getPendingFriends();
        String friend = user.getFriends();

        //Instantiate ArrayList of Pending Friends and Friends
        final ArrayList<String> pendingFriendList = new ArrayList<>();
        final ArrayList<String> friendList = new ArrayList<>();

        //Populating pendingFriendList only when it is not empty
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

        //Populating FriendList only when it is not empty
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

        //Instantiating the ArrayList of current user's PendingFriends
        final ArrayList<String> currPendingFriendList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    User u = snapshot.getValue(User.class);

                    //Check if User u is the current user by Id
                    if (u.getId().equals(currentUserId)) {
                        String currPendingFriends = u.getPendingFriends();

                        //Populate Current User PendingFriendList only if it is not empty
                        if (!currPendingFriends.isEmpty()) {
                            //More than 1 friend
                            if (currPendingFriends.contains(",")) {
                                String[] pendingFriendList = currPendingFriends.split(",");
                                for (String i : pendingFriendList) {
                                    currPendingFriendList.add(i);
                                    Log.d("Current User", "Pending Friend: " + i);
                                }
                            }
                            //1 friend
                            else {
                                currPendingFriendList.add(currPendingFriends);
                                Log.d("Current User", "Pending Friend: " + currPendingFriends);
                            }
                        }
                    }
                }

                //Check if the adding of friends is mutual.

                // If yes, currPendingFriendList.contains(userId) is true
                if (currPendingFriendList.contains(userId)) {
                    currFriendList.add(userId);
                    friendList.add(currentUserId);
                    currPendingFriendList.remove(userId);
                    sendNotification(userId);

                    /* DISPLAY USER */

                    //Change display user's friendList (Add current user's Id)
                    String newfriend = "";
                    int count = 0;
                    for (String i : friendList) {
                        if (count != 0) {
                            newfriend += "," + i;
                        } else {
                            newfriend += i;
                        }
                        Log.d("Other user list", i + "added to friendList");
                        count++;
                    }

                    //Firebase: Update display users' pendingFriends
                    ref2 = FirebaseDatabase.getInstance().getReference();
                    ref2.child("Users").child(userId).child("Friends").setValue(newfriend);
                    Log.d("Other user list", newfriend);

                    /* CURRENT USER */

                    //Change current user's pendingFriendList (Remove display user's Id)
                    String newcurrPendingFriends = "";
                    int count1 = 0;
                    for (String i : currPendingFriendList) {
                        if (count1 != 0) {
                            newcurrPendingFriends += "," + i;
                        } else {
                            newcurrPendingFriends += i;
                        }
                        count1++;
                        Log.d("Current user list", i + "added to friendList");
                    }

                    //Firebase: Update current users' pendingFriends
                    ref3 = FirebaseDatabase.getInstance().getReference();
                    ref3.child("Users").child(currentUserId).child("PendingFriends").setValue(newcurrPendingFriends);
                    Log.d("Current user list", newcurrPendingFriends);

                    //Change current user's FriendList (Add display user's Id)
                    String newcurrFriends = "";
                    int count2 = 0;
                    for (String i : currFriendList) {
                        if (count2 != 0) {
                            newcurrFriends += "," + i;
                        } else {
                            newcurrFriends += i;
                        }
                        Log.d("Current user list", i + "added to friendList");
                        count2++;
                    }

                    //Firebase: Update display current users' Friends
                    ref4 = FirebaseDatabase.getInstance().getReference();
                    ref4.child("Users").child(currentUserId).child("Friends").setValue(newcurrFriends);
                    Log.d("Current user list", newcurrFriends);

                    //Create Alert Message for current user to start chatting with display user
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

                    /* Custom View */
                    View alertView = LayoutInflater.from(v.getContext()).inflate(R.layout.fragment_home_alert, null);

                    //Add current user profile picture
                    ImageView currUserPic = (ImageView) alertView.findViewById(R.id.newfriend_user);
                    String getCurrUserPic = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();
                    Glide.with(alertView).load(getCurrUserPic).into(currUserPic);

                    //Add display user profile picture
                    ImageView otherUserPic = (ImageView) alertView.findViewById(R.id.newfriend_friend);
                    Glide.with(alertView).load(user.getImageURL()).into(otherUserPic);

                    TextView alertMsg = (TextView) alertView.findViewById(R.id.newfriend_msg);
                    alertMsg.setText(user.getName() + " and you are now friends. Click the \"Say Hello\" button to start a new conversation!");


                    alert.setTitle("You have a new friend!")
                            .setPositiveButton("Say Hello!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getContext(), Message.class);
                                    intent.putExtra("userid", userId);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("Sweet!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setView(alertView);
                    alert.show();

                    //Update new friend's notifications in the Notifications page
                    Notification notification = new Notification();
                    notification.setInfo(user.getName() + " is now your friend!");
                    notification.setTime(Calendar.getInstance());
                    refNotification = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference notifRef = refNotification.child("Users").child(userId).child("Notifications");
                    notifRef.push().setValue(notification);
                }

                // If no, currPendingFriendList.contains(userId) is false
                else {
                    //Add current user to display users' pendingFriends
                    pendingFriendList.add(currentUserId);
                    String newpendingFriends = "";
                    int count3 = 0;
                    for (String i : pendingFriendList) {
                        if (count3 != 0) {
                            newpendingFriends += "," + i;
                        } else {
                            newpendingFriends += i;
                        }
                        count3++;
                    }

                    //Firebase: Update display users' pendingFriends
                    ref1 = FirebaseDatabase.getInstance().getReference();
                    ref1.child("Users").child(userId).child("PendingFriends").setValue(newpendingFriends);
                    Log.d("Other user list", newpendingFriends);

                    Toast.makeText(v.getContext(), "You are added to " + user.getName() + "'s pending friend list", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(final String receiver) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(currentUserId,  "Start chatting now!", "Someone swiped right!",
                            receiver,"Swiperight");

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().success != 1){
                                            Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}