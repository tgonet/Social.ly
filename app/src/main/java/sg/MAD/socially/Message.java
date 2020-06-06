package sg.MAD.socially;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Message extends AppCompatActivity {

    CircleImageView profile_pic;
    TextView username;
    EditText message;
    ImageButton send;
    RecyclerView rv;
    List<Chat> chatList;

    FirebaseUser fuser;
    DatabaseReference reference;

    Intent intent;
    MessageAdapter messageadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        //Instantiates the views
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        profile_pic = findViewById(R.id.profile_image);
        username = findViewById(R.id.username_Message);
        message = findViewById(R.id.messaage);
        send = findViewById(R.id.send);

        //Instantiates the adapter and telling it to display vertically
        rv = findViewById(R.id.rv_message);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager((this));
        linearLayoutManager.setStackFromEnd(true);  //populate the recyclerview from the bottom instead
        rv.setLayoutManager(linearLayoutManager);
        messageadapter = new MessageAdapter(chatList, Message.this);
        rv.setAdapter(messageadapter);

        //Retreive the userid of the person that the user selected
        intent = getIntent();
        final String userid = intent.getStringExtra("userid");

        //Getting logged in user details
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        //setting the path to reference to in firebase
        reference = FirebaseDatabase.getInstance().getReference();

        //Setting the send button to send the text to firebase
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = message.getText().toString();
                if (!msg.equals("")) {
                    //send the text to firebase
                    sendMessage(fuser.getUid(), userid, msg);
                } else {
                    Toast.makeText(Message.this, "Error in sending message", Toast.LENGTH_SHORT).show();
                }
                message.setText("");
            }
        });

        //Retrieve friend details to be displayed at toolbar
        reference.child("Users").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getImageURL().equals("default")) {
                    profile_pic.setImageResource(R.mipmap.ic_launcher);
                } else {
                    //Set/resize image of profile pic
                    Glide.with(Message.this).load(user.getImageURL()).into(profile_pic);
                }

                //Displays all the messages between both users
                readMessage(fuser.getUid(), userid, user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String sender, String receiver, String message) {
        HashMap<String, Object> hashmap = new HashMap<>();
        hashmap.put("sender", sender);
        hashmap.put("receiver", receiver);
        hashmap.put("message", message);

        //upload the current message to firebase
        reference.child("Chats").push().setValue(hashmap);
    }

    private void readMessage(final String myid, final String userid, final String imageurl) {
        chatList = new ArrayList<>();

        //Populates the chatlist
        reference.child("Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    //Checks if this message is send between the 2 users
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
                        chatList.add(chat);
                    }
                }
                //Displays the newly updated messages
                messageadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
