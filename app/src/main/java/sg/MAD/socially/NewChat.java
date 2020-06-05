package sg.MAD.socially;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.GridLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class NewChat extends AppCompatActivity {

    DatabaseReference reference;
    DatabaseReference reference1;
    FirebaseUser user;
    ArrayList<User> FriendList;
    ArrayList<String> FriendListID;
    GridFriendAdapter adapter;
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        FriendList = new ArrayList<>();
        rv = findViewById(R.id.rv_newchat);
        rv.setLayoutManager(new GridLayoutManager(this,3));
        FriendListID = new ArrayList<>();

        Display();

        //Retreive the friendlist of the current user
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                FriendListID = new ArrayList<>();
                String friendliststring = user.getFriends();
                //Check if the friendlist has any string inside as long
                // as the string is not empty there is at least 1 friend
                if(friendliststring.length() > 2){
                    if(friendliststring.contains(",")){
                        String[] friendList = friendliststring.split(",");
                        for(String i : friendList){
                            FriendListID.add(i);
                        }
                    }
                    else{
                        FriendListID.add(friendliststring);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FriendList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    if(FriendListID.contains(user.getId())){
                        FriendList.add(user);
                    }

                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        adapter = new GridFriendAdapter(FriendList,this);
        rv.setAdapter(adapter);
    }

    //Resize the new activity to make it look like a popup
    public void Display() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .6));
    }
}
