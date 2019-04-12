package com.example.fyp.Customer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.fyp.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
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

import java.util.List;

public class CustomerMaps extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    GoogleApiClient mGoogleClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    FusedLocationProviderClient mfusedLocationProviderClient;
    LocationManager locationManager;
    double pickupLat;
    double pickupLon;
    String uid;
    Location pickuplocation;
    Location driverlocation;
    Button distance;
    FloatingActionButton myfab;
    double locationlat;
    double locationlon;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    LatLng pick,driver;
    double pickupLocationLat, dropoffLocationLat, pickupLocationLon, dropoffLocationLon;
    Location pickupLocation, dropoffLocation , driverLocation;
    LatLng pickup,dropoff;
    private DrawerLayout drawer;
    Toolbar toolbar;
    String requestId ,driverId;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.customerMap);
        mapFragment.getMapAsync(this);


        requestId=getIntent().getStringExtra("requestId");
      //  driverId=getIntent().getStringExtra("driverId");

      //  Log.d("MyCheck",requestId+"     "+driverId);
        myfab=findViewById(R.id.newOrder);
        myfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new MakeOrder();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference workingDrivers= FirebaseDatabase.getInstance().getReference().child("WorkingRequests").child(requestId);
        workingDrivers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    driverId=dataSnapshot.child("driverId").getValue(String.class);
                    Log.d("MyCheck1",requestId+"     "+driverId);

                    addMarkers(driverId);
                }
                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }






    public void findDistance(){
        float distance=pickuplocation.distanceTo(driverlocation);
       // Toast.makeText(getActivity(), ""+distance/1000, Toast.LENGTH_SHORT).show();
    }
    Marker now;
    public void addMarkers(String driverId){
        Log.d("MyCheckmarker",""+locationlat+"   "+locationlon);

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("WorkingDrivers").child(driverId).child("l");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    List<Object> map= (List<Object>) dataSnapshot.getValue();
                    if (map.get(0)!=null){
                        locationlat=Double.parseDouble(map.get(0).toString());
                    }
                    if (map.get(1)!=null){
                        locationlon=Double.parseDouble(map.get(1).toString());
                    }
                    driver = new LatLng(locationlat,locationlon);
                    Log.d("MyCheckmarker",""+locationlat+"   "+locationlon);
                    if(now != null){
                        now.remove();
                    }
                    now=  mMap.addMarker(new MarkerOptions().position(driver).title("Driver").icon(BitmapDescriptorFactory.fromResource(R.drawable.deliverytruck)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(driver));
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
                 //   makeroute();
                    DatabaseReference customerId = FirebaseDatabase.getInstance().getReference().child("WorkingRequests").child(requestId);
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
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        driverlocation=new Location("");
        driverlocation.setLatitude(locationlat);
        driverlocation.setLongitude(locationlon);
        Toast.makeText(this, ""+driverlocation.getLatitude()+"\n"+driverlocation.getLongitude(), Toast.LENGTH_SHORT).show();
      // findDistance();
    }
/*
    private void makeroute() {

        new com.example.fyp.directionhelpers.FetchURL(this).execute(getUrl(driver, pick, "driving"), "driving");
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
    }*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;/*
        LatLng latLng = new LatLng(pickupLat,pickupLon);
        mMap.addMarker(new MarkerOptions().position(latLng).title("Customer"));*/
        getLocationPermission();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }/*
        double latitude=32.6367362;
        double longitude= 71.744148;
        LatLng latLng = new LatLng(latitude,longitude);
        mMap.addMarker(new MarkerOptions().position(latLng).title("Customer"));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(11));*/
        mMap.setMyLocationEnabled(true);

       // addMarkers();
    }


    private void getLocationPermission() {

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
    }



  /*  private Polyline currentPolyline;
    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
*/
}











































































