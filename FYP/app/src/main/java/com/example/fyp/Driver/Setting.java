package com.example.fyp.Driver;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.fyp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
/*
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
*/

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class Setting extends AppCompatActivity {

    private EditText nameText, phoneText, dlText, cnicText;
    CircleImageView imageView;
    private StorageReference mStorageReference;
    private RadioButton smallPickp, largeTruck, mediumTruck;
    Button saveProfile;
    private static final int GALLERY_PICK = 1;
    Uri filePath;
    String type,uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        nameText = findViewById(R.id.dSettingName);
        phoneText = findViewById(R.id.dSettingPhone);
        dlText = findViewById(R.id.dSettingLicense);
        cnicText = findViewById(R.id.dSettingCnic);
        imageView = findViewById(R.id.showprofile_image);
        saveProfile=findViewById(R.id.saveProfile);

        smallPickp =findViewById(R.id.dsettingSmallPickup);
        largeTruck = findViewById(R.id.dsettingLargeTruck);
        mediumTruck = findViewById(R.id.dsettingMediumTruck);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mStorageReference= FirebaseStorage.getInstance().getReference();

        //final String id = getIntent().getStringExtra("user_id");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nameText.setText(dataSnapshot.child("name").getValue(String.class));
                phoneText.setText(dataSnapshot.child("mobile").getValue(String.class));
                dlText.setText(dataSnapshot.child("license").getValue(String.class));
                cnicText.setText(dataSnapshot.child("cnic").getValue(String.class));
                type=dataSnapshot.child("truckType").getValue(String.class);
               if (type.equals("1")){
                   smallPickp.setChecked(true);
               }
                if (type.equals("2")){
                    mediumTruck.setChecked(true);
                }
                if (type.equals("3")){
                    largeTruck.setChecked(true);
                }
                String image = dataSnapshot.child("image").getValue().toString();

                //String s = dataSnapshot.child("image").getValue(String.class);
                if (!image.equals("default")) {
                   Picasso.get().load(image).into(imageView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void editProfilePic(View v) {

        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "Choose Image"), GALLERY_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            Uri imageUri = data.getData();
            //Log.d("imageUri",imageUri.toString());
       /*     CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this);*/

/*

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
*/
/*
            if (resultCode == RESULT_OK) {*/
            /* Uri resultUri = result.getUri();*/


            final StorageReference imgPath = mStorageReference.child("profileimages").child("RINlsZbn5mPryTOMkp2Y9L1H1rH2" + ".jpg");
            imgPath.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {

                        throw task.getException();

                    }
                    Toast.makeText(Setting.this, "" + imgPath, Toast.LENGTH_SHORT).show();
                    return imgPath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downUri = task.getResult();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child("RINlsZbn5mPryTOMkp2Y9L1H1rH2");
                        databaseReference.child("image").setValue(downUri.toString());
                        Toast.makeText(Setting.this, downUri.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

    public void saveProfile(View view){
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(uid);
        HashMap<String,Object> profile=new HashMap<>();

        if(smallPickp.isChecked())
            type = "1";
        else if(mediumTruck.isChecked())
            type = "2";
        else if(largeTruck.isChecked())
            type = "3";

        profile.put("name",nameText.getText().toString());
        profile.put("mobile",phoneText.getText().toString());
        profile.put("cnic",cnicText.getText().toString());
        profile.put("license",dlText.getText().toString());
        profile.put("truckType",type);

        databaseReference.updateChildren(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

    }

}


