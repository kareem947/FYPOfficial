package com.example.fyp.Driver;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.fyp.R;
import com.example.fyp.directionhelpers.TaskLoadedCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
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

public class ShowDriverCompletedOrder extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

    public GoogleMap mMap;

    String requestId;
    double pickupLocationLat, dropoffLocationLat, pickupLocationLon, dropoffLocationLon;
    Location pickupLocation, dropoffLocation ;

    LatLng pickup,dropoff;

    TextView pickupLocationtxt,dropOffLocationtxt,driverNametxt,driverMobiletxt,datetxt,rcvrNametxt,rcvrMobiletxt;
    CircleImageView imageView;

    DatabaseReference data;
    String uid,dId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_driver_completed_order);

        pickupLocationtxt=findViewById(R.id.pickupLocation);
        dropOffLocationtxt=findViewById(R.id.dropOffLocation);
        datetxt=findViewById(R.id.dateTime);
        driverNametxt=findViewById(R.id.driverName);
        rcvrNametxt=findViewById(R.id.rcvrName);
        rcvrMobiletxt=findViewById(R.id.rcvrMobile);
        driverMobiletxt=findViewById(R.id.driverMobile);
        imageView=findViewById(R.id.driverPic);
        requestId=getIntent().getStringExtra("requestId");
        data = FirebaseDatabase.getInstance().getReference().child("CompletedRequests").child(requestId);
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.customerCompletedMap);
        mapFragment.getMapAsync(this);


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
                    driverNametxt.setText(dataSnapshot.child("customerName").getValue().toString());
                    driverMobiletxt.setText(dataSnapshot.child("customerNumber").getValue().toString());
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
