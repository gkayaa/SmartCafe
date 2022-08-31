package com.example.smartcafe;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragmentOptions#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragmentOptions extends Fragment {
    View myView;
    ArrayList<Order> list_adapter_orderhistory;
    ListView listOrderHistory;
    OrderHistoryListAdapter orderHistoryAdapter;
    ImageButton btnSignOut;
    ImageButton btnOrderHistory;
    ImageButton btnEditProfile;
    ImageButton btnAddBalance;
    ImageButton btnAbout;
    ImageButton btnCloseOrderHistory;
    TextView txtOrderHistory;
    TextView txtEditProfile;
    TextView txtSignOut;
    TextView txtAddBalance;
    TextView txtAbout;
    TextView txtNoOrders;
    TextView txtUserBalance;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragmentOptions() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragmentOptions.
     */
    // TODO: Rename and change types and number of parameters
    public static fragmentOptions newInstance(String param1, String param2) {
        fragmentOptions fragment = new fragmentOptions();
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
                             Bundle savedInstanceState) {
        /*if(isLocated == false)
        {
            btnLocation.setImageResource(R.drawable.ic_baseline_location_disabled_24);
        }
        else
        {
            btnLocation.setImageResource(R.drawable.ic_baseline_my_location_24);
            btnLocation.setClickable(false);
        }*/
        myView = inflater.inflate(R.layout.fragment_options, container, false);
        listOrderHistory = myView.findViewById(R.id.listOrderHistory);
        list_adapter_orderhistory = new ArrayList<>();
        txtSignOut = myView.findViewById(R.id.txtSignOut);
        txtAbout = myView.findViewById(R.id.txtAbout);
        txtAddBalance = myView.findViewById(R.id.txtBalance);
        txtEditProfile = myView.findViewById(R.id.txtProfile);
        txtOrderHistory = myView.findViewById(R.id.txtOrderHistory);
        orderHistoryAdapter = new OrderHistoryListAdapter(getContext(),list_adapter_orderhistory);
        txtNoOrders = myView.findViewById(R.id.txtNoOrders);
        txtUserBalance = myView.findViewById(R.id.txtUserBalance);

        Animation fadeAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.anim_slide_in_right);

        btnCloseOrderHistory = myView.findViewById(R.id.btnCloseOrderHistory);
        btnCloseOrderHistory.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                txtUserBalance.setVisibility(View.VISIBLE);
                btnSignOut.setVisibility(View.VISIBLE);
                btnAbout.setVisibility(View.VISIBLE);
                btnAddBalance.setVisibility(View.VISIBLE);
                btnEditProfile.setVisibility(View.VISIBLE);
                btnSignOut.setVisibility(View.VISIBLE);
                btnOrderHistory.setVisibility(View.VISIBLE);
                txtAbout.setVisibility(View.VISIBLE);
                txtAddBalance.setVisibility(View.VISIBLE);
                txtOrderHistory.setVisibility(View.VISIBLE);
                txtEditProfile.setVisibility(View.VISIBLE);
                txtSignOut.setVisibility(View.VISIBLE);
                listOrderHistory.setVisibility(View.INVISIBLE);
                btnCloseOrderHistory.setVisibility(View.INVISIBLE);
                txtNoOrders.setVisibility(View.INVISIBLE);
                list_adapter_orderhistory.clear();
            }
        });

        btnAbout = myView.findViewById(R.id.btnAbout);

        btnAddBalance = myView.findViewById(R.id.btnBalance);

        btnEditProfile = myView.findViewById(R.id.btnProfile);


        btnSignOut = myView.findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(),AuthActivity.class));
            }
        });

        btnOrderHistory = myView.findViewById(R.id.btnOrderHistory);
        btnOrderHistory.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                txtUserBalance.setVisibility(View.INVISIBLE);
                btnSignOut.setVisibility(View.INVISIBLE);
                btnAbout.setVisibility(View.INVISIBLE);
                btnOrderHistory.setVisibility(View.INVISIBLE);
                btnAddBalance.setVisibility(View.INVISIBLE);
                btnEditProfile.setVisibility(View.INVISIBLE);
                btnSignOut.setVisibility(View.INVISIBLE);
                txtAbout.setVisibility(View.INVISIBLE);
                txtAddBalance.setVisibility(View.INVISIBLE);
                txtOrderHistory.setVisibility(View.INVISIBLE);
                txtEditProfile.setVisibility(View.INVISIBLE);
                txtSignOut.setVisibility(View.INVISIBLE);
                btnCloseOrderHistory.setVisibility(View.VISIBLE);

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("OrdersForUsers").child(FirebaseAuth.getInstance().getUid().toString());
                Query qRef = ref;
                qRef.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        if(snapshot.exists())
                        {
                            for (DataSnapshot ssHistory: snapshot.getChildren())
                            {
                                Order temp = ssHistory.getValue(Order.class);
                                HashMap<String,FoodDrink> ordered = new HashMap();
                                for (DataSnapshot ssItems: ssHistory.getChildren())
                                {
                                    if(ssItems.hasChildren())
                                    {
                                        FoodDrink item = ssItems.getValue(FoodDrink.class);
                                        ordered.put(item.getItemID(),item);
                                    }
                                }
                                temp.setItemsOrdered(ordered);
                                list_adapter_orderhistory.add(temp);
                            }
                            listOrderHistory.setAdapter(orderHistoryAdapter);
                        }
                        else
                        {
                            txtNoOrders.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });


                listOrderHistory.setVisibility(View.VISIBLE);
            }
        });
        
        Query balanceQry = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid().toString()).child("userBalance");
        balanceQry.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double userBalance = snapshot.getValue(double.class);
                txtUserBalance.setText(txtUserBalance.getText().toString().concat(" " + String.valueOf(userBalance)) + "₺");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnAddBalance.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDialog("Add Balance to Wallet","",R.drawable.logosplash);
                ImageButton btnBalancePopup = inflater.inflate(R.layout.balance_alertdialog, container, false).findViewById(R.id.btnBalancePopup);
            }
        });


        btnEditProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent editProfile = new Intent(getContext(),EditProfileActivity.class);
                /*getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                startActivity(editProfile);*/

                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.anim_slide_in_right, R.anim.anim_slide_out_right).toBundle();
                startActivity(editProfile,bndlanimation);
            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog dialog = new Dialog(getContext());
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.setContentView(R.layout.about_dialog);
                ImageButton btnUnderstood = dialog.findViewById(R.id.btnUnderstood);

                btnUnderstood.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                dialog.show();
            }
        });















        return myView;
    }

    public void showDialog(String title, String des, int icon) {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.add_balance_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        ImageButton btnCompletePayment = dialog.findViewById(R.id.btnCompletePayment);
        ImageButton btnCloseBalancePopup = dialog.findViewById(R.id.btnCloseBalancePopup);

        TextView txtCartName = dialog.findViewById(R.id.txtCartName);
        TextView txtCartNumber = dialog.findViewById(R.id.txtCartNumber);
        TextView txtCartExpiration = dialog.findViewById(R.id.txtExpirationDate);
        TextView txtCartCvc = dialog.findViewById(R.id.txtCvc);
        TextView txtAmount = dialog.findViewById(R.id.txtAmount);

        EditText editCartName = dialog.findViewById(R.id.editCartName);
        EditText editCartNumber = dialog.findViewById(R.id.editCartNumber);
        EditText editCartExpiration = dialog.findViewById(R.id.editCartExpiration);
        EditText editCartCvc = dialog.findViewById(R.id.editCartCvc);
        EditText editAmount = dialog.findViewById(R.id.editAmount);

        btnCompletePayment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(editAmount.getText().toString().isEmpty() || editCartCvc.getText().toString().isEmpty() || editCartExpiration.getText().toString().isEmpty() || editCartName.getText().toString().isEmpty() || editCartNumber.getText().toString().isEmpty())
                {
                    showMsg("Payment confirmation failed. All the fields must be provided and be valid.",2000);
                }
                else
                {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid().toString()).child("userBalance");
                    Query balanceQuery = ref;
                    balanceQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ref.setValue(snapshot.getValue(Double.class) + Double.parseDouble(editAmount.getText().toString()));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    dialog.dismiss();
                    FragmentTransaction transaction = ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.mainFrameLayout, new fragmentOptions());
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        btnCloseBalancePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void showMsg(String msg, final long duration) {
        final Toast toast = Toast.makeText(getContext(), msg, Toast.LENGTH_LONG);
        toast.show();
        Thread t = new Thread() {
            public void run(){
                try {
                    sleep(duration);
                    toast.cancel();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally { }
            }
        };
        t.start();
    }
}