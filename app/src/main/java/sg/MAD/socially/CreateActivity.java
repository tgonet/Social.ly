package sg.MAD.socially;

import android.Manifest;
import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class CreateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText ActivityName,Desc,Location,Time,Date;
    Button Createactivitybtn;
    ImageButton Camerabtn,Gallerybtn;
    Spinner Interest;
    FirebaseUser user;
    DatabaseReference reference;
    String interest  = "Gaming";
    String mUri;
    String imageFilePath;
    private StorageTask uploadTask;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReferenceFromUrl("gs://socially-943f3.appspot.com");
    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_activity);

        ActivityName = findViewById(R.id.new_activity_name);
        Desc = findViewById(R.id.new_desc);
        Location = findViewById(R.id.new_actlocation);
        Time = findViewById(R.id.new_acttime);
        Date = findViewById(R.id.new_actdate);
        Interest = findViewById(R.id.interest_activity);
        Gallerybtn = findViewById(R.id.Gallery_select_Createactivity);
        Camerabtn = findViewById(R.id.Camera_Createactivity);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.interest_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        Interest.setAdapter(adapter);

        Interest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                interest = parent.getItemAtPosition(position).toString();
                Toast.makeText(CreateActivity.this, interest, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();

        Createactivitybtn = findViewById(R.id.Create_activity_button);
        Createactivitybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateBtn();
                finish();
            }
        });

        Gallerybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });

        Camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int mYear ;
                int mMonth ;
                int mDay;
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int y=year;
                        /*Calender object starts month with 0 so month+1 is needed to get current month*/
                        int m=month+1;
                        int d=dayOfMonth;
                        /*set date to edit text*/
                        Date.setText(d+"/"+ m +"/" + year);
                    }
                }, mYear, mMonth, mDay);

                /*If you want to use minimum date to select from put setMinData(with the date)*/
                /*setMaxDate() is you dialog show the max date System.currentTimeMillis() is for current date*/
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
    }

    private void CreateBtn(){
        String activityname = ActivityName.getText().toString();
        String desc = Desc.getText().toString();
        String location = Location.getText().toString();
        String time = Time.getText().toString();
        String date = Date.getText().toString();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("activity_name", activityname);
        hashMap.put("act_desc",desc);
        hashMap.put("profile_image", user.getPhotoUrl().toString());
        hashMap.put("act_location", location);
        hashMap.put("act_time",time);
        hashMap.put("act_date",date);
        hashMap.put("Name_register",user.getDisplayName());
        hashMap.put("Interest",interest);


        reference = FirebaseDatabase.getInstance().getReference("Activity").child(interest).push();
        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    uploadImage(user.getDisplayName());
                    Toast.makeText(getBaseContext(),"Activity created",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getBaseContext(),"Activity failed to be created",Toast.LENGTH_SHORT).show();
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
                    Uri photoURI = FileProvider.getUriForFile(CreateActivity.this, "sg.MAD.socially.provider", photoFile);
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
            //For updating default Image
            String urlDefault = "https://firebasestorage.googleapis.com/v0/b/socially-943f3.appspot.com/o/profile-placeholder.png?alt=media&token=de632eea-3125-44fb-af7d-a883a82d107c";
            reference = FirebaseDatabase.getInstance().getReference("Activity").child(interest).child(Name);
            HashMap<String,Object> map = new HashMap<>();
            map.put("act_picture",urlDefault);
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

                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Activity").child(interest).child(reference.getKey());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("act_picture",mUri);
                        reference1.updateChildren(map);
                    } else {
                        Toast.makeText(CreateActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(CreateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(CreateActivity.this,"Gallery Upload: " + imageUri , Toast.LENGTH_SHORT).show();
            if(uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(CreateActivity.this,"Gallery Upload in progress: " + imageUri ,Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
            //Camera Button
            //imageUri = Uri.parse(getRealPathFromURI(Uri.parse(String.valueOf(data.getData()))));

            /*If you want to display image to any imageview then put your code here*/

            if(uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(CreateActivity.this,"Camera Image Upload in progress",Toast.LENGTH_SHORT).show();
            }

        }
    }
    //Check for runtime permissions
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(CreateActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) + ContextCompat
                    .checkSelfPermission(CreateActivity.this,
                            Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale
                        (CreateActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                        ActivityCompat.shouldShowRequestPermissionRationale
                                (CreateActivity.this, Manifest.permission.CAMERA)) {

                    Snackbar.make(CreateActivity.this.findViewById(android.R.id.content),
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
                        Snackbar.make(CreateActivity.this.findViewById(android.R.id.content),
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
