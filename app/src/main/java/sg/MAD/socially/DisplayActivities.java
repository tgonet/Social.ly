package sg.MAD.socially;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
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
    ArrayList<Activity> filtered;
    DisplayActivitiesAdapter adapter;
    Button Createacticitybtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_activities);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); //instantiates adapter to display as recylcerview
        Createacticitybtn = findViewById(R.id.button_create_activity);
        list = new ArrayList<>();

        //bring user to create activity
        Createacticitybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DisplayActivities.this, CreateActivity.class));
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
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Activity a = dataSnapshot1.getValue(Activity.class);
                    String key = dataSnapshot1.getKey();
                    a.setActivityId(key);

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

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

    }


    private void filter(String text) {
        filtered = new ArrayList<>();


        for (Activity activity : list){
            if (activity.getActivity_name().toLowerCase().equals((text)) || activity.getActivity_name().toLowerCase().contains(text)){
                filtered.add(activity);
            }
        }
        adapter.filteredList(filtered);
    }
}
