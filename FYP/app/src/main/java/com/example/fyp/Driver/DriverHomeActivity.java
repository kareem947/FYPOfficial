package com.example.fyp.Driver;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.Driver.DriverRequests.DriverCompleteOrders;
import com.example.fyp.Driver.DriverRequests.DriverRequests;
import com.example.fyp.R;
import com.example.fyp.SignIn;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DriverHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, com.google.android.gms.location.LocationListener, GoogleApiClient.OnConnectionFailedListener , GoogleApiClient.ConnectionCallbacks{

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    GoogleApiClient mGoogleClient;
    LocationRequest mLocationRequest;
    Boolean working=false;
    TextView driverName,driverEmail;

    String Uid,vehicleType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getLocationPermission();
        buildGoogleApiClient();
        Uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("iamhere",Uid);

        final DatabaseReference driver= FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(Uid);
        driver.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("iamhere", dataSnapshot.child("truckType").getValue(String.class));
                    vehicleType = dataSnapshot.child("truckType").getValue(String.class);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        driverName = (TextView) headerView.findViewById(R.id.driverName);
        driverEmail=(TextView) headerView.findViewById(R.id.driverEmail);
        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    driverName.setText(dataSnapshot.child("name").getValue(String.class));
                    driverEmail.setText(dataSnapshot.child("email").getValue(String.class));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        navigationView.setNavigationItemSelectedListener(this);
       /* getSupportFragmentManager().beginTransaction().replace(R.id.driver_container,new MapsActivity()).commit();*/

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.driver_home, menu);
        MenuItem item = menu.findItem(R.id.app_bar_switch);
        item.setActionView(R.layout.switch_item);

        Switch mySwitch = item.getActionView().findViewById(R.id.app_switch);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // do something based on isChecked
                if (isChecked){
                    working=true;
                }
                if (!isChecked){
                    working=false;
                }
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.app_bar_switch){
            Toast.makeText(this, "cccccccccc", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.new_Order) {

            getSupportFragmentManager().beginTransaction().replace(R.id.driver_container,new DriverRequests()).commit();

        } else if (id == R.id.progressing_Order) {

            getSupportFragmentManager().beginTransaction().replace(R.id.driver_container,new DriverAllProgressingOrders()).commit();

        } else if (id == R.id.completed_Orders) {
            getSupportFragmentManager().beginTransaction().replace(R.id.driver_container,new DriverCompleteOrders()).commit();


        } else if (id == R.id.settings) {

            getSupportFragmentManager().beginTransaction().replace(R.id.driver_container,new ShowDriverProfile()).commit();

        } else if (id == R.id.logOut) {
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(DriverHomeActivity.this, SignIn.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    GeoFire geoFire1;

    @Override
    public void onLocationChanged( Location location) {

        Log.d("iamhere","noooooo");

        if (working) {
            DatabaseReference databaseReference1= FirebaseDatabase.getInstance().getReference().child("WorkingDrivers");
            final GeoFire geoFire = new GeoFire(databaseReference1);
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

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("DriversAvailable").child(vehicleType);
            geoFire1 = new GeoFire(databaseReference);
            geoFire1.setLocation(Uid, new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {

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
        if (!working) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("DriversAvailable").child(vehicleType);
            DatabaseReference databaseReference1= FirebaseDatabase.getInstance().getReference().child("WorkingDrivers");



            geoFire1 = new GeoFire(databaseReference1);
            geoFire1.setLocation(Uid, new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {

                @Override
                public void onComplete(String key, DatabaseError error) {
                    if (error != null) {
                        System.err.println("There was an error saving the location to GeoFire: " + error);
                    } else {
                        System.out.println("Location saved on server successfully!");
                    }
                }

            });


            GeoFire geoFire=new GeoFire(databaseReference);
            geoFire.removeLocation(Uid, new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {

                }
            });


        }
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
    protected synchronized void buildGoogleApiClient(){
        mGoogleClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.d("xyss","aaaaaaaaaaa");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10);
        // mLocationRequest.setSmallestDisplacement();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
