package sg.MAD.socially;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    private List<String> UsersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        user_button =  findViewById(R.id.user_button);

        rv = findViewById(R.id.user_convo_rv);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();  //Logged in user
        UsersList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UsersList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);

                    if(chat.getSender().equals(fuser.getUid())){  //Check if this message belongs to the logged in user which he is the sender
                        UsersList.add(chat.getReceiver());
                    }
                    if(chat.getReceiver().equals(fuser.getUid())){  //Check if this message belongs to the logged in user which he is the receiver
                        UsersList.add(chat.getSender());
                    }
                }

                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        user_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Conversation.this,NewChat.class);
                startActivity(intent);
            }
        });
    }

    private void readChats(){
        User = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    for(String id : UsersList){
                        if(user.getId().equals(id)){
                            if(User.size() != 0){  //As the list is empty so there is no worry about displaying the same user twice
                                for(User user1 : User){
                                    if(!user.getId().equals(user1.getId())){  //Check if user is already in the User list
                                        User.add(user);
                                    }
                                }
                            }
                            else{
                                User.add(user);
                            }
                        }
                    }
                }
                adapter = new UserAdapter(User,getBaseContext());
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
