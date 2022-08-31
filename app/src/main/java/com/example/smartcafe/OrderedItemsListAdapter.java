package com.example.smartcafe;


import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;

import static android.content.ContentValues.TAG;

class OrderedItemListAdapter extends BaseAdapter {

    private ArrayList<FoodDrink> orderedItems;
    private Context context;
    private LayoutInflater layoutInflater;
    private FirebaseAuth mAuth;
    private fragmentOrder newFragment;

    public OrderedItemListAdapter(Context context, ArrayList<FoodDrink> orderedItems){
        this.context = context;
        this.orderedItems = orderedItems;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return orderedItems.size();
    }

    @Override
    public Object getItem(int i) {
        return orderedItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        View customView = layoutInflater.inflate(R.layout.list_order,null);
        TextView txtItem = (TextView) customView.findViewById(R.id.txtItem);
        TextView txtItemId = (TextView) customView.findViewById(R.id.txtItemId);
        TextView txtItemCost = (TextView) customView.findViewById(R.id.txtPrice);
        Button btnDelete = (Button) customView.findViewById(R.id.btnDelete);

        mAuth = FirebaseAuth.getInstance();
        txtItem.setText(orderedItems.get(i).getName());
        txtItemId.setText(orderedItems.get(i).getItemID());
        txtItemCost.setText(String.valueOf(orderedItems.get(i).getCost()).concat("₺"));

        btnDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //path:"/OrdersInProgress/9JuppRS2LlYEwWJ3Sy6Yl6v7Roq2/-MWyfMsKQf3KdFUkhVpg"
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/OrdersInProgress/" + mAuth.getUid().toString() + "/" + txtItemId.getText().toString());
                ref.removeValue();
                showMsg(txtItem.getText().toString() + " was successfully removed from the cart",1000);
                orderedItems.clear();
                loadFragment(new fragmentOrder());

            /*    Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    }
                },2000);*/
            }
        });

        return customView;
    }
    private void loadFragment(Fragment fragment) {

        FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void showMsg(String msg, final long duration) {
        final Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
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