package sg.MAD.socially;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.session.MediaSessionManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    Button Loginbutton, Backbutton;
    EditText Email, Password;
    TextView txtForgotPassword;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Loginbutton = findViewById(R.id.Login_login);
        Email = findViewById(R.id.Email_login);
        Password = findViewById(R.id.Password_login);
        Backbutton = findViewById(R.id.Back_login);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);

        auth = FirebaseAuth.getInstance();


        Loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = Password.getText().toString();
                String email = Email.getText().toString();

             /*   if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {  //Prevent a login attempt if fields are empty
                    Toast.makeText(Login.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                } else {*/
                    if (verifyField()){
                        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                  //  }

                }
            }
        });

        Backbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtForgotPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

    }
    //Function for verification check
    public boolean verifyField(){
        boolean check = true;
        if(Email.getText().toString().trim().equals("") && Password.getText().toString().trim().equals("") ){
            Email.setError("Email cannot be left blank");
            Password.setError("Please enter a valid password");
            check = false;
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

        return check;
    }
    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.forgot_password_dialog,null);
        builder.setView(dialogView);
        final TextInputEditText edtEmail = dialogView.findViewById(R.id.edtEmail);
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                    String email = edtEmail.getText().toString().trim();

                    if(!edtEmail.getText().toString().matches("")) {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dialog.dismiss();
                            Toast.makeText(Login.this, "Reset link sent to your email.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    } else {

                        dialog.dismiss();
                        Toast.makeText(Login.this, "Please enter email.", Toast.LENGTH_SHORT).show();
                    }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
