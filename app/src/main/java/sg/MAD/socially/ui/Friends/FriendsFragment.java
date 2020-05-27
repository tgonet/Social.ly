package sg.MAD.socially.ui.Friends;

import android.os.Bundle;
import android.service.autofill.Dataset;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import sg.MAD.socially.R;
import sg.MAD.socially.User;

public class FriendsFragment extends Fragment {

    DatabaseReference reference;
    ImageView profile_pic;

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
                Log.d("Check", user.getImageURL());
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

    public ArrayList<String> getUsersId(){
        final ArrayList<String> userList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String user = dataSnapshot.getKey();
                userList.add(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return userList;
    }

    public ArrayList<String> GenerateFindFriends(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ArrayList<String> userFriendList = getFriendList(user);
        ArrayList<String> userList = getUsersId();
        ArrayList<String> potentialFriendList = null;

        for (String i: userList){
            for (String j: userFriendList){
                if (i != j){
                    potentialFriendList.add(i);
                }
            }
        }
        return potentialFriendList;
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
