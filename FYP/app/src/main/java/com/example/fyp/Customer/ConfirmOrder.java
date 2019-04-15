package com.example.fyp.Customer;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class ConfirmOrder extends AppCompatActivity {

    String cName, cCnic, cNumber,ordername, rName, rCnic, rNumber, pickup, delivery, uid, paymentMethod,cusImage;
    int mYear, mMonth, mDay, mHour, mMinute;
    Button order_Vehicle;
    double pickupLat, pickUpLon, dropOffLat, dropOffLon;
    boolean driverfound = false;
    int count = 0;
    ArrayList<String> ids;
    private TextView price,payment, orderName,customerName,vehicle, customerCnic, customerNumber, rcvrName, rcvrCnic, rcvrNumber, pickupLoc, deliveryLoc, date, time;
    private String requestId;
    private int radius = 2;
    private String driverFoundId;
    String truckType;
    int estimatedFair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        pickupLoc = findViewById(R.id.pickupLocation_tvC);
        deliveryLoc = findViewById(R.id.deliveryLocation_tvC);
        customerName = findViewById(R.id.customerNameC);
        customerCnic = findViewById(R.id.customerCnicC);
        payment=findViewById(R.id.paymentMethod);
        customerNumber = findViewById(R.id.customerNumberC);
        price = findViewById(R.id.estimatedprice);
        rcvrName = findViewById(R.id.rcvrNameC);
        orderName = findViewById(R.id.orderName);
        rcvrCnic = findViewById(R.id.rcvrCnicC);
        vehicle=findViewById(R.id.truckordred);
        rcvrNumber = findViewById(R.id.rcvrNumberC);
        date = findViewById(R.id.dateC);
        time = findViewById(R.id.timeC);
        order_Vehicle = findViewById(R.id.order_Vehicle);

        ids=new ArrayList<>();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        cusImage=getIntent().getStringExtra("cusImage");
        cName = getIntent().getStringExtra("cName");
        cCnic = getIntent().getStringExtra("cCnic");
        cNumber = getIntent().getStringExtra("cNumber");
        rName = getIntent().getStringExtra("rName");
        rCnic = getIntent().getStringExtra("rCnic");
        ordername=getIntent().getStringExtra("orderName");
        rNumber = getIntent().getStringExtra("rNumber");
        truckType=getIntent().getStringExtra("truckType");
        paymentMethod=getIntent().getStringExtra("paymentMethod");
        pickup = getIntent().getStringExtra("pickup");
        delivery = getIntent().getStringExtra("delivery");
        mYear = getIntent().getIntExtra("year", 0);
        mMonth = getIntent().getIntExtra("month", 0);
        mDay = getIntent().getIntExtra("day", 0);
        mHour = getIntent().getIntExtra("hour", 0);
        mMinute = getIntent().getIntExtra("minute", 0);
        pickupLat = getIntent().getDoubleExtra("pickuplat", 0.0);
        pickUpLon = getIntent().getDoubleExtra("pickuplon", 0.0);
        dropOffLat = getIntent().getDoubleExtra("dropofflat", 0.0);
        dropOffLon = getIntent().getDoubleExtra("dropofflon", 0.0);
        Location pickupLocation=new Location("");
        Location dropOffLocation=new Location("");
        pickupLocation.setLatitude(pickupLat);
        pickupLocation.setLongitude(pickUpLon);
        dropOffLocation.setLatitude(dropOffLat);
        dropOffLocation.setLongitude(dropOffLon);
        double distance= pickupLocation.distanceTo(dropOffLocation)/1000;
        estimatedFair = (int) (distance*100);
        completeFields();
    }
    public void completeFields() {
        customerName.setText("Name: "+cName);
        customerCnic.setText("Cnic: "+cCnic);
        customerNumber.setText("Mobile: "+cNumber);
        rcvrCnic.setText("Cnic: "+rCnic);
        orderName.setText("Oders  :" +ordername);
        rcvrName.setText("Name: "+rName);
        rcvrNumber.setText("mobile: "+rNumber);
        pickupLoc.setText("Pickup: "+pickup);
        price.setText(Integer.toString(estimatedFair));
        deliveryLoc.setText("DropOff: "+delivery);
        date.setText("Date: "+mDay + "-" + mMonth + "-" + mYear);
        time.setText("Time: "+mHour + ":" + mMinute);
        if (truckType.equals("1"))
            vehicle.setText("small Pickup");
        else if (truckType.equals("2"))
            vehicle.setText("medium truck");
        else if (truckType.equals("3"))
            vehicle.setText("large truck");

        if (paymentMethod.equals("10"))
            payment.setText("Payment Method   : Cash Payment");
        else if (paymentMethod.equals("20"))
            payment.setText("Payment Method   : Online Payment");

    }

    public void orderVehicle(View view) {


        getClosestLocation();

                final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Requests").child(userId);
                HashMap<String, String> requests = new HashMap<>();
                requests.put("customerName", cName);
                requests.put("customerCnic", cCnic);
                requests.put("customerNumber", cNumber);
                requests.put("rcvrName", rName);
                requests.put("rcvrCnic", rCnic);
                requests.put("rcvrNumber", rNumber);
                requests.put("pickup", pickup);
                requests.put("delivery", delivery);
                requests.put("customerImage", cusImage);
                requests.put("orderId", ordername);
                requests.put("customerId", userId);
                requests.put("paymentMethod", paymentMethod);
                requests.put("fair", Integer.toString(estimatedFair));

                requestId = databaseReference.push().getKey();
                databaseReference = databaseReference.child(requestId);

                databaseReference.setValue(requests);
                GeoFire geoFire = new GeoFire(databaseReference);
                geoFire.setLocation("pickuplocation", new GeoLocation(pickupLat, pickUpLon), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        if (error != null) {
                            System.err.println("There was an error saving the location to GeoFire: " + error);
                        } else {
                            System.out.println("Location saved on server successfully!");
                        }
                    }
                });
                geoFire.setLocation("dropofflocation", new GeoLocation(dropOffLat, dropOffLon), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        if (error != null) {
                            System.err.println("There was an error saving the location to GeoFire: " + error);
                        } else {
                            System.out.println("Location saved on server successfully!");
                        }
                    }
                });
            }

    public void getClosestLocation() {
        DatabaseReference driverlocation = FirebaseDatabase.getInstance().getReference().child("DriversAvailable").child(truckType);
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers");


        GeoFire geoFire = new GeoFire(driverlocation);
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(pickupLat, pickUpLon), radius);
        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(final String key, GeoLocation location) {


                if (count < 15) {

                    driverFoundId = key;
                    database.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                if (dataSnapshot.child(key).child("suspended").getValue(String.class).equals("no")){
                                    ids.add(driverFoundId);
                                    Log.d("Id123",""+driverFoundId);
                                    count++;
                                    driverfound = true;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
            @Override
            public void onKeyExited(String key) {
            }
            @Override
            public void onKeyMoved(String key, GeoLocation location) {
            }
            @Override
            public void onGeoQueryReady() {
                if (radius < 20){
                if (count < 15) {
                    radius++;
                    count = 0;
                    driverfound=false;
                    getClosestLocation();

                }
                }
            else {
                if (count>0){
                    if (ids.size()>0) {
                        sendRequest();
                    }
                }
                else
                    Toast.makeText(ConfirmOrder.this, "We Have No Driver In Your Area", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    ArrayList<RatingSort> newList= new ArrayList<>();
    public void sendRequest() {
        for (int i = 0; i<ids.size() ; i++){

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(ids.get(i));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    newList.add(new RatingSort(dataSnapshot.getKey(),Math.round(dataSnapshot.child("rating").getValue(Float.class))));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       }
       sendRequest1();
        DatabaseReference driverReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers");

        for (int i = 0; i<ids.size()/2 ; i++){
            driverReference.child(ids.get(i)).child("customerIds").child(requestId).child("requestId").setValue(uid);
        }
    }




    public void sendRequest1() {
        Collections.sort(newList, new Comparator<RatingSort>() {
            @Override
            public int compare(RatingSort o1, RatingSort o2) {
                return Integer.valueOf(o1.rating).compareTo(o2.rating);
            }

        });


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers");
        for (int i = 0; i < ids.size(); i++) {
            Log.d("customerKiID", uid);
            Log.d("customerKiID", ids.get(i));


            databaseReference.child(ids.get(i)).child("customerIds").child(requestId).child("requestId").setValue(uid);


        }
    }

}
