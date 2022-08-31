package com.example.smartcafe;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderHistoryListAdapter extends BaseAdapter
{
    private ArrayList<Order> userOrders;
    private Context context;
    private LayoutInflater layoutInflater;
    private FirebaseAuth mAuth;

    public OrderHistoryListAdapter(Context context, ArrayList<Order> userOrders){
        this.context = context;
        this.userOrders = userOrders;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return userOrders.size();
    }

    @Override
    public Object getItem(int i) {
        return userOrders.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent)
    {
        View ordView = layoutInflater.inflate(R.layout.list_orderhistory,null);
        TextView txtOrderDate = ordView.findViewById(R.id.txtOrderDate);
        TextView txtOrderRestaurant = ordView.findViewById(R.id.txtOrderRestaurant);
        TextView txtOrderPrice = ordView.findViewById(R.id.txtOrderPrice);
        ImageButton btnOrderDetails = ordView.findViewById(R.id.btnOrderDetails);

        mAuth = FirebaseAuth.getInstance();
        ArrayList<FoodDrink> items = new ArrayList<>();
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.order_history_popup);
        ListView popupItemsList = dialog.findViewById(R.id.list_popup_items);
        ImageButton btnClosePopup = dialog.findViewById(R.id.btnCloseBalancePopup);
        TextView txtPopupTotalCost = dialog.findViewById(R.id.txtPopupTotal);

        txtOrderDate.setText(userOrders.get(i).getDate());
        txtOrderPrice.setText(String.valueOf(userOrders.get(i).getTotalCost()).concat("₺"));
        txtOrderRestaurant.setText(userOrders.get(i).getRestaurant());
        OrderPopupListAdapter adpt = new OrderPopupListAdapter(context,items);

        btnOrderDetails.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Query qRy2 = FirebaseDatabase.getInstance().getReference().child("OrdersForUsers").child(mAuth.getUid()).child(userOrders.get(i).getOrderId());
                qRy2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        txtPopupTotalCost.setText("Total :".concat(String.valueOf(snapshot.child("totalCost").getValue(Double.class)).concat("₺")));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Query qRy = FirebaseDatabase.getInstance().getReference().child("OrdersForUsers").child(mAuth.getUid()).child(userOrders.get(i).getOrderId()).child("itemsOrdered");
                qRy.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        for (DataSnapshot sslola: snapshot.getChildren())
                        {
                            FoodDrink temp = sslola.getValue(FoodDrink.class);

                            items.add(temp);
                        }
                        popupItemsList.setAdapter(adpt);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });
                dialog.show();
                btnClosePopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        items.clear();
                    }
                });
            }
        });









        return ordView;
    }
}
