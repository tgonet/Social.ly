package sg.MAD.socially;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import sg.MAD.socially.Notifications.Token;

public class SplashScreen extends AppCompatActivity {

    int SPLASH_TIME = 2000; //This is 2 seconds
    FirebaseUser fuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set View to layout
        setContentView(R.layout.activity_splash_screen);

        //Getting logged in user details
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        //Display SplashScreen for a period of time (in this case is 2 seconds)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Move to next page
                Intent mySuperIntent = new Intent(SplashScreen.this,Start.class);
                startActivity(mySuperIntent);

                //Exiting the app when back button pressed from Home page which is ActivityHome
                finish();

            }
        }, SPLASH_TIME);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                updateToken(instanceIdResult.getToken());
            }
        });
    }

    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(fuser.getUid()).setValue(token1);
    }
}