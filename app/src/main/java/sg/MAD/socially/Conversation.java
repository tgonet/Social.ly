package sg.MAD.socially;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import sg.MAD.socially.Adapter.UserAdapter;
import sg.MAD.socially.Class.Chat;
import sg.MAD.socially.Class.User;
import sg.MAD.socially.Notifications.Token;

public class Conversation extends AppCompatActivity {

    private RecyclerView rv;
    private UserAdapter adapter;
    ArrayList<sg.MAD.socially.Class.User> User;
    Set<String> Finaluserstringlist;
    Button user_button;
    FirebaseUser fuser;
    DatabaseReference reference;

    private List<String> UserstringidList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        //Instantiates the button to view new friends to chat with
        user_button = findViewById(R.id.user_button);

        //Instantiates the adapter and telling it to display vertically
        rv = findViewById(R.id.user_convo_rv);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //Retrieve logged in user details
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        UserstringidList = new ArrayList<>();
        User = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chats");

        adapter = new UserAdapter(User, getBaseContext());
        rv.setAdapter(adapter);

        //Retrieve the userid that the user messages
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserstringidList.clear();

                //Extract the userid of whoever this user texted
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);

                    //Check if this message belongs to the logged in user which he is the sender
                    if (chat.getSender().equals(fuser.getUid())) {
                        //Gets the userid of the person he is texting
                        UserstringidList.add(chat.getReceiver());
                    }

                    //Check if this message belongs to the logged in user which he is the receiver
                    if (chat.getReceiver().equals(fuser.getUid())) {
                        //Gets the userid of the person he receive text from
                        UserstringidList.add(chat.getSender());
                    }
                }
                //Keep only unique userid as some will be repeated
                Finaluserstringlist = new LinkedHashSet<>(UserstringidList);
                //Retrieve all the details of user that has a chat record with user
                DisplayCurrentChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Setting the user button to go to new activity
        user_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Conversation.this, NewChat.class);
                startActivity(intent);
            }
        });
    }


    private void DisplayCurrentChats() {

        reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User.clear();
                //Populates the Userlist
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    for (String id : Finaluserstringlist) {
                        if (user.getId().equals(id)) {
                            User.add(user);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
