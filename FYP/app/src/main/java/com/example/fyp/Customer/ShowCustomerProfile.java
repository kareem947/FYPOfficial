package com.example.fyp.Customer;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class ShowCustomerProfile extends Fragment {

public static final int REQUEST_CALL=1;
    private TextView nameText, phoneText, cnicText;
    CircleImageView imageView;
    private StorageReference mStorageReference;
    Button editProfilePic;
    TextView editProfilePhone;
    TextView makeCall;
    String uid;
    private static final int GALLERY_PICK = 1;
    Uri filePath;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_show_customer_profile, container, false);
        nameText = view.findViewById(R.id.CProfileName);
        phoneText = view.findViewById(R.id.CProfilePhone);
        cnicText = view.findViewById(R.id.CProfileCnic);
        imageView = view.findViewById(R.id.showCustomerprofile_image);
        editProfilePic=view.findViewById(R.id.editProfilePic);
        editProfilePhone=view.findViewById(R.id.editProfilePhone);
        makeCall=view.findViewById(R.id.makeCall);
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();

        mStorageReference= FirebaseStorage.getInstance().getReference();

        showEveryThing();

        editProfilePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editPhoneNumber();
            }
        });

        makeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_DENIED){
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
                }
                else {

                    Log.d("whatisthis", "Thisis nothing");
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phoneText.getText()));
                    startActivity(callIntent);
                }
            }
        });


        editProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "Choose Image"), GALLERY_PICK);            }
        });




        return view;
    }
    public void showEveryThing(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nameText.setText(dataSnapshot.child("name").getValue(String.class));
                phoneText.setText(dataSnapshot.child("mobile").getValue(String.class));
                cnicText.setText(dataSnapshot.child("cnic").getValue(String.class));

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

    public void editPhoneNumber(){
        Toast.makeText(getContext(), "here to change phone", Toast.LENGTH_SHORT).show();

        LayoutInflater layoutInflater = LayoutInflater.from(ShowCustomerProfile.this.getActivity());
        View promptView = layoutInflater.inflate(R.layout.phone_number_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ShowCustomerProfile.this.getActivity());
        alertDialogBuilder.setView(promptView);
        final EditText editText = (EditText) promptView
                .findViewById(R.id.enterPhone);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Log.d("111database",editText.);
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(uid);
                        databaseReference.child("mobile").setValue(editText.getText().toString());
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==REQUEST_CALL){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phoneText.getText()));
                startActivity(callIntent);
            }
            else
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Log.d("ProfilePic    ",data.getData().toString());
            Uri imageUri = data.getData();
            final StorageReference imgPath = mStorageReference.child("CustomerProfileImages").child(uid + ".jpg");
            imgPath.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {

                        throw task.getException();

                    }
                   // Toast.makeText(Setting.this, "" + imgPath, Toast.LENGTH_SHORT).show();
                    return imgPath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downUri = task.getResult();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(uid);
                        databaseReference.child("image").setValue(downUri.toString());
                        Picasso.get().load(downUri.toString()).into(imageView);

                        //  Toast.makeText(Setting.this, downUri.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }
}
