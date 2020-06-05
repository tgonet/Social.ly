package sg.MAD.socially;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Register2 extends AppCompatActivity {

    Button Register;
    EditText Nickname, ShortDesc;
    ImageButton Gallerybutton, Camerabutton;

    FirebaseAuth auth;
    DatabaseReference reference;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReferenceFromUrl("gs://socially-943f3.appspot.com");
    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 123;

    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;
    String mUri;
    private Uri filepath;
    String imageFilePath;

    private static final int CAMERA_REQUEST_CODE = 1;

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
        //Check for runtime permissions Above 6.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }

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
    //Capture Image from camera and  getting  as a file path
    public void openCamera(){
        try {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            if(cameraIntent.resolveActivity(getPackageManager()) != null){
                File photoFile = null;
                try{
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(photoFile != null){
                    Uri photoURI = FileProvider.getUriForFile(Register2.this, "sg.MAD.socially.provider", photoFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            photoURI);
                    startActivityForResult(cameraIntent,
                            CAMERA_REQUEST_CODE);
                }
            }
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
        if(imageUri == null) {
            //For updating default Image
            String urlDefault = "https://firebasestorage.googleapis.com/v0/b/socially-943f3.appspot.com/o/profile-placeholder.png?alt=media&token=de632eea-3125-44fb-af7d-a883a82d107c";
            FirebaseUser fuser = auth.getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference("Users").child((fuser.getUid()));
            HashMap<String,Object> map = new HashMap<>();
            map.put("ImageURL",urlDefault);
            reference.updateChildren(map);
            Log.w("Successful","upload"+urlDefault);

            //imageUri = Uri.parse("content://com.android.providers.media.documents/document/image%3A672111");
        }else {

            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "."
                    + getFileExtension(imageUri));
            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {

                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {

                        Uri downloadUri = task.getResult();
                        mUri = downloadUri.toString();

                        FirebaseUser fuser = auth.getCurrentUser();
                        reference = FirebaseDatabase.getInstance().getReference("Users").child((fuser.getUid()));
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("ImageURL", mUri);
                        reference.updateChildren(map);
                    } else {

                        Toast.makeText(Register2.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(Register2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }/*
        else{
            Toast.makeText(Register2.this,"No image selected",Toast.LENGTH_SHORT).show();
        }*/

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    //Creating Image file after getting captured from camera
    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName,        ".jpg",  storageDir  );

        imageFilePath = image.getAbsolutePath();
        imageUri=Uri.fromFile(image);

        return image;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

/*Put this log with check if data is null or not
else it will through error*/

        // Log.v("TESTINGGGGGGGG", " ReqC " + requestCode + " ResC" + resultCode + " Data " + data + " " + data.getData());

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            //Gallery Button
            imageUri = data.getData();
            Toast.makeText(Register2.this,"Gallery Upload: " + imageUri , Toast.LENGTH_SHORT).show();
            if(uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(Register2.this,"Gallery Upload in progress: " + imageUri ,Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
            //Camera Button
            //imageUri = Uri.parse(getRealPathFromURI(Uri.parse(String.valueOf(data.getData()))));

            /*If you want to display image to any imageview then put your code here*/

            if(uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(Register2.this,"Camera Image Upload in progress",Toast.LENGTH_SHORT).show();
            }

        }
    }
    //Check for runtime permissions
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(Register2.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) + ContextCompat
                    .checkSelfPermission(Register2.this,
                            Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale
                        (Register2.this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                        ActivityCompat.shouldShowRequestPermissionRationale
                                (Register2.this, Manifest.permission.CAMERA)) {

                    Snackbar.make(Register2.this.findViewById(android.R.id.content),
                            "Please Grant Permissions to upload profile photo",
                            Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    requestPermissions(
                                            new String[]{Manifest.permission
                                                    .READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                            PERMISSIONS_MULTIPLE_REQUEST);
                                }

                            }).show();
                } else {
                    requestPermissions(
                            new String[]{Manifest.permission
                                    .READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            PERMISSIONS_MULTIPLE_REQUEST);
                }
            } else {
                // write your logic code if permission already granted
            }
        }
    }
    //Request permissions
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST:
                if (grantResults.length > 0) {
                    boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(cameraPermission && readExternalFile)
                    {

                    } else {
                        Snackbar.make(Register2.this.findViewById(android.R.id.content),
                                "Please Grant Permissions to upload profile photo",
                                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(
                                                    new String[]{Manifest.permission
                                                            .READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                                    PERMISSIONS_MULTIPLE_REQUEST);
                                        }
                                    }
                                }).show();
                    }
                }
                break;
        }
    }
}
