package com.example.smartcafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class DirectionsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.directionMap);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        Bundle b = getIntent().getExtras();
        double userLat = b.getDouble("userLat");
        double userLong = b.getDouble("userLong");
        double resLat = b.getDouble("resLat");
        double resLong = b.getDouble("resLong");
        LatLng source = new LatLng(userLat,userLong);
        LatLng destination = new LatLng(resLat, resLong);
        mMap.addMarker(new MarkerOptions().position(source).title("Your Location"));
        mMap.addMarker(new MarkerOptions().position(destination).title("Restaurant"));


        ArrayList<LatLng> list = new ArrayList<LatLng>();
        list.add(source);
        list.add(destination);
        PolylineOptions lineOptions = new PolylineOptions();
        lineOptions.width(2);
        lineOptions.color(Color.BLUE);
        lineOptions.addAll(list);
        mMap.addPolyline(lineOptions);


        /*new GetPathFromLocation(source, destination, new DirectionPointListener()
        {
            @Override
            public void onPath(PolylineOptions polyLine)
            {
                mMap.addPolyline(polyLine);
            }
        }).execute();*/
    }
}