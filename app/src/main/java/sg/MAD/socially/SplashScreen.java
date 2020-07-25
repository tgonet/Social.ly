package sg.MAD.socially;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    int SPLASH_TIME = 2000; //This is 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set View to layout
        setContentView(R.layout.activity_splash_screen);

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
    }
}