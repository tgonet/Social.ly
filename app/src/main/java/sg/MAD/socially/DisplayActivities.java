package sg.MAD.socially;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.MAD.socially.Adapter.DisplayActivitiesAdapter;
import sg.MAD.socially.Class.Activity;

public class DisplayActivities extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference reference;
    ArrayList<Activity> list;
    DisplayActivitiesAdapter adapter;
    Button Createacticitybtn;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_activities);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); //instantiates adapter to display as recylcerview
        Createacticitybtn = findViewById(R.id.button_create_activity);
        list = new ArrayList<>();

        //bring user to create activity
        Createacticitybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DisplayActivities.this,CreateActivity.class));
            }
        });

        list = new ArrayList<>(); //create array list to store activities
        String activity = getIntent().getStringExtra("activity");

        //retrieve activities
        reference = FirebaseDatabase.getInstance().getReference().child("Activity").child(activity);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear(); //clear underlying list
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    Activity a = dataSnapshot1.getValue(Activity.class);
                    list.add(a); //add activities retrieved from firebase into arraylist
                }
                adapter.notifyDataSetChanged(); //notify that new activities are added in
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DisplayActivities.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

        adapter = new DisplayActivitiesAdapter(DisplayActivities.this, list);
        recyclerView.setAdapter(adapter);

    }
}
