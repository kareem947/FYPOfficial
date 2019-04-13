package com.example.fyp.Customer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.fyp.R;
import com.example.fyp.directionhelpers.TaskLoadedCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowCustomerCompletedOrder extends AppCompatActivity implements OnMapReadyCallback , TaskLoadedCallback {
    public GoogleMap mMap;

    String requestId;
    double pickupLocationLat, dropoffLocationLat, pickupLocationLon, dropoffLocationLon;
    Location pickupLocation, dropoffLocation ;

    LatLng pickup,dropoff;

    TextView pickupLocationtxt,dropOffLocationtxt,driverNametxt,driverMobiletxt,datetxt,rcvrNametxt,rcvrMobiletxt;
    CircleImageView imageView;
    RatingBar ratingBar;

    private ScrollView mScrollView;
    DatabaseReference data;
    String uid,dId;
    int ratingSum = 0;
    float ratingTotal= 0;
    float ratingAverage=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_customer_completed_order);

        mScrollView = findViewById(R.id.scrollView);
        pickupLocationtxt=findViewById(R.id.pickupLocation);
        dropOffLocationtxt=findViewById(R.id.dropOffLocation);
        datetxt=findViewById(R.id.dateTime);
        driverNametxt=findViewById(R.id.driverName);
        rcvrNametxt=findViewById(R.id.rcvrName);
        rcvrMobiletxt=findViewById(R.id.rcvrMobile);
        driverMobiletxt=findViewById(R.id.driverMobile);
        imageView=findViewById(R.id.driverPic);
        ratingBar=findViewById(R.id.ratingBar);
        requestId=getIntent().getStringExtra("requestId");
        data = FirebaseDatabase.getInstance().getReference().child("CompletedRequests").child(requestId);

        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, final float rating, boolean fromUser) {
                data.child("myRating").setValue(rating).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DatabaseReference gotoDriverCompleted=FirebaseDatabase.getInstance().getReference().child("CustomerCompleted").child(uid);
                        gotoDriverCompleted.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Log.d("wow","dsdsdsd");


                                if (dataSnapshot.exists()){
                                    Log.d("wow","dsdsdsd");
                                     ratingSum = 0;
                                     ratingTotal= 0;
                                    for (final DataSnapshot dataSnap:dataSnapshot.getChildren()){
                                        Log.d("wow",dataSnap.getKey());

                                        DatabaseReference getRating=FirebaseDatabase.getInstance().getReference().child("CompletedRequests").child(dataSnap.getKey());
                                        getRating.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                dId = dataSnapshot.child("driverId").getValue().toString();

                                                Log.d("wow",dataSnapshot.getKey());
                                                ratingSum =ratingSum + dataSnapshot.child("myRating").getValue(Integer.class);
                                                ratingTotal++;
                                                DatabaseReference driverRating = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(dId);

                                                driverRating.child("rating").setValue(ratingSum/ratingTotal);


                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.customerCompletedMap);
        mapFragment.getMapAsync(this);

      /*  ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map)).setListener(new WorkaroundMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                mScrollView.requestDisallowInterceptTouchEvent(true);
            }
        });*/


        addMarker();
        setData();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
     //   buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);



    }

    public void setData(){
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    driverNametxt.setText(dataSnapshot.child("driverName").getValue().toString());
                    driverMobiletxt.setText(dataSnapshot.child("driverMobile").getValue().toString());
                    pickupLocationtxt.setText(dataSnapshot.child("pickup").getValue().toString());
                    dropOffLocationtxt.setText(dataSnapshot.child("delivery").getValue().toString());
                    rcvrNametxt.setText(dataSnapshot.child("rcvrName").getValue().toString());
                    rcvrMobiletxt.setText(dataSnapshot.child("rcvrNumber").getValue().toString());
                    dId = dataSnapshot.child("driverId").getValue().toString();
                    Long datetime=dataSnapshot.child("timeStamp").getValue(Long.class);
                    datetxt.setText(getDate(datetime));
                    String image=dataSnapshot.child("driverImage").getValue().toString();
                    if (!image.equals("default")){
                        Picasso.get().load(image).into(imageView);

                    }
                    if (dataSnapshot.child("myRating")!=null){
                        ratingBar.setRating(dataSnapshot.child("myRating").getValue(Integer.class));
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public String getDate(Long time){
        Calendar calendar=Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(time*1000);

        String date= DateFormat.format("dd-MM-yyyy  hh:mm",calendar).toString();

        return date;
    }
    public void addMarker(){
    DatabaseReference markers = FirebaseDatabase.getInstance().getReference().child("CompletedRequests").child(requestId);
    markers.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists()) {
                pickupLocation = new Location("");
                dropoffLocation = new Location("");

                List<Object> map = (List<Object>) dataSnapshot.child("pickuplocation").child("l").getValue();
                List<Object> mapp = (List<Object>) dataSnapshot.child("dropofflocation").child("l").getValue();

                if (map.get(0) != null) {
                    pickupLocationLat = Double.parseDouble(map.get(0).toString());
                    pickupLocation.setLatitude(pickupLocationLat);
                }
                if (map.get(1) != null) {
                    pickupLocationLon = Double.parseDouble(map.get(1).toString());
                    pickupLocation.setLongitude(pickupLocationLon);

                }
                if (mapp.get(0) != null) {
                    dropoffLocationLat = Double.parseDouble(mapp.get(0).toString());
                    dropoffLocation.setLatitude(dropoffLocationLat);
                }
                if (mapp.get(1) != null) {
                    dropoffLocationLon = Double.parseDouble(mapp.get(1).toString());
                    dropoffLocation.setLongitude(dropoffLocationLon);
                }
                Log.d("location",dropoffLocation.toString());
                pickup = new LatLng(pickupLocationLat, pickupLocationLon);
                dropoff = new LatLng(dropoffLocationLat, dropoffLocationLon);
                mMap.addMarker(new MarkerOptions().position(pickup).title("PickupLocation").icon(BitmapDescriptorFactory.fromResource(R.drawable.box)));
                mMap.addMarker(new MarkerOptions().position(dropoff).title("DropOffLocation").icon(BitmapDescriptorFactory.fromResource(R.drawable.checked)));
                makeroute(pickup,dropoff);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(pickup));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(6));

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
}




    private void makeroute(LatLng driver, LatLng place) {


        new com.example.fyp.directionhelpers.FetchURL(this).execute(getUrl(driver, place, "driving"), "driving");
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    private Polyline currentPolyline;
    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

}
