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

    DatabaseReference reference;
    ImageView profile_pic;
    ImageView profilepic;
    TextView name;
    TextView friendCount;
    TextView DateOfBirth;
    TextView ShortDesc;
    Button notFriend;
    Button addFriend;

    private FriendsViewModel friendsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        friendsViewModel =
                ViewModelProviders.of(this).get(FriendsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

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

    public ArrayList<String> getFriendList(FirebaseUser user){
        final ArrayList<String> FriendList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("Friends");

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
        reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
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
        ArrayList<String> userFriendList = getFriendList(user);
        ArrayList<User> userList = getUsers();
        ArrayList<User> potentialFriendList = null;

        for (User u: userList){
            for (String j: userFriendList){
                if (u.getId().contains(j) == false && u.getId() != user.getUid()){
                    potentialFriendList.add(u);
                }
            }
        }

        return potentialFriendList;
    }

    public void DisplayFindFriends(){
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        final ArrayList<User> potentialFriendList = GenerateFindFriends();

        addFriend = getView().findViewById(R.id.addfriend_yes);
        notFriend = getView().findViewById(R.id.addfriend_no);

        //FriendsAdapter = new FriendsAdapter(this, R.layout.fragment_home_addfriend, potentialFriendList);
        SwipeFlingAdapterView swipeContainer =(SwipeFlingAdapterView) getView().findViewById(R.id.frame);

        //swipeContainer.setAdapter(FriendsAdapter);
        swipeContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("Find Friends", "Removed object");
                potentialFriendList.remove(0);
                //FriendsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o) {
            }

            @Override
            public void onRightCardExit(Object o) {
                User user = (User)o;
                String userId = user.getId();
                String pendingfriends = user.getPendingFriends();
                String[] pendingfriendList = pendingfriends.split(",");
                //String currUserFriendList = currentUser.get

                boolean isPendingFriend;
                for (String i: pendingfriendList){
                    if (i == userId){
                       // pendingfriendList = ArrayUtils.remove(pendingfriendList,i);
                      //  currentUser
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
