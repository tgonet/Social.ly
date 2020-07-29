package sg.MAD.socially;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

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
        DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Pick data using data picker*/
                Calendar c = Calendar.getInstance();
                int mYear ;
                int mMonth ;
                int mDay;
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Register.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int y=year;
                        /*Calender object starts month with 0 so month+1 is needed to get current month*/
                        int m=month+1;
                        int d=dayOfMonth;
                        /*set date to edit text*/
                        DOB.setText(d+"/"+ m +"/" + year);
                    }
                }, mYear, mMonth, mDay);

                /*If you want to use minimum date to select from put setMinData(with the date)*/
                /*setMaxDate() is you dialog show the max date System.currentTimeMillis() is for current date*/
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        Continuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Name.getText().toString();
                String password = Password.getText().toString();
                String email = Email.getText().toString();
                String dob = DOB.getText().toString();

             /*   if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name) || TextUtils.isEmpty(dob)) {  //Prevent registering if fields are empty
                    Toast.makeText(Register.this, "All fields required", Toast.LENGTH_SHORT).show();
                }
                else {*/
                    //Check for validation
                    if (verifyField()){
                        Intent intent = new Intent(Register.this, Register2.class);
                        intent.putExtra("Name", Name.getText().toString());
                        intent.putExtra("Password", Password.getText().toString());
                        intent.putExtra("Email", Email.getText().toString());
                        intent.putExtra("DOB", DOB.getText().toString());
                        startActivity(intent);
                    }else{
                        Toast.makeText(Register.this, "All fields required", Toast.LENGTH_SHORT).show();
                    }

                //}
            }
        });
    }
    //Function for verification check
    public boolean verifyField(){
        boolean check = true;
        if (Email.getText().toString().trim().equals("") &&
                Password.getText().toString().trim().equals("") &&
                Name.getText().toString().trim().equals("") &&
                DOB.getText().toString().trim().equals("")){
            check = false;
            Email.setError("Email cannot be left blank");
            Password.setError("Password cannot be left blank");
            Name.setError("Name cannot be left blank");
            DOB.setError("DOB cannot be left blank");
        }

        if(Email.getText().toString().trim().length()<1){
            Email.setError("Email cannot be left blank");
            check = false;
        }else if(!Email.getText().toString().trim().contains("@")){
            Email.setError("Please enter a valid email");
            check = false;
        }
        if(Password.getText().toString().trim().length()<1){
            Password.setError("Please enter a valid password");
            check = false;
        }
        if(Name.getText().toString().trim().length()<1){
            Name.setError("Name cannot be left blank");
            check = false;
        }
        if(DOB.getText().toString().trim().length()<1){
            DOB.setError("Please Enter Date of Birth");
            check = false;
        }

        return check;
    }
}

