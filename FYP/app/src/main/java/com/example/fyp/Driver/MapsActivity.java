package com.example.fyp.Driver;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.fyp.R;
import com.example.fyp.directionhelpers.TaskLoadedCallback;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,TaskLoadedCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener ,com.google.android.gms.location.LocationListener {
    private GoogleMap mMap;
    GoogleApiClient mGoogleClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    FusedLocationProviderClient mfusedLocationProviderClient;
    LocationManager locationManager;
    private String requestId,customerId;
    Button requests;
    String Uid;
    CardView cardView;
    AlertDialog alertDialog;
    double pickupLocationLat, dropoffLocationLat, pickupLocationLon, dropoffLocationLon;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    Location pickupLocation, dropoffLocation , driverLocation;
    FloatingActionButton myfab;
    LatLng driverl,pickup,dropoff;
    TextView showDistanePick,showDistaneDrop;
    Boolean reachedPick = false;
    Boolean reachedDrop = false;
    Boolean working = false;
    String vehicleType;
    Button pickedUp,droppedOff;
    int fair;


    private TextView nameText, phoneText, cnicText,pick,drop;
    CircleImageView imageView;
    private StorageReference mStorageReference;
    TextView makeCall;




    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        requestId=getIntent().getStringExtra("requestId");
        customerId=getIntent().getStringExtra("customerId");



        myfab=findViewById(R.id.makeRoute);
        pickedUp=findViewById(R.id.picked_up);
        droppedOff=findViewById(R.id.dropped_Off);
        cardView=findViewById(R.id.cardView);
        showDistanePick=findViewById(R.id.distanceRemaing);
        showDistaneDrop=findViewById(R.id.distanceRemaingd);


        nameText = findViewById(R.id.CProfileName);
        phoneText =findViewById(R.id.CProfilePhone);
        cnicText =  findViewById(R.id.CProfileCnic);
        imageView =  findViewById(R.id.showCustomerprofile_image);
        pick = findViewById(R.id.pickupLocationtxt);
        drop = findViewById(R.id.dropoffLocationtxt);

        makeCall=findViewById(R.id.makeCall);

        setCustomerProfile();


        myfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseWhere();
            }
        });

        pickedUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickedUp.setClickable(false);
            }
        });
        droppedOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                droppedOff.setClickable(false);

                makeHistory();
            }
        });

        Uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference workingDrivers= FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(Uid);
        workingDrivers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                        vehicleType=dataSnapshot.child("truckType").getValue(String.class);
                        Log.d("vehicle",dataSnapshot.child("truckType").getValue(String.class));

                    DatabaseReference customerId = FirebaseDatabase.getInstance().getReference().child("WorkingRequests").child(requestId);
                    customerId.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                pick.setText(dataSnapshot.child("pickup").getValue(String.class));
                                drop.setText(dataSnapshot.child("delivery").getValue(String.class));
                                fair= Integer.parseInt(dataSnapshot.child("fair").getValue(String.class));
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
                                working = true;
                                pickup = new LatLng(pickupLocationLat, pickupLocationLon);
                                dropoff = new LatLng(dropoffLocationLat, dropoffLocationLon);
                                mMap.addMarker(new MarkerOptions().position(pickup).title("PickupLocation").icon(BitmapDescriptorFactory.fromResource(R.drawable.box)));
                                mMap.addMarker(new MarkerOptions().position(dropoff).title("DropOffLocation").icon(BitmapDescriptorFactory.fromResource(R.drawable.checked)));


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                }
           // }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void setCustomerProfile() {


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(customerId);
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

    private void makeHistory() {

                DatabaseReference workingRequests= FirebaseDatabase.getInstance().getReference().child("WorkingRequests").child(requestId);
                final DatabaseReference completedRequests=FirebaseDatabase.getInstance().getReference().child("CompletedRequests").child(requestId);
                final DatabaseReference customerCompleted= FirebaseDatabase.getInstance().getReference().child("CustomerCompleted").child(customerId);
                final DatabaseReference driverCompleted= FirebaseDatabase.getInstance().getReference().child("DriverCompleted").child(Uid);
                final DatabaseReference driver= FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(Uid);

        driver.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            int x = dataSnapshot.child("completedOrders").getValue(Integer.class);
                            int y = dataSnapshot.child("fair").getValue(Integer.class);

                            if (x>=1){
                                driver.child("completedOrders").setValue(x+1);
                                driver.child("fair").setValue(y+fair);
                                driver.child("suspended").setValue("yes");
                            }
                            else {
                                driver.child("completedOrders").setValue(x + 1);
                                driver.child("fair").setValue(y + fair);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        workingRequests.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            completedRequests.setValue(dataSnapshot.getValue());
                            Log.d("timeStamp", String.valueOf(System.currentTimeMillis()/1000));
                            completedRequests.child("timeStamp").setValue(System.currentTimeMillis()/1000);

                            driverCompleted.child(requestId).setValue(true);
                            customerCompleted.child(requestId).setValue(true);

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
                workingRequests.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MapsActivity.this, "everything is done", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocationPermission();
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
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);


    }



    protected synchronized void buildGoogleApiClient(){
        mGoogleClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleClient.connect();
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
       // updateLocationUI();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {


        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(100);
       // mLocationRequest.setSmallestDisplacement();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
         LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleClient, mLocationRequest,  this);

    }

    @Override
    public void onStop(){
        super.onStop();
/*
        String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(userId);
        GeoFire geoFire = new GeoFire(databaseReference);
        geoFire.removeLocation("location");*/
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    Marker now;
    @Override
    public void onLocationChanged(final Location location) {
        mLastLocation=location;
        if(now != null){
            now.remove();

        }
        driverl= new LatLng(location.getLatitude(),location.getLongitude());
       // mMap.clear();

       now= mMap.addMarker(new MarkerOptions().position(driverl).title("Driver").icon(BitmapDescriptorFactory.fromResource(R.drawable.deliverytruck)));
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(driverl));
       //mMap.moveCamera(CameraUpdateFactory.z(6));


        DatabaseReference workingDrivers= FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(Uid);
        workingDrivers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    vehicleType=dataSnapshot.child("truckType").getValue(String.class);

                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();


                        final GeoFire geoFire = new GeoFire(databaseReference.child("WorkingDrivers"));


                        geoFire.setLocation(Uid, new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {

                            @Override
                            public void onComplete(String key, DatabaseError error) {
                                if (error != null) {
                                    System.err.println("There was an error saving the location to GeoFire: " + error);
                                } else {
                                    System.out.println("Location saved on server successfully!");
                                }
                            }

                        });

                        Log.d("ithy ta aya hy","working");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


         if(working) {
             cardView.setVisibility(View.VISIBLE);
             if (!reachedPick) {

                 findDistance(mLastLocation, pickupLocation, "pick");
             }
             if (!reachedDrop ) {
                 findDistance(mLastLocation, dropoffLocation, "drop");
             }
         }
         else
         {
             cardView.setVisibility(View.INVISIBLE);
         }
    }

    public void chooseWhere() {


        CharSequence[] values = {"PickUpPlace", "DropOffPlace"};
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(this);
        mbuilder.setTitle("Choose Where To..");
        mbuilder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                switch (i) {
                    case 0:
/*
                        removePolyLines();
*/
                        makeroute(driverl,pickup);
                        Toast.makeText(getApplicationContext(), "PickupPlace Clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
/*
                        removePolyLines();
*/
                        makeroute(driverl,dropoff);
                        Toast.makeText(getApplicationContext(), "DropOffPlaceClicked", Toast.LENGTH_SHORT).show();
                        break;
                }
                alertDialog.dismiss();
            }
        });

        alertDialog = mbuilder.create();
        alertDialog.show();
    }



    public void findDistance(Location location1, Location location2,String to){
        DecimalFormat df = new DecimalFormat("#.####");
        double distance=Double.parseDouble( df.format(location1.distanceTo(location2)/1000));
        if(working) {
            if (to.equals("pick")) {
                if (distance < 1.0) {
                    reachedPick = true;
                    showDistanePick.setText(distance + "KM");
                    showDistanePick.setVisibility(View.INVISIBLE);
                    Toast.makeText(this, "You reached At PickUp Location Click if You Picked the Order", Toast.LENGTH_SHORT).show();
                    pickedUp.setVisibility(View.VISIBLE);
                } else
                    showDistanePick.setText(distance + "KM");
            } else {

                if (distance < 1.0 && reachedPick) {
                    reachedDrop = true;
                    showDistaneDrop.setText(distance + "KM");
                    showDistaneDrop.setVisibility(View.INVISIBLE);
                    // showDistane.setVisibility(0);
                    Toast.makeText(this, "You reached At DropOff ", Toast.LENGTH_SHORT).show();
                    droppedOff.setVisibility(View.VISIBLE);

                } else
                    showDistaneDrop.setText(distance + "KM");
            }
        }
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