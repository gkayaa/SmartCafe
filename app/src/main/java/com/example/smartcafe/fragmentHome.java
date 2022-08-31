package com.example.smartcafe;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;

import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragmentHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragmentHome extends Fragment {
    public List<String> list_parent;
    TextView txtRestaurant;
    public ArrayList<Restaurant> list_adapter_restaurant;
    public DistanceRestaurantAdapter adapterDistance;
    public ExpandListViewAdapter expand_adapter;
    public HashMap<String, ArrayList<FoodDrink>> list_child;
    public ExpandableListView expandlist_view;
    public ArrayList<ArrayList> iter;
    public int last_position = -1;
    View myView;
    ImageButton btnScan;
    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private static final int PERMISSION_REQUEST_CAMERA = 0;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    static String qrCode;
    String restaurantName;
    int[] itemCount = new int[1];
    DatabaseReference db;
    TextView txtTemp;
    int qrRead = 0;
    Button btnLeave;
    Button btnDeneme;
    ListView listRestaurants;
    Context cont;

    public fragmentHome() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static fragmentHome newInstance(String param1, String param2) {
        fragmentHome fragment = new fragmentHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        myView = inflater.inflate(R.layout.fragment_home, container, false);
        previewView = myView.findViewById(R.id.activity_main_previewView);
        btnScan = myView.findViewById(R.id.btnScan);
        txtRestaurant = myView.findViewById(R.id.txtRestaurant);
        expandlist_view = (ExpandableListView)myView.findViewById(R.id.expandableListView);
        txtTemp = myView.findViewById(R.id.txtTemp);
        btnLeave = myView.findViewById(R.id.btnLeave);
        listRestaurants = myView.findViewById(R.id.listRestaurants);

        PrepareMenu();

        FirebaseAuth user = FirebaseAuth.getInstance();
        DatabaseReference check = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("currentRestaurant");
        Query checkQuery = check;
        check.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue().toString().equals(""))
                {
                    btnScan.setVisibility(View.VISIBLE);
                    listRestaurants.setVisibility(View.VISIBLE);

                }
                else
                {
                    btnLeave.setVisibility(View.VISIBLE);
                    expandlist_view.setVisibility(View.VISIBLE);
                    DatabaseReference resNameGetter = FirebaseDatabase.getInstance().getReference().child("Restaurants").child(snapshot.getValue().toString()).child("resName");
                    Query resNameQuery = resNameGetter;
                    resNameQuery.addValueEventListener(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot2)
                        {
                            txtRestaurant.setText(snapshot2.getValue().toString());
                            txtRestaurant.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error)
                        {

                        }
                    });
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnLeave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FirebaseAuth user = FirebaseAuth.getInstance();
                DatabaseReference del1 = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("currentRestaurant");
                del1.setValue("");
                DatabaseReference del2 = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("resTable");
                del2.setValue("");
                btnLeave.setVisibility(View.INVISIBLE);
                expandlist_view.setVisibility(View.INVISIBLE);
                txtRestaurant.setVisibility(View.INVISIBLE);
            }
        });

        expandlist_view.setOnGroupExpandListener(new OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

                if(last_position != -1 && last_position != groupPosition)
                {
                    expandlist_view.collapseGroup(last_position);
                }
                last_position = groupPosition;

            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraProviderFuture = ProcessCameraProvider.getInstance(getActivity());
                requestCamera();
                previewView.setVisibility(View.VISIBLE);
                btnScan.setVisibility(View.INVISIBLE);
                txtRestaurant.setVisibility(View.VISIBLE);
                listRestaurants.setVisibility(View.INVISIBLE);
            }
        });



        list_adapter_restaurant = new ArrayList<Restaurant>();
        DatabaseReference getAllRestaurants = FirebaseDatabase.getInstance().getReference().child("Restaurants");
        Query QgetRestaurants = getAllRestaurants;
        QgetRestaurants.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot dsRes : snapshot.getChildren())
                {
                    Restaurant temp = dsRes.getValue(Restaurant.class);
                    list_adapter_restaurant.add(temp);
                }
                adapterDistance = new DistanceRestaurantAdapter(getContext(),list_adapter_restaurant);
                listRestaurants.setAdapter(adapterDistance);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });























        return myView;
    }
    public void PrepareMenu()
    {

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        list_parent = new ArrayList<String>();  // başlıklarımızı listemelek için oluşturduk
        list_child = new HashMap<String, ArrayList<FoodDrink>>(); // başlıklara bağlı elemenları tutmak için oluşturduk

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String item = "Item";

        if(qrCode == null)
        {
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference db2 = rootRef.child("Users").child(mAuth.getUid()).child("currentRestaurant");
            Query query2 = db2;
            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    if(dataSnapshot.getValue() != null)
                    {
                        long count = dataSnapshot.getChildrenCount();
                        txtTemp.setText(String.valueOf(count));

                        FirebaseDatabase db3 = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = db3.getReference().child("FoodsDrinks");
                        Query query3 = myRef/*.child(item.concat(String.valueOf(i+1)))*/.orderByChild("menuID").equalTo(dataSnapshot.getValue().toString());
                        query3.addValueEventListener(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot)
                            {
                                for (DataSnapshot ds : snapshot.getChildren())
                                {
                                    FoodDrink result = ds.getValue(FoodDrink.class);
                                    if (!(list_parent.contains(result.getCategory())))
                                    {
                                        list_parent.add(result.getCategory());
                                        list_child.put(list_parent.get(list_parent.indexOf(result.getCategory())),new ArrayList<FoodDrink>()); // ilk başlığımızı ve onların elemanlarını HashMap sınıfında tutuyoruz
                                        list_child.get(result.getCategory()).add(result);
                                    }
                                    else if(list_parent.contains(result.getCategory()))
                                    {
                                        list_child.get(result.getCategory()).add(result);
                                        //list_child.get(result.getCategory()).get(list_child.get(result.getCategory()).indexOf(result.getName())).concat(String.valueOf(result.getCost()));

                                        //list_child.get(result.getCategory()).add(String.valueOf(result.getCost()));
                                    }
                                }
                                expand_adapter = new ExpandListViewAdapter(getContext(), list_parent, list_child);
                                expandlist_view.setAdapter(expand_adapter);  // oluşturduğumuz adapter sınıfını set ediyoruz
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error)
                            {

                            }
                        });
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };
            query2.addListenerForSingleValueEvent(eventListener);
        }
        else
        {
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference db2 = rootRef.child("Users").child(mAuth.getUid()).child("currentUser");
            Query query2 = db2;
            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    long count = dataSnapshot.getChildrenCount();
                    txtTemp.setText(String.valueOf(count));

                    FirebaseDatabase db3 = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = db3.getReference().child("FoodsDrinks");
                    Query query3 = myRef/*.child(item.concat(String.valueOf(i+1)))*/.orderByChild("menuID").equalTo(qrToResID(qrCode));
                    query3.addValueEventListener(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {
                            for (DataSnapshot ds : snapshot.getChildren())
                            {
                                FoodDrink result = ds.getValue(FoodDrink.class);
                                if (!(list_parent.contains(result.getCategory())))
                                {
                                    list_parent.add(result.getCategory());
                                    list_child.put(list_parent.get(list_parent.indexOf(result.getCategory())),new ArrayList<FoodDrink>()); // ilk başlığımızı ve onların elemanlarını HashMap sınıfında tutuyoruz
                                    list_child.get(result.getCategory()).add(result);
                                }
                                else if(list_parent.contains(result.getCategory()))
                                {
                                    list_child.get(result.getCategory()).add(result);
                                    //list_child.get(result.getCategory()).get(list_child.get(result.getCategory()).indexOf(result.getName())).concat(String.valueOf(result.getCost()));

                                    //list_child.get(result.getCategory()).add(String.valueOf(result.getCost()));
                                }
                            }
                            expand_adapter = new ExpandListViewAdapter(getContext(), list_parent, list_child);
                            expandlist_view.setAdapter(expand_adapter);  // oluşturduğumuz adapter sınıfını set ediyoruz
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error)
                        {

                        }
                    });
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };
            query2.addListenerForSingleValueEvent(eventListener);
        }



    }
   public static String qrToTableID(String qrCodeIn)
    {
        String result = "";
        int index = qrCodeIn.indexOf("T");
        if(index != -1)
        {
            result = qrCodeIn.substring(index);
        }
        return result;
    }
    public static String qrToResID(String qrCodeIn)
    {
        String result = "";
        int index = qrCodeIn.indexOf("T");
        if (index != -1)
        {
            result= qrCodeIn.substring(0 , index);
        }
        return result;
    }

    private void requestCamera() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            }
        }
    }

    private void bindCameraPreview(@NonNull ProcessCameraProvider cameraProvider) {
        previewView.setPreferredImplementationMode(PreviewView.ImplementationMode.SURFACE_VIEW);

        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.createSurfaceProvider());

        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        .setTargetResolution(new Size(1280, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(getContext()), new QRCodeImageAnalyzer(new QRCodeFoundListener() {
            @Override
            public void onQRCodeFound(String _qrCode) {
                qrCode = _qrCode;
                if(qrRead < 1)
                {PrepareMenu();
                    qrRead++; }
                expandlist_view.setVisibility(View.VISIBLE);
                previewView.setVisibility(View.INVISIBLE);
                btnScan.setVisibility(View.INVISIBLE);
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
               /* int index = qrCode.indexOf("T");
                String resID = null;
                if (index != -1)
                {
                    resID= qrCode.substring(0 , index);
                }*/
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                Query query = db.child("Restaurants").child(qrToResID(qrCode)).child("resName");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        txtRestaurant.setText(snapshot.getValue().toString());
                        txtRestaurant.setVisibility(View.VISIBLE);
                        DatabaseReference add = FirebaseDatabase.getInstance().getReference();
                        add.child("Users").child(mAuth.getUid()).child("currentRestaurant").setValue(qrToResID(qrCode));
                        add.child("Users").child(mAuth.getUid()).child("resTable").setValue(qrToTableID(qrCode));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void qrCodeNotFound() { }
        }));

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, imageAnalysis, preview);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
            }
        }
    }

    private void startCamera() {
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindCameraPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
            }
        }, ContextCompat.getMainExecutor(getActivity()));
    }

}