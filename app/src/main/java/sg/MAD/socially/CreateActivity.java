package sg.MAD.socially;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class CreateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText ActivityName,Desc,Location,Time,Date;
    Button Createactivitybtn;
    Spinner Interest;
    FirebaseUser user;
    DatabaseReference reference;
    String interest;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_activity);

        ActivityName = findViewById(R.id.new_activity_name);
        Desc = findViewById(R.id.new_desc);
        Location = findViewById(R.id.new_actlocation);
        Time = findViewById(R.id.new_acttime);
        Date = findViewById(R.id.new_actdate);
        Interest = findViewById(R.id.interest_activity);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.interest_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        Interest.setAdapter(adapter);

        user = FirebaseAuth.getInstance().getCurrentUser();

        Createactivitybtn = findViewById(R.id.Create_activity_button);
        Createactivitybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateBtn();
            }
        });
    }

    private void CreateBtn(){
        String activityname = ActivityName.getText().toString();
        String desc = Desc.getText().toString();
        String location = Location.getText().toString();
        String time = Time.getText().toString();
        String date = Date.getText().toString();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("activity_name", activityname);
        hashMap.put("act_desc",desc);
        hashMap.put("profile_image", user.getPhotoUrl().toString());
        hashMap.put("act_location", location);
        hashMap.put("act_time",time);
        hashMap.put("act_date",date);
        hashMap.put("Name_register",user.getDisplayName());

        reference = FirebaseDatabase.getInstance().getReference("Activity").child("Interest");
        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getBaseContext(),"Activity created",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getBaseContext(),"Activity failed to be created",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String interest = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
