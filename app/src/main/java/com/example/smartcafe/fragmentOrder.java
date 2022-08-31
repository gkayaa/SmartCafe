package com.example.smartcafe;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.IntentCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragmentOrder#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragmentOrder extends Fragment {

    private static Context context;
    HashMap<String,FoodDrink> orderedItemsForUserOrder;
    Order orderForUser;
    Order orderForRestaurant;
    View customView;
    public ArrayList<FoodDrink> list_adapter_array;
    OrderedItemListAdapter adapter;
    ListView listOrder;
    TextView txtNoItem;
    Button btnComplete;
    TextView txtTotalCost;
    ImageView img;
    int randomOrderNumber = new Random().nextInt(100);

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragmentOrder() {

    }

    public static fragmentOrder newInstance(String param1, String param2) {
        fragmentOrder fragment = new fragmentOrder();
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

    public static String retTextToX(String inputText)
    {
        String result = "";
        int index = inputText.indexOf("₺");
        if (index != -1)
        {
            result= inputText.substring(0 , index);
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendOrderReceivedNotification()
    {
        NotificationChannel channel = new NotificationChannel("n","n", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = context.getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"n").setContentText("SmartCafe").setSmallIcon(R.drawable.logosplash).setAutoCancel(true).setContentText("Your order was created. When it is ready, you will get a notification.").setStyle(new NotificationCompat.BigTextStyle().bigText("Your order was created. When it is ready, you will get a notification."));
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        String notfId = "not".concat(FirebaseAuth.getInstance().getUid()).concat(String.valueOf(new Random().nextInt()));
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        DatabaseReference notf = FirebaseDatabase.getInstance().getReference().child("Notifications").child(FirebaseAuth.getInstance().getUid().toString()).child(notfId);
        Notification pushNotf = new Notification(notfId,"Your order was created. When it is ready, you will get a notification",date);
        notf.setValue(pushNotf);
        managerCompat.notify(999,builder.build());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        context = getContext();
        customView = inflater.inflate(R.layout.fragment_order, container, false);
        orderForUser = new Order();
        orderForRestaurant = new Order();
        orderedItemsForUserOrder = new HashMap<>();
        img = customView.findViewById(R.id.imgNoItem);
        txtNoItem = customView.findViewById(R.id.txtNoItem);
        listOrder = customView.findViewById(R.id.listOrder);
        btnComplete = customView.findViewById(R.id.btnComplete);
        TextView txtTotalCost = customView.findViewById(R.id.txtTotalCost);
        EditText txtOrderNote = customView.findViewById(R.id.txtOrderNote);

        FirebaseAuth currentUser = FirebaseAuth.getInstance();
        FirebaseDatabase dbase2 = FirebaseDatabase.getInstance();
        DatabaseReference refComplete2 = dbase2.getReference().child("OrdersInProgress");
        Query queryComplete = refComplete2.child(currentUser.getUid());
        queryComplete.addValueEventListener(new ValueEventListener()
        {
            double totalCost = 0.0;
            @Override
            public void onDataChange(@NonNull DataSnapshot ssInitial)
            {
                for (DataSnapshot dsInitial : ssInitial.getChildren())
                {
                    FoodDrink fdInitial = dsInitial.getValue(FoodDrink.class);
                    if(dsInitial != null)
                    {
                        totalCost += fdInitial.getCost();
                        txtTotalCost.setVisibility(View.VISIBLE);
                        btnComplete.setVisibility(View.VISIBLE);
                        txtOrderNote.setVisibility(View.VISIBLE);
                    }
                }
                txtTotalCost.setText(String.valueOf(totalCost) + "₺");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });


        btnComplete.setOnClickListener(new View.OnClickListener()
        {
            int randomOrderNumber = new Random().nextInt(100);
            String orderedRestaurant = "";
            FirebaseAuth mAuth = FirebaseAuth.getInstance();

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v)
            {
                DatabaseReference refToCheckIfBalanceEnough = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid().toString()).child("userBalance");
                Query qToCheckIfBalanceEnough = refToCheckIfBalanceEnough;
                qToCheckIfBalanceEnough.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        double userBal = snapshot.getValue(double.class);
                        if(userBal >= Double.parseDouble(retTextToX(txtTotalCost.getText().toString())))
                        {
                            refToCheckIfBalanceEnough.setValue((snapshot.getValue(double.class))-(Double.parseDouble(retTextToX(txtTotalCost.getText().toString()))));
                            sendOrderReceivedNotification();
                            FirebaseDatabase fd1 = FirebaseDatabase.getInstance();
                            DatabaseReference refC = fd1.getReference().child("OrdersInProgress");
                            Query queryComplete2 = refC.child(mAuth.getUid());
                            queryComplete2.addValueEventListener(new ValueEventListener()
                            {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot ssForUsers)
                                {
                                    for (DataSnapshot dsForUsers : ssForUsers.getChildren())
                                    {
                                        FoodDrink fdForUsers = dsForUsers.getValue(FoodDrink.class);
                                        DatabaseReference rootForUsers = FirebaseDatabase.getInstance().getReference();
                                        DatabaseReference refForUsers = rootForUsers.child("OrdersForUsers").child(mAuth.getUid().toString()).child(mAuth.getUid().concat("!" + randomOrderNumber));
                                        //refForUsers.setValue(fdForUsers);
                                        orderedItemsForUserOrder.put(fdForUsers.getItemID(),fdForUsers);
                                        String restaurantIdentification = fdForUsers.getMenuID();
                                        //DatabaseReference ordrRes = FirebaseDatabase.getInstance().getReference().child("OrdersForUsers").child(mAuth.getUid()).child(mAuth.getUid().concat("!" + randomOrderNumber)).child("Restaurant");
                                        Query qOrdrRes = FirebaseDatabase.getInstance().getReference().child("Restaurants").child(restaurantIdentification).child("resName");
                                        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
                                        String date = df.format(Calendar.getInstance().getTime());
                                        //DatabaseReference costOrdr = FirebaseDatabase.getInstance().getReference().child("OrdersForUsers").child(mAuth.getUid()).child((mAuth.getUid().concat("!" + randomOrderNumber))).child("TotalCost");
                                        //costOrdr.setValue(Double.parseDouble();
/*
                            DatabaseReference dateOrdr = FirebaseDatabase.getInstance().getReference().child("OrdersForUsers").child(mAuth.getUid()).child((mAuth.getUid().concat("!" + randomOrderNumber))).child("OrderDate");
                            dateOrdr.setValue(date);*/
                                        qOrdrRes.addValueEventListener(new ValueEventListener()
                                        {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshotlaylaylay)
                                            {
                                                orderedRestaurant = snapshotlaylaylay.getValue(String.class);
                                                orderForUser.setItemsOrdered(orderedItemsForUserOrder);
                                                orderForUser.setDate(date);
                                                orderForUser.setRestaurant(orderedRestaurant);
                                                orderForUser.setTotalCost(Double.parseDouble(retTextToX(txtTotalCost.getText().toString())));
                                                orderForUser.setOrderId(mAuth.getUid().concat("!" + randomOrderNumber));
                                                refForUsers.setValue(orderForUser);

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error)
                                            {

                                            }
                                        });


                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error)
                                {

                                }
                            });

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                            DatabaseReference refToGetResId = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid());
                            Query queryToGetResId = refToGetResId.child("currentRestaurant");
                            queryToGetResId.addValueEventListener((new ValueEventListener()
                            {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot)
                                {
                                    String resID = snapshot.getValue().toString();
                                    DatabaseReference refToGetResId2 = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid());
                                    Query queryToGetResId2 = refToGetResId2.child("resTable");
                                    queryToGetResId2.addValueEventListener(new ValueEventListener()
                                    {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot2)
                                        {
                                            String resTable = snapshot2.getValue().toString();
                                            DatabaseReference refForRestaurants = FirebaseDatabase.getInstance().getReference().child("OrdersForUsers").child(mAuth.getUid()).child(mAuth.getUid().concat(("!" + randomOrderNumber))).child("itemsOrdered");
                                            Query queryForRestaurants = refForRestaurants;
                                            queryForRestaurants.addValueEventListener(new ValueEventListener()
                                            {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot ssForRestaurants)
                                                {
                                                    for (DataSnapshot dsForRestaurants : ssForRestaurants.getChildren())
                                                    {
                                                        FoodDrink fdForRestaurants = dsForRestaurants.getValue(FoodDrink.class);
                                                        DatabaseReference restaurantOrder = FirebaseDatabase.getInstance().getReference().child("OrdersForRestaurants").child(resID).child(resTable).child(fdForRestaurants.getItemID());
                                                        restaurantOrder.setValue(fdForRestaurants);
                                                    }
                                                    DatabaseReference noteRef = FirebaseDatabase.getInstance().getReference().child("OrdersForRestaurants").child(resID).child(resTable).child("OrderNote");
                                                    noteRef.setValue(txtOrderNote.getText().toString());
                                                    DatabaseReference tokenRef = FirebaseDatabase.getInstance().getReference().child("OrdersForRestaurants").child(resID).child(resTable).child("userToken");
                                                    Query qToken = tokenRef;
                                                    qToken.addListenerForSingleValueEvent(new ValueEventListener()
                                                    {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot shotToken)
                                                        {
                                                            if(shotToken.exists())
                                                            {
                                                                String oldToken = shotToken.getValue(String.class);
                                                                tokenRef.setValue(oldToken.concat(FirebaseInstanceId.getInstance().getToken().toString()));
                                                            }
                                                            else
                                                            {
                                                                tokenRef.setValue(FirebaseInstanceId.getInstance().getToken().toString());
                                                            }
                                                        }
                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error)
                                                        {

                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error)
                                                {

                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error)
                                        {

                                        }
                                    });
                                }
                                public void onCancelled(DatabaseError error)
                                {

                                }
                            }));
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////







                            final Dialog dialog = new Dialog(getContext());
                            dialog.setContentView(R.layout.successful_alertdialog);

                            TextView txtAlert = (TextView) dialog.findViewById(R.id.txtTitle);
                            ImageView imgIcon = (ImageView) dialog.findViewById(R.id.imgIcon);

                            txtAlert.setText("Order completed successfully \n      Redirecting...");
                            imgIcon.setImageResource(R.drawable.eathamburger);

                            dialog.show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                }
                            },2000);

                            DatabaseReference deleteRef = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference refToRemove = deleteRef.child("OrdersInProgress").child(currentUser.getUid());
                            refToRemove.removeValue();
                        }
                        else
                        {
                            final Dialog dialog = new Dialog(getContext());
                            dialog.setContentView(R.layout.balance_alertdialog);
                            View diffView = inflater.inflate(R.layout.fragment_options, container, false);
                            ImageButton close = dialog.findViewById(R.id.btnCloseBalanceErrorPopup);
                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            TextView txtError = (TextView) dialog.findViewById(R.id.txtTitle);
                            ImageButton btnBalance = (ImageButton) dialog.findViewById(R.id.btnBalancePopup);
                            btnBalance.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v)
                                {
                                    dialog.dismiss();
                                    FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.mainFrameLayout, new fragmentOptions());
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                }
                            });
                            txtError.setText("Insufficient balance");

                            dialog.show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });

            }
        });

        list_adapter_array = new ArrayList<FoodDrink>();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase db3 = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db3.getReference().child("OrdersInProgress");
        Query query3 = myRef.child(mAuth.getUid());
        query3.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotAdapter)
            {
                for (DataSnapshot dsAdapter : snapshotAdapter.getChildren())
                {
                    FoodDrink queryRes = dsAdapter.getValue(FoodDrink.class);
                    list_adapter_array.add(queryRes);
                }

                adapter = new OrderedItemListAdapter(getContext(),list_adapter_array);
                listOrder.setAdapter(adapter);
                if(list_adapter_array.isEmpty())
                {

                }
                else if(list_adapter_array.isEmpty() == false)
                {
                    img.setVisibility(View.INVISIBLE);
                    txtNoItem.setVisibility(View.INVISIBLE);
                    listOrder.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });


        return customView;
    }



    public static void restart(Context context){
        fragmentOrder.context = context;
        Intent mainIntent = IntentCompat.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_LAUNCHER);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(mainIntent);
        System.exit(0);
    }
}