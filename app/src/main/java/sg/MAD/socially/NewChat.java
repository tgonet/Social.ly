package sg.MAD.socially;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.DisplayMetrics;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.MAD.socially.Adapter.GridFriendAdapter;
import sg.MAD.socially.Class.User;
import sg.MAD.socially.ui.Friends.FriendsFragment;

public class NewChat extends AppCompatActivity {

    DatabaseReference reference;
    FirebaseUser user;
    ArrayList<User> FriendList;
    ArrayList<String> FriendListID;
    GridFriendAdapter adapter;
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);

        //Getting logged in user details
        user = FirebaseAuth.getInstance().getCurrentUser();

        //setting the path to reference to in firebase
        reference = FirebaseDatabase.getInstance().getReference("Users");

        FriendList = new ArrayList<>();
        FriendListID = new ArrayList<>();

        //Instantiates the adapter and telling it to display in a grid layout
        rv = findViewById(R.id.rv_newchat);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new GridFriendAdapter(FriendList, this);
        rv.setAdapter(adapter);

        //Resize the new activity to make it look like a popup
        Display();

        //Retrieve the friendlist of the current user
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                FriendListID = new ArrayList<>();

                //Check if user has friends
                if(user.getFriends() != null){
                    String friendliststring = user.getFriends();
                    //Check if the friendlist has any string inside as long
                    // as the string is not empty there is at least 1 friend
                    FriendListID = FriendsFragment.removeComma(friendliststring);
                    /*if (!friendliststring.isEmpty()) {
                        if (friendliststring.contains(",")) {
                            String[] friendList = friendliststring.split(",");
                            for (String i : friendList) {
                                FriendListID.add(i);
                            }
                        } else {
                            FriendListID.add(friendliststring);
                        }
                    }*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Adding the users to the friendlist to be displayed in the gridlayout
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FriendList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    //Check if user is already in the list
                    if (FriendListID.contains(user.getId())) {
                        FriendList.add(user);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void Display() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        //Width is .9 times of normal size and height is .65 times of normal size
        getWindow().setLayout((int) (width * .9), (int) (height * .6));
    }
}
