package com.example.smartcafe;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    Double longtd;
    Double lattd;
    Button btnok;
    Button temp;
    final Context con = this;
    boolean msgCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        msgCheck = false;
        Intent receiveIntent = this.getIntent();
        lattd = receiveIntent.getDoubleExtra("lat", 0);
        longtd = receiveIntent.getDoubleExtra("long", 0);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnok = findViewById(R.id.btnOk);
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.isLocated = true;
                if(msgCheck == false)
                {
                    final Dialog dialog = new Dialog(con);
                    dialog.setContentView(R.layout.successful_alertdialog);

                    TextView txtAlert = (TextView) dialog.findViewById(R.id.txtTitle);
                    ImageView imgIcon = (ImageView) dialog.findViewById(R.id.imgIcon);

                    txtAlert.setText("Location Received \n      Redirecting...");
                    imgIcon.setImageResource(R.drawable.eathamburger);

                    dialog.show();
                    msgCheck = true;}

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(LocationActivity.this, MainActivity.class));
                        finish();
                    }
                },2000);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng currentLocation = new LatLng(lattd, longtd);
        googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Your Location"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lattd,longtd))
                .title("Your Location")
                .snippet("Your Location"));
    }
}