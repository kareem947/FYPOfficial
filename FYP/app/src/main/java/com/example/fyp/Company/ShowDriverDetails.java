package com.example.fyp.Company;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.Company.DriverRequests.CompanyDriverRequests;
import com.example.fyp.Driver.DriverRegister;
import com.example.fyp.R;
import com.example.fyp.SignIn;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowDriverDetails extends AppCompatActivity {


    private TextView nameText, phoneText, dlText, cnicText;
    CircleImageView imageView;
    private StorageReference mStorageReference;
    private RadioButton smallPickp, largeTruck, mediumTruck;
    Button acceptBtn, rejectBtn;
    String id="",email,password, uid;
    FirebaseAuth mAuth;
    private FirebaseUser mcurrentuser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_driver_details);

        nameText = findViewById(R.id.dSettingName);
        phoneText = findViewById(R.id.dSettingPhone);
        dlText = findViewById(R.id.dSettingLicense);
        cnicText =findViewById(R.id.dSettingCnic);
        imageView = findViewById(R.id.showprofile_image);
        acceptBtn=findViewById(R.id.companyAcceptDriverRequest);
        rejectBtn=findViewById(R.id.compantRejectDriverRequest);


        smallPickp =findViewById(R.id.dsettingSmallPickup);
        largeTruck = findViewById(R.id.dsettingLargeTruck);
        mediumTruck = findViewById(R.id.dsettingMediumTruck);



        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             registerDriver();
            }
        });

        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference frompath = FirebaseDatabase.getInstance().getReference().child("DriversRequests").child(id);
                frompath.removeValue();

            }
        });


        //final String id = getIntent().getStringExtra("user_id");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("DriversRequests");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dd:dataSnapshot.getChildren()){
                        id=dd.getKey();
                        email=dd.child("email").getValue(String.class);
                        password=dd.child("password").getValue(String.class);

                        nameText.setText(dd.child("name").getValue(String.class));
                        phoneText.setText(dd.child("mobile").getValue(String.class));
                        dlText.setText(dd.child("license").getValue(String.class));
                        cnicText.setText(dd.child("cnic").getValue(String.class));
                        String type = dd.child("truckType").getValue(String.class);
                    if (type.equals("1")) {
                        smallPickp.setChecked(true);
                    }
                    if (type.equals("2")) {
                        mediumTruck.setChecked(true);
                    }
                    if (type.equals("3")) {
                        largeTruck.setChecked(true);
                    }
                    String image = dd.child("image").getValue(String.class);

                    //String s = dataSnapshot.child("image").getValue(String.class);
                    if (!image.equals("default")) {
                        Picasso.get().load(image).into(imageView);
                    }
                }
            }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }



    public void registerDriver(){
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            mcurrentuser = FirebaseAuth.getInstance().getCurrentUser();
                            uid = mcurrentuser.getUid();

                            setDriverData();
                            Intent intent =new Intent(ShowDriverDetails.this, CompanyDriverRequests.class);
                            startActivity(intent);


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(ShowDriverDetails.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }



    DatabaseReference topath = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers");
    DatabaseReference frompath = FirebaseDatabase.getInstance().getReference().child("DriversRequests");
    public void setDriverData(){
        frompath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    topath.child(uid).setValue(dataSnapshot.child(id).getValue());
                    frompath.child(id).removeValue();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
