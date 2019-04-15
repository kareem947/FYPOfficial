package com.example.fyp.Company;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.Company.DriverRequests.CompanyDriverRequests;
import com.example.fyp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowCompanyAllDriversDetails extends AppCompatActivity {

    private TextView nameText, phoneText, dlText, cnicText,vehicletxt;
    CircleImageView imageView;
    private StorageReference mStorageReference;
    String id="",email,password, uid;
    FirebaseAuth mAuth;
    private FirebaseUser mcurrentuser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_company_all_drivers_details);

        uid=getIntent().getStringExtra("idofDriver");
        nameText = findViewById(R.id.dSettingName);
        phoneText = findViewById(R.id.dSettingPhone);
        dlText = findViewById(R.id.dSettingLicense);
        cnicText =findViewById(R.id.dSettingCnic);
        imageView = findViewById(R.id.showprofile_image);
        vehicletxt = findViewById(R.id.dVehicleType);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dd) {
                if (dd.exists()) {

                        email=dd.child("email").getValue(String.class);
                        password=dd.child("password").getValue(String.class);

                        nameText.setText(dd.child("name").getValue(String.class));
                        phoneText.setText(dd.child("mobile").getValue(String.class));
                        dlText.setText(dd.child("license").getValue(String.class));
                        cnicText.setText(dd.child("cnic").getValue(String.class));
                        String type = dd.child("truckType").getValue(String.class);
                        if (type.equals("1")) {
                            vehicletxt.setText("Small Vehicle");                        }
                        if (type.equals("2")) {
                            vehicletxt.setText("Medium Vehicle");
                        }
                        if (type.equals("3")) {
                            vehicletxt.setText("Large Vehicle");
                        }
                        String image = dd.child("image").getValue(String.class);

                        //String s = dataSnapshot.child("image").getValue(String.class);
                        if (!image.equals("default")) {
                            Picasso.get().load(image).into(imageView);
                        }
                    }
                }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }
}



























