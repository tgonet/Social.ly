package sg.MAD.socially;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAccount extends AppCompatActivity {
CircleImageView profileImage;
TextView data;
    FirebaseAuth auth;
    DatabaseReference reference;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        profileImage = findViewById(R.id.profleImage);
        data = findViewById(R.id.textView5);
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();  //Current logged in user
         userid = firebaseUser.getUid();  //Logged in user's ID

        reference = FirebaseDatabase.getInstance().getReference("Users");
        getData();


    }

    private void getData(){
        reference.child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String txt =   dataSnapshot.child("Name").getValue(String.class)+" "+
                        dataSnapshot.child("NickName").getValue(String.class)+" "+
                        dataSnapshot.child("Email").getValue(String.class)+" "+
                        dataSnapshot.child("DOB").getValue(String.class)+" "+
                        dataSnapshot.child("ShortDesc").getValue(String.class)+" "+
                        dataSnapshot.child("Interest").getValue(String.class);

                       data.setText(txt);
                       Glide.with(MyAccount.this).load(dataSnapshot.child("ImageURL").getValue(String.class))
                               .into(profileImage);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MyAccount.this,"Error!",Toast.LENGTH_SHORT).show();
            }
        });

    }
}