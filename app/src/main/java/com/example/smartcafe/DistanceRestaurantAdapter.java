package com.example.smartcafe;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

class DistanceRestaurantAdapter extends BaseAdapter {

    private ArrayList<Restaurant> orderedRestaurants;
    private HashMap<Restaurant,Double> distances;
    private Context context;
    private LayoutInflater layoutInflater;
    private FirebaseAuth mAuth;
    private fragmentOrder newFragment;

    public DistanceRestaurantAdapter(Context context, ArrayList<Restaurant> orderedRestaurants){
        this.context = context;
        this.orderedRestaurants = orderedRestaurants;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return orderedRestaurants.size();
    }

    @Override
    public Object getItem(int i) {
        return orderedRestaurants.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {

        View resListView = layoutInflater.inflate(R.layout.list_restaurant,null);
        TextView txtResName = (TextView) resListView.findViewById(R.id.txtResName);
        TextView txtResId = (TextView) resListView.findViewById(R.id.txtResId);
        RatingBar ratingBar = (RatingBar) resListView.findViewById(R.id.ratingBar);
        TextView txtDistance = (TextView) resListView.findViewById(R.id.txtDistance);
        Button btnDirection = (Button) resListView.findViewById(R.id.btnDirection);

        mAuth = FirebaseAuth.getInstance();
        txtResName.setText(orderedRestaurants.get(i).getResName());
        txtResId.setText(orderedRestaurants.get(i).getResID());

        Task<DataSnapshot> userLat = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid().toString()).child("latitude").get();
        Task<DataSnapshot> userLong = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid().toString()).child("longitude").get();


           userLat.addOnCompleteListener(new OnCompleteListener<DataSnapshot>()
           {
               @Override
               public void onComplete(@NonNull Task<DataSnapshot> task)
               {
                   userLong.addOnCompleteListener(new OnCompleteListener<DataSnapshot>()
                   {
                       @Override
                       public void onComplete(@NonNull Task<DataSnapshot> task)
                       {
                           double userLatValue = userLat.getResult().getValue(double.class);
                           double userLongValue = userLong.getResult().getValue(double.class);
                           DatabaseReference resLatLong = FirebaseDatabase.getInstance().getReference().child("Restaurants");
                           Query QresLatLong = resLatLong.child(orderedRestaurants.get(i).getResID());

                           QresLatLong.addValueEventListener(new ValueEventListener()
                           {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot snapshot)
                               {
                                   Restaurant resultRes = snapshot.getValue(Restaurant.class);
                                    //MESAFELERDE 2 BASAMAK VIRGUL GOSTERILMESI IYI OLUR ONUNLA ILGILEN, RESTORAN PUANLAMASI YAPILABİLİR. YOL TARİFİNE GEÇİLECEK
                                   Location usr = new Location("");
                                   Location rest = new Location("");
                                   usr.setLatitude(userLatValue);
                                   usr.setLongitude(userLongValue);
                                   rest.setLatitude(resultRes.getResLatitude());
                                   rest.setLongitude(resultRes.getResLongitude());
                                   double distance = 0;
                                   distance = usr.distanceTo(rest) / 1000;
                                  /* DecimalFormat formatter = new DecimalFormat("##.##");
                                   formatter.format(distance);*/

                                   txtDistance.setText(String.format("%10.2f", distance).concat(" KM Away"));
                                   ratingBar.setRating((float) (resultRes.getResRate()/2));
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError error)
                               {

                               }
                           });
                       }
                   });
               }
           });

           btnDirection.setOnClickListener(new View.OnClickListener()
           {
               @Override
               public void onClick(View v)
               {

                   Task<DataSnapshot> userLat2 = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid().toString()).child("latitude").get();
                   Task<DataSnapshot> userLong2 = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid().toString()).child("longitude").get();


                   userLat2.addOnCompleteListener(new OnCompleteListener<DataSnapshot>()
                   {
                       @Override
                       public void onComplete(@NonNull Task<DataSnapshot> task)
                       {
                           userLong2.addOnCompleteListener(new OnCompleteListener<DataSnapshot>()
                           {
                               @Override
                               public void onComplete(@NonNull Task<DataSnapshot> task)
                               {
                                   double userLatValue2 = (double) userLat2.getResult().getValue();
                                   double userLongValue2 = (double) userLong2.getResult().getValue();
                                   DatabaseReference resLatLong2 = FirebaseDatabase.getInstance().getReference().child("Restaurants");
                                   Query QresLatLong2 = resLatLong2.child(orderedRestaurants.get(i).getResID());

                                   QresLatLong2.addValueEventListener(new ValueEventListener()
                                   {
                                       @Override
                                       public void onDataChange(@NonNull DataSnapshot snapshot2)
                                       {
                                           Restaurant resultRes2 = snapshot2.getValue(Restaurant.class);
                                           LatLng source = new LatLng(userLatValue2,userLongValue2);
                                           LatLng destination = new LatLng(resultRes2.getResLatitude(), resultRes2.getResLongitude());
                                           Intent intentDirections = new Intent(context, DirectionsActivity.class);
                                           Bundle b = new Bundle();
                                           b.putDouble("userLat", source.latitude);
                                           b.putDouble("userLong", source.longitude);
                                           b.putDouble("resLat", destination.latitude);
                                           b.putDouble("resLong", destination.longitude);
                                           intentDirections.putExtras(b);
                                           context.startActivity(intentDirections);
                                       }

                                       @Override
                                       public void onCancelled(@NonNull DatabaseError error)
                                       {

                                       }
                                   });
                               }
                           });
                       }
                   });







               }
           });


        return resListView;
    }
    /*private double CalculateDistance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    *//*  This function converts decimal degrees to radians   *//*

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }


    *//*  This function converts radians to decimal degrees       *//*
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }*/

}