package sg.MAD.socially;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Conversation extends AppCompatActivity {

    private RecyclerView rv;
    private UserAdapter adapter;
    ArrayList<User> User;
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
        User = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    for (String id : UserstringidList) {
                        if (user.getId().equals(id)) {
                            //As the list is empty so there is no worry about displaying the same user twice
                            if (User.size() != 0) {
                                for (User user1 : User) {
                                    //Check if user is already in the User list
                                    if (!user1.getId().equals(id)) {
                                        User.add(user);
                                    }
                                }
                            } else {
                                User.add(user);
                            }
                        }
                    }
                }
                adapter = new UserAdapter(User, getBaseContext());
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
