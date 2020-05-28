package sg.MAD.socially;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    Button Continuebutton;
    EditText Name, Email, Password,DOB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Name = findViewById(R.id.Name_register);
        Password = findViewById(R.id.Password_register);
        Email = findViewById(R.id.Email_register);
        DOB = findViewById(R.id.DOB_register);
        Continuebutton = findViewById(R.id.Continue_register);

        Log.v("test", "testinggggg");

        Continuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Name.getText().toString();
                String password = Password.getText().toString();
                String email = Email.getText().toString();
                String dob = Email.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name) || TextUtils.isEmpty(dob)) {  //Prevent registering if fields are empty
                    Toast.makeText(Register.this, "All fields required", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(Register.this, Register2.class);
                    intent.putExtra("Name", Name.getText().toString());
                    intent.putExtra("Password", Password.getText().toString());
                    intent.putExtra("Email", Email.getText().toString());
                    intent.putExtra("DOB", DOB.getText().toString());
                    startActivity(intent);
                }
            }
        });



    }
}