/*




package com.example.fyp.Customer;

        import android.Manifest;
        import android.content.pm.PackageManager;
        import android.location.Location;
        import android.location.LocationManager;

        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.design.widget.FloatingActionButton;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.app.Fragment;
        import android.os.Bundle;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentTransaction;
        import android.support.v4.content.ContextCompat;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.Toast;

        import com.example.fyp.R;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.location.FusedLocationProviderClient;
        import com.google.android.gms.location.LocationRequest;
        import com.google.android.gms.maps.CameraUpdateFactory;
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

        import java.util.List;

public class CustomerMaps extends Fragment implements OnMapReadyCallback, com.example.fyp.directionhelpers.TaskLoadedCallback  {
    private GoogleMap mMap;
    GoogleApiClient mGoogleClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    FusedLocationProviderClient mfusedLocationProviderClient;
    LocationManager locationManager;
    double pickupLat;
    double pickupLon;
    String uid;
    Location pickuplocation;
    Location driverlocation;
    Button distance;
    FloatingActionButton myfab;
    double locationlat;
    double locationlon;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    LatLng pick,driver;
    double pickupLocationLat, dropoffLocationLat, pickupLocationLon, dropoffLocationLon;
    Location pickupLocation, dropoffLocation , driverLocation;
    LatLng pickup,dropoff;
    private DrawerLayout drawer;
    Toolbar toolbar;
    String requestId ,driverId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.activity_customer_maps,container, false);
        SupportMapFragment mapFragment =
                (SupportMapFragment)
                        getChildFragmentManager().findFragmentById(R.id.customerMap);
        mapFragment.getMapAsync(this);
        requestId=getActivity().getIntent().getStringExtra("requestId");
        driverId=getActivity().getIntent().getStringExtra("driverId");
        myfab=view.findViewById(R.id.newOrder);
        myfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new MakeOrder();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference workingDrivers= FirebaseDatabase.getInstance().getReference().child("WorkingRequests").child(requestId);
        workingDrivers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    driverId=dataSnapshot.child("driverId").getValue(String.class);
                    addMarkers(driverId);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return view;
    }






    public void findDistance(){
        float distance=pickuplocation.distanceTo(driverlocation);
        // Toast.makeText(getActivity(), ""+distance/1000, Toast.LENGTH_SHORT).show();
    }
    Marker now;
    public void addMarkers(String driverId){
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("WorkingDrivers").child(driverId).child("l");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    List<Object> map= (List<Object>) dataSnapshot.getValue();
                    if (map.get(0)!=null){
                        locationlat=Double.parseDouble(map.get(0).toString());
                    }
                    if (map.get(1)!=null){
                        locationlon=Double.parseDouble(map.get(1).toString());
                    }
                    driver = new LatLng(locationlat,locationlon);
                    Log.d("MyCheck marker",""+locationlat+"   "+locationlon);
                    if(now != null){
                        now.remove();
                    }
                    now=  mMap.addMarker(new MarkerOptions().position(driver).title("Driver").icon(BitmapDescriptorFactory.fromResource(R.drawable.deliverytruck)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(driver));
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
                    //   makeroute();
                    DatabaseReference customerId = FirebaseDatabase.getInstance().getReference().child("WorkingRequests").child(requestId);
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
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        driverlocation=new Location("");
        driverlocation.setLatitude(locationlat);
        driverlocation.setLongitude(locationlon);
        Toast.makeText(getActivity(), ""+driverlocation.getLatitude()+"\n"+driverlocation.getLongitude(), Toast.LENGTH_SHORT).show();
        // findDistance();
    }

    private void makeroute() {

        new com.example.fyp.directionhelpers.FetchURL(getActivity()).execute(getUrl(driver, pick, "driving"), "driving");
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;*/
/*
        LatLng latLng = new LatLng(pickupLat,pickupLon);
        mMap.addMarker(new MarkerOptions().position(latLng).title("Customer"));*//*

        getLocationPermission();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }*/
/*
        double latitude=32.6367362;
        double longitude= 71.744148;
        LatLng latLng = new LatLng(latitude,longitude);
        mMap.addMarker(new MarkerOptions().position(latLng).title("Customer"));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(11));*//*

        mMap.setMyLocationEnabled(true);

        // addMarkers();
    }


    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    */
/**
     * Handles the result of the request for location permissions.
     *//*

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
    }



    private Polyline currentPolyline;
    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

}

*/


