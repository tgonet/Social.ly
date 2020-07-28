package sg.MAD.socially;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import sg.MAD.socially.Class.Activity;
import sg.MAD.socially.Class.NotificationFriend;
import sg.MAD.socially.Class.User;

public class DisplaySelectedActivity extends AppCompatActivity {

TextView selected_Name_register;
TextView selected_act_location;
TextView selected_act_time;
TextView selected_activity_name;
TextView selected_act_desc;
TextView selected_act_date;
ImageView selected_act_picture;
ImageView selected_profile_image;
Button interested_button;

//join activity
FirebaseUser currentUser;
String activityid;
Activity selected;
String interest;

//notification
DatabaseReference refNotification;
DatabaseReference refNotification2;
DatabaseReference currentUserRef;
String currentUserId;

Intent intent;
Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_selected);

        intent = this.getIntent();
        bundle = intent.getExtras();

        selected = (Activity) bundle.getSerializable("selected");

        Log.d("I LOVE EUGUNESSS",selected.getInterest());

        //instantiate views
        selected_Name_register = findViewById(R.id.selected_Name_register);
        selected_act_location= findViewById(R.id.selected_act_location);
        selected_act_time = findViewById(R.id.selected_act_time);
        selected_activity_name = findViewById(R.id.selected_activity_name);
        selected_act_desc = findViewById(R.id.selected_act_desc);
        selected_act_date = findViewById(R.id.selected_act_date);
        selected_act_picture = findViewById(R.id.selected_act_picture);
        selected_profile_image = findViewById(R.id.selected_profile_image);
        interested_button = findViewById(R.id.interested_button);


        //display data of selected activity
        selected_Name_register.setText(selected.getName_register());
        selected_act_location.setText(selected.getAct_location());
        selected_act_time.setText(selected.getAct_time());
        selected_activity_name.setText(selected.getActivity_name());
        selected_act_desc.setText(selected.getAct_desc());
        selected_act_date.setText(selected.getAct_date());

        Picasso.get().load(selected.getAct_picture()).fit() .into(selected_act_picture);
        Picasso.get().load(selected.getProfile_image()).resize(600, 200) // resizes the image to these dimensions (in pixel)
                .centerInside() .into(selected_profile_image);


        interested_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InterestedButton();

            }
        });

    }

    private void InterestedButton() {

        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
        //retrieve selected activity's id
        interest = selected.getInterest();
        activityid = selected.getActivityid();

        //get current user id from firebase
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();




        //address firebase and put data into firebase
        FirebaseDatabase.getInstance().getReference().child("Activity").child(interest).child(activityid).child("Joined_users")
                .child(currentUserId).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getBaseContext(), "Successfully joined activity!", Toast.LENGTH_SHORT).show();

                    ValueEventListener getUserinfo = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String name = (String) snapshot.child("Name").getValue();
                            String imageURL = (String) snapshot.child("ImageURL").getValue();

                            //store notification information
                            NotificationFriend notif = new NotificationFriend();
                            //custom date format
                            final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                            //current date and time with format
                            final String timeNow = dateFormat.format(new Date());
                            notif.setImageURL(imageURL);
                            notif.setInfo(name + " has joined your activity!");
                            notif.setTime(timeNow);
                            refNotification = FirebaseDatabase.getInstance().getReference();
                            Log.d("USERS","HOST" + selected.getHost_id());
                            DatabaseReference notiRef = refNotification.child("Users").child(selected.getHost_id()).child("Activity_Notifications");
                            //push the notification object into firebase as a child
                            notiRef.push().setValue(notif);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    };
                    databaseRef.child(currentUserId)
                    .addValueEventListener(getUserinfo);
                }



                else {
                    Toast.makeText(getBaseContext(), "Failed to join activity!", Toast.LENGTH_SHORT).show();
                }
            }
            });


        //NotificationFriend notification = new NotificationFriend();

        //notification.setImageURL(user.getImageURL());
        //notification.setInfo(user.getDisplayName() + "has joined ");



    }



}