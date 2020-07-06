package sg.MAD.socially;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import sg.MAD.socially.Class.Activity;

public class DisplaySelectedActivity extends AppCompatActivity {

TextView selected_Name_register;
TextView selected_act_location;
TextView selected_act_time;
TextView selected_activity_name;
TextView selected_act_desc;
TextView selected_act_date;
ImageView selected_act_picture;
ImageView selected_profile_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_selected);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        Activity selected = (Activity) bundle.getSerializable("selected");

        selected_Name_register = (TextView) findViewById(R.id.selected_Name_register);
        selected_act_location= (TextView) findViewById(R.id.selected_act_location);
        selected_act_time = (TextView) findViewById(R.id.selected_act_time);
        selected_activity_name = (TextView) findViewById(R.id.selected_activity_name);
        selected_act_desc = (TextView) findViewById(R.id.selected_act_desc);
        selected_act_date = (TextView) findViewById(R.id.selected_act_date);
        selected_act_picture = (ImageView) findViewById(R.id.selected_act_picture);
        selected_profile_image = (ImageView) findViewById(R.id.selected_profile_image);

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

    }

}