package sg.MAD.socially;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAccount extends AppCompatActivity implements View.OnClickListener{
    CircleImageView profileImage;
    TextView nameTxt;
    FirebaseAuth auth;
    DatabaseReference reference;
    String userid;
    ImageView profleImage;
    EditText nicnameEdt,interestsEdt,shortDescEdt;
    Button updateProfile;
    RelativeLayout editProfile;
    ImageButton backBtn;
    ImageButton Gallerybutton, Camerabutton;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReferenceFromUrl("gs://socially-943f3.appspot.com");
    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;
    String mUri;
    private Uri filepath;
    String imageFilePath;
	ProgressDialog dialog;
    AlertDialog alertDialog;
    private static final int CAMERA_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }
		dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait");
        profileImage = findViewById(R.id.profleImage);
        nicnameEdt = findViewById(R.id.nicnameEdt);
        interestsEdt = findViewById(R.id.interestsEdt);
        shortDescEdt = findViewById(R.id.shortDescEdt);
        updateProfile = findViewById(R.id.updateBtn);
        editProfile = findViewById(R.id.editProfile);
        backBtn = findViewById(R.id.backBtn);

        updateProfile.setOnClickListener(this);
        editProfile.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        profileImage.setOnClickListener(this);

        nameTxt = findViewById(R.id.nameTxt);
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();  //Current logged in user
         userid = firebaseUser.getUid();  //Logged in user's ID

        reference = FirebaseDatabase.getInstance().getReference("Users");
        getData();


    }

    private void getData(){
        reference.child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hideClickable();

                nameTxt.setText(""+dataSnapshot.child("Name").getValue(String.class));
                nicnameEdt.setText(""+dataSnapshot.child("NickName").getValue(String.class));
                interestsEdt.setText(""+dataSnapshot.child("Interest").getValue(String.class));
                shortDescEdt.setText(""+dataSnapshot.child("ShortDesc").getValue(String.class));
                       Glide.with(MyAccount.this).load(dataSnapshot.child("ImageURL").getValue(String.class))
                               .into(profileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MyAccount.this,"Error!",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.editProfile:
                editProfileClick();
                break;
            case R.id.profleImage:
                openDialog();
                break;
            case R.id.updateBtn:
                updateProfileData(nicnameEdt.getText().toString(),shortDescEdt.getText().toString(),interestsEdt.getText().toString(),nameTxt.getText().toString());
                break;

        }
    }

    private void editProfileClick(){
        updateProfile.setVisibility(View.VISIBLE);
        editProfile.setVisibility(View.GONE);
        nicnameEdt.setEnabled(true);
        interestsEdt.setEnabled(true);
        shortDescEdt.setEnabled(true);
    }
    private void hideClickable(){
        updateProfile.setVisibility(View.GONE);
        nicnameEdt.setEnabled(false);
        interestsEdt.setEnabled(false);
        shortDescEdt.setEnabled(false);
    }
    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyAccount.this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.button_dialog,null);
        builder.setView(dialogView);
        Gallerybutton = dialogView.findViewById(R.id.Gallery_select_Register2);
        Camerabutton = dialogView.findViewById(R.id.Camera_Register2);
        alertDialog = builder.create();
        alertDialog.show();
        Gallerybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                openImage();
            }
        });

        Camerabutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                alertDialog.dismiss();
                openCamera();
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
                    Uri photoURI = FileProvider.getUriForFile(MyAccount.this, "sg.MAD.socially.provider", photoFile);
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

    private void uploadImage(final String Name){
        if(imageUri == null) {
            Toast.makeText(MyAccount.this, "Something went wrong,please try again", Toast.LENGTH_SHORT).show();
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

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(Name)
                                .setPhotoUri(Uri.parse(mUri))
                                .build();

                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        firebaseUser.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("Checck", "User profile updated.");
                                        }
                                    }
                                });
                        FirebaseUser fuser = auth.getCurrentUser();
                        reference = FirebaseDatabase.getInstance().getReference("Users").child((fuser.getUid()));
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("ImageURL", mUri);
                        reference.updateChildren(map);
						dialog.dismiss();
					Toast.makeText(MyAccount.this,"Profile updated successfully",Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(MyAccount.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(MyAccount.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

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

        File image = File.createTempFile(imageFileName,".jpg",  storageDir);

        imageFilePath = image.getAbsolutePath();
        imageUri = Uri.fromFile(image);
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
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // profileImage.setPadding(2,2,2,2);

            //Toast.makeText(Register2.this,"Gallery Upload: " + imageUri , Toast.LENGTH_SHORT).show();
            if(uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(MyAccount.this,"Gallery Upload In Progress: " + imageUri ,Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
            //Camera Button
            //imageUri = Uri.parse(getRealPathFromURI(Uri.parse(String.valueOf(data.getData()))));

            /*If you want to display image to any imageview then put your code here*/
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(MyAccount.this,"Camera Image Upload in progress",Toast.LENGTH_SHORT).show();
            }

        }
    }
    //Check if camera
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MyAccount.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) + ContextCompat
                    .checkSelfPermission(MyAccount.this,
                            Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale (MyAccount.this, Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale (MyAccount.this, Manifest.permission.CAMERA)) {

                    Toast.makeText(MyAccount.this,"Please enable camera permissions",Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(MyAccount.this,"Please enable upload image permissions",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
    public void updateProfileData(final String Nickname, final String ShortDesc, final String Interest, final String Name){
		dialog.show();
        FirebaseUser firebaseUser = auth.getCurrentUser();  //Current logged in user
        String userid = firebaseUser.getUid();  //Logged in user's ID

        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid); // address in the database to access (/User/$userid)

//                    Add info into hash map for register 2
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reference.child("NickName").setValue(Nickname);
                reference.child("ShortDesc").setValue(ShortDesc);
                reference.child("Interest").setValue(Interest);
                if (imageUri!= null){
                    uploadImage(Name+"_"+System.currentTimeMillis());
                }else{
					dialog.dismiss();
					Toast.makeText(MyAccount.this,"Profile updated successfully",Toast.LENGTH_SHORT).show();
				}

                
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}