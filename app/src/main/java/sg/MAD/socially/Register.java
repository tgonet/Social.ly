package sg.MAD.socially;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

        Continuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this,Register2.class);
                intent.putExtra("Name",Name.getText().toString());
                intent.putExtra("Password",Password.getText().toString());
                intent.putExtra("Email",Email.getText().toString());
                intent.putExtra("DOB",DOB.getText().toString());
                startActivity(intent);
            }
        });
    }
}
