package com.example.fyp.Driver;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.R;
import com.google.android.gms.location.places.Place;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class ShowFullRequest extends AppCompatActivity {

    FirebaseUser mcurrentuser;
    String uid;
    String customerId=null;
    private TextView customerName, customerCnic, customerNumber, rcvrName, rcvrCnic, rcvrNumber, pickupLoc, deliveryLoc, date, time, payment;
    Button accept,reject;

    String dName,dCnic,dImage,dMobile,dLicense,dTrucktype;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_full_request);


        pickupLoc=findViewById(R.id.pickupLocation_tvC);
        deliveryLoc=findViewById(R.id.deliveryLocation_tvC);
        customerName=findViewById(R.id.customerNameC);
        customerCnic=findViewById(R.id.customerCnicC);
        customerNumber=findViewById(R.id.customerNumberC);
        rcvrName=findViewById(R.id.rcvrNameC);
        rcvrCnic=findViewById(R.id.rcvrCnicC);
        rcvrNumber=findViewById(R.id.rcvrNumberC);
        date=findViewById(R.id.dateC);
        time=findViewById(R.id.timeC);
        accept=findViewById(R.id.acceptRequest);
        reject=findViewById(R.id.rejectRequest);

        payment=findViewById(R.id.payment);




        mcurrentuser= FirebaseAuth.getInstance().getCurrentUser();
        uid= mcurrentuser.getUid();

        final String id=getIntent().getStringExtra("customerId");
        final String requestid=getIntent().getStringExtra("requestId");

        DatabaseReference driverInfo= FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(uid);
        driverInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    dImage=dataSnapshot.child("image").getValue(String.class);
                    dTrucktype=dataSnapshot.child("truckType").getValue(String.class);
                    dLicense=dataSnapshot.child("license").getValue(String.class);
                    dCnic=dataSnapshot.child("cnic").getValue(String.class);
                    dName=dataSnapshot.child("name").getValue(String.class);
                    dMobile=dataSnapshot.child("mobile").getValue(String.class);

                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        DatabaseReference show= FirebaseDatabase.getInstance().getReference().child("Requests").child(id).child(requestid);
        show.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                customerName.setText(dataSnapshot.child("customerName").getValue().toString());
                customerCnic.setText(dataSnapshot.child("customerCnic").getValue().toString());
                customerNumber.setText(dataSnapshot.child("customerNumber").getValue().toString());
                rcvrName.setText(dataSnapshot.child("rcvrName").getValue().toString());
                rcvrCnic.setText(dataSnapshot.child("rcvrCnic").getValue().toString());
                rcvrNumber.setText(dataSnapshot.child("rcvrNumber").getValue().toString());
                pickupLoc.setText(dataSnapshot.child("pickup").getValue().toString());
                deliveryLoc.setText(dataSnapshot.child("delivery").getValue().toString());

                if (dataSnapshot.child("paymentMethod").getValue().toString().equals("10"))
                payment.setText("PayMent  :   Cash Payment");
                else if (dataSnapshot.child("paymentMethod").getValue().toString().equals("10"))
                    payment.setText("PayMent  :   Online Payment");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference fromPath=FirebaseDatabase.getInstance().getReference().child("Requests").child(id).child(requestid);
                final DatabaseReference toPath=FirebaseDatabase.getInstance().getReference().child("WorkingRequests").child(requestid);
                final DatabaseReference requestsid=FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(uid);
                fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        toPath.setValue(dataSnapshot.getValue());
                        toPath.child("driverId").setValue(uid);
                        toPath.child("driverName").setValue(dName);
                        toPath.child("driverImage").setValue(dImage);
                        toPath.child("driverMobile").setValue(dMobile);
                        toPath.child("driverCnic").setValue(dCnic);
                        toPath.child("driverLicense").setValue(dLicense);
                        toPath.child("driverTruckType").setValue(dTrucktype);
                        fromPath.removeValue();


                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                DatabaseReference myDriver=FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(id).child("MyDrivers");
                myDriver.child(requestid).setValue(true);

                requestsid.child("customerIds").child(requestid).removeValue();
                requestsid.child("working").child(requestid).setValue(true);

             /*   Intent intent=new Intent(ShowFullRequest.this,DriverHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);*/
            }



        });



        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference deletedataFromDriver=FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(uid).child("customerIds").child(requestid);
                deletedataFromDriver.removeValue();
            }
        });


    }
}
