package sg.MAD.socially;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.DisplayMetrics;
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
        FriendList = new ArrayList<>();
        rv = findViewById(R.id.rv_newchat);
        rv.setLayoutManager(new GridLayoutManager(this,3));
        FriendListID = new ArrayList<>();

        Display();

        reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("Friends");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FriendListID = new ArrayList<>();
                String friendliststring = dataSnapshot.getKey();
                if(friendliststring.length() > 2){
                    String[] friendList = friendliststring.split(",");
                    for(String i : friendList){
                        FriendListID.add(i);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(FriendListID.size() != 0){
            reference = FirebaseDatabase.getInstance().getReference("Users");

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
                    adapter = new GridFriendAdapter(FriendList,getBaseContext());
                    rv.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void Display() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .6));
    }
}
