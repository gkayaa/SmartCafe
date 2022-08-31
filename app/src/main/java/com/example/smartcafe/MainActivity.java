package com.example.smartcafe;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements LocationListener {
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    private FirebaseAuth mAuth;
    protected Context context;
    ImageButton btnLocation;
    String lat;
    String provider;
    private String session;
    private TextView txtCurrentUser;
    DatabaseReference ref;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;
    static boolean isLocated = false;
    private static final int PERMISSION_REQUEST_LOC1 = 0;
    private static final int PERMISSION_REQUEST_LOC2 = 0;


    @SuppressLint("MissingPermission")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestLocation();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        btnLocation = (ImageButton) findViewById(R.id.btnLocationOption);
        mAuth = FirebaseAuth.getInstance();
        txtCurrentUser = findViewById(R.id.txtCurrentUser);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference();
        btnLocation = (ImageButton) findViewById(R.id.btnLocationOption);


        if(isLocated == false)
        {
            btnLocation.setImageResource(R.drawable.ic_baseline_location_disabled_24);
        }
        else
        {
            btnLocation.setImageResource(R.drawable.ic_baseline_my_location_24);
            btnLocation.setClickable(false);
        }

        if (currentUser == null) {
            // No user is signed in
        } else {
            session = currentUser.getUid();
            DatabaseReference db = FirebaseDatabase.getInstance().getReference();

            Query query = db.child("Users").child(session).child("name");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    txtCurrentUser.setText(snapshot.getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            loadFragment(new fragmentHome());

           /* locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);*/


        }
    }

    /*private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {

                case R.id.navigation_home:
                    swapFragments(new fragmentHome(), item.getItemId());
                    return true;

                case R.id.navigation_order:
                    swapFragments(new fragmentOrder(), item.getItemId());
                    return true;

                case R.id.navigation_notifications:
                    swapFragments(new fragmentNotifications(), item.getItemId());
                    return true;

                case R.id.navigation_options:
                    swapFragments(new fragmentOptions(), item.getItemId());
                    return true;

                default:
                    return false;
            }
        }
    });

    private void swapFragments(Fragment fragment, int itemId) {
        if (getSupportFragmentManager().findFragmentById(R.id.)) == null) {
            saveFragmentState(itemId);
            createFragment(fragment, itemId);
        }
    }

    private void saveFragmentState(int itemId) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container_fragment);
        if (currentFragment != null) {
            fragmentStateArray.put(currentSelectedItemId, getSupportFragmentManager().saveFragmentInstanceState(currentFragment));
        }

        currentSelectedItemId = itemId;
    }

    private void createFragment(Fragment fragment, int itemId, String tag) {
        fragment.setInitialSavedState(fragmentStateArray.get(itemId));

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_fragment, fragment, tag)
                .commit();
    }*/


    private void requestLocation()
    {
        if ((ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED))
        {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        else
        {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)) && (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)))
            {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_LOC1);
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_REQUEST_LOC2);
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
            else
            {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_LOC1);
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_REQUEST_LOC2);
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
        };
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = new fragmentHome();

                    break;
                case R.id.navigation_notifications:
                    selectedFragment = new fragmentNotifications();
                    break;
                case R.id.navigation_options:
                    selectedFragment = new fragmentOptions();
                    break;
                case R.id.navigation_order:
                    selectedFragment = new fragmentOrder();
                    break;
            }
            loadFragment(selectedFragment);
            return true;
        }
    };
    private void loadFragment(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().setCustomAnimations(
                R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
        transaction.replace(R.id.mainFrameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onLocationChanged (Location location){
       btnLocation.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v)
           {
               ref.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("latitude").setValue(location.getLatitude());
               ref.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("longitude").setValue(location.getLongitude());
               Intent sendingIntent = new Intent(MainActivity.this, LocationActivity.class);
               sendingIntent.putExtra("lat", location.getLatitude());
               sendingIntent.putExtra("long", location.getLongitude());
               startActivity(sendingIntent);
           }


       });

    }

    @Override
    public void onProviderDisabled (String provider){
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled (String provider){
        Log.d("Latitude", "enable");
    }

    @Override
    public void onStatusChanged (String provider,int status, Bundle extras){
        Log.d("Latitude", "status");
    }



}