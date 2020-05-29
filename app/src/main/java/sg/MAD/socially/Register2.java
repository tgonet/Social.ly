package sg.MAD.socially;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinner;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.androidbuts.multispinnerfilter.SpinnerListener;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

public class Register2 extends AppCompatActivity {

    Button Register;
    EditText Nickname, ShortDesc;
    ImageButton Gallerybutton, Camerabutton;

    FirebaseAuth auth;
    DatabaseReference reference;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReferenceFromUrl("gs://socially-943f3.appspot.com");

    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;
    String mUri;

    private static final int CAMERA_REQUEST_CODE = 1;

    private StorageReference mStorage;
    String Interest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        Register = findViewById(R.id.Register_register2);
        Nickname = findViewById(R.id.Nickname_register2);
        ShortDesc = findViewById(R.id.Shortdesc_register2);
        Gallerybutton = findViewById(R.id.Gallery_select_Register2);
        Camerabutton = findViewById(R.id.Camera_Register2);
        MultiSpinnerSearch Spinner = findViewById(R.id.Interest_register2);


        final List<String> list = Arrays.asList(getResources().getStringArray(R.array.Sports));
        final List<KeyPairBoolData> listArray0 = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i + 1);
            h.setName(list.get(i));
            h.setSelected(false);
            listArray0.add(h);
        }
        Interest = "";

/***
 * -1 is no by default selection
 * 0 to length will select corresponding values
 */
        Spinner.setItems(listArray0, -1, new SpinnerListener() {

            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {
                Interest = "";

                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        Log.i("Check", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                        Interest += items.get(i).getName() + ",";
                    }
                }
                if (Interest != ""){
                    Interest = Interest.substring(0, Interest.length() - 1);
                }
            }
        });


        auth = FirebaseAuth.getInstance();

        final String Email = getIntent().getStringExtra("Email");
        final String Name = getIntent().getStringExtra("Name");
        final String Password = getIntent().getStringExtra("Password");
        final String DOB = getIntent().getStringExtra("DOB");

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = Nickname.getText().toString();
                String shortdesc = ShortDesc.getText().toString();
                Register(Name, Email,Password,DOB,nickname,shortdesc);
            }
        });

        Gallerybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });

        Camerabutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openCamera();
            }
        });
    }


    public void Register(final String Name, final String Email, final String Password, final String DOB, final String Nickname, final String ShortDesc){
        auth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){  //Checks if task is successful then add the user data to firebase
                    FirebaseUser firebaseUser = auth.getCurrentUser();  //Current logged in user
                    String userid = firebaseUser.getUid();  //Logged in user's ID

                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userid); // address in the database to access (/User/$userid)

//                    Add info into hash map for register 2
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("Id", userid);
                    hashMap.put("Name", Name);
                    hashMap.put("NickName",Nickname);
                    hashMap.put("Password",Password);
                    hashMap.put("Email", Email);
                    hashMap.put("DOB",DOB);
                    hashMap.put("ShortDesc",ShortDesc);
                    hashMap.put("Interest",Interest);
                    hashMap.put("Friends", "");
                    hashMap.put("PendingFriends", "");


                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {   //Checks if current task is completed
                            if(task.isSuccessful()){
                                uploadImage();
                                Intent intent = new Intent(Register2.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish(); //Prevents user from coming back to this page
                            }
                            else{
                                Toast.makeText(Register2.this,"You cant register with this info!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(Register2.this,"You cant register with this info--",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    public void openCamera(){
        try {
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getBaseContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        if(imageUri != null){

            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "."
                    + getFileExtension(imageUri));
            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        mUri = downloadUri.toString();

                        FirebaseUser fuser = auth.getCurrentUser();
                        reference = FirebaseDatabase.getInstance().getReference("Users").child((fuser.getUid()));
                        HashMap<String,Object> map = new HashMap<>();
                        map.put("ImageURL",mUri);
                        reference.updateChildren(map);
                    }
                    else{
                        Toast.makeText(Register2.this,"Failed",Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Register2.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(Register2.this,"No image selected",Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            //Gallery Button
            imageUri = data.getData();

            if(uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(Register2.this,"Upload in progress",Toast.LENGTH_SHORT).show();
            }
            else{
                //uploadImage();
            }
        }

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){
            //Camera Button
            Uri uri = data.getData();

//            StorageReference filepath = storage.child("Photos").child(uri.getLastPathSegment());

        }
    }

}
