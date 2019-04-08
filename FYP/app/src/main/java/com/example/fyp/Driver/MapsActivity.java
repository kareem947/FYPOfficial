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
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends Fragment implements OnMapReadyCallback, TaskLoadedCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener ,com.google.android.gms.location.LocationListener{
    private GoogleMap mMap;
    GoogleApiClient mGoogleClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    FusedLocationProviderClient mfusedLocationProviderClient;
    LocationManager locationManager;
    private String customerId="";
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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);

        View view=inflater.inflate(R.layout.activity_maps,container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
       /* SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/


        SupportMapFragment mapFragment =
                (SupportMapFragment)
                        getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        myfab=view.findViewById(R.id.makeRoute);
        pickedUp=view.findViewById(R.id.picked_up);
        droppedOff=view.findViewById(R.id.dropped_Off);
        cardView=view.findViewById(R.id.cardView);
        showDistanePick=view.findViewById(R.id.distanceRemaing);
        showDistaneDrop=view.findViewById(R.id.distanceRemaingd);

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
                DatabaseReference workingDrivers= FirebaseDatabase.getInstance().getReference().child("WorkingRequests").child(customerId);
                DatabaseReference completedRequests=FirebaseDatabase.getInstance().getReference().child("completeRequests").child(customerId);



            }
        });

/*
        Uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference workingDrivers= FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(Uid);
        workingDrivers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                        vehicleType=dataSnapshot.child("truckType").getValue(String.class);
                    if (dataSnapshot.child("working").getValue(String.class)!=null){
                        customerId=dataSnapshot.child("working").getValue(String.class);
*//*
                    Log.d("workingValue",dataSnapshot.child("working").getValue(String.class));
*//*

                    DatabaseReference customerId = FirebaseDatabase.getInstance().getReference().child("WorkingRequests").child(dataSnapshot.child("working").getValue(String.class));
                    customerId.addValueEventListener(new ValueEventListener() {
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
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        return view;

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocationPermission();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        mGoogleClient = new GoogleApiClient.Builder(getActivity())
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
        if (ContextCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
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
        mLocationRequest.setInterval(100000);
       // mLocationRequest.setSmallestDisplacement();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        final String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();


        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        DatabaseReference databaseReference1= FirebaseDatabase.getInstance().getReference("DriversAvailable").child(vehicleType);
        if (working) {
            final GeoFire geoFire = new GeoFire(databaseReference.child("WorkingDrivers"));
            GeoFire geoFire1=new GeoFire(databaseReference1);

                     geoFire1.removeLocation(userId, new GeoFire.CompletionListener() {
                         @Override
                         public void onComplete(String key, DatabaseError error) {
                             geoFire.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {

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
                     });
            geoFire.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {

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
        else {
            GeoFire geoFire = new GeoFire(databaseReference.child("WorkingDrivers"));
            final GeoFire geoFire1=new GeoFire(databaseReference1);

            geoFire.removeLocation(userId, new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {
                    geoFire1.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {

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
            });
            geoFire1.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {

                @Override
                public void onComplete(String key, DatabaseError error) {
                    if (error != null) {
                        System.err.println("There was an error saving the location to GeoFire: " + error);
                    } else {
                        System.out.println("Location saved on server successfully!");
                    }
                }

            });

            Log.d("ithy ta aya hy","not working");


        }
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
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(getActivity());
        mbuilder.setTitle("Choose Where To..");
        mbuilder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                switch (i) {
                    case 0:
                        makeroute(driverl,pickup);
                        Toast.makeText(getActivity(), "PickupPlace Clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        makeroute(driverl,dropoff);
                        Toast.makeText(getActivity(), "DropOffPlaceClicked", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), "You reached At PickUp Location Click if You Picked the Order", Toast.LENGTH_SHORT).show();
                    pickedUp.setVisibility(View.VISIBLE);
                } else
                    showDistanePick.setText(distance + "KM");
            } else {

                if (distance < 1.0 && reachedPick) {
                    reachedDrop = true;
                    showDistaneDrop.setText(distance + "KM");
                    showDistaneDrop.setVisibility(View.INVISIBLE);
                    // showDistane.setVisibility(0);
                    Toast.makeText(getActivity(), "You reached At DropOff ", Toast.LENGTH_SHORT).show();
                    droppedOff.setVisibility(View.VISIBLE);

                } else
                    showDistaneDrop.setText(distance + "KM");
            }
        }
    }


    private void makeroute(LatLng driver, LatLng place) {

        new com.example.fyp.directionhelpers.FetchURL(getActivity()).execute(getUrl(driver, place, "driving"), "driving");
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