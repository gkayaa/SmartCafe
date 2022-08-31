package com.example.smartcafe;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class OrderPopupListAdapter extends BaseAdapter
{
    private ArrayList<FoodDrink> items;
    private Context context;
    private LayoutInflater layoutInflater;
    private FirebaseAuth mAuth;

    public OrderPopupListAdapter(Context context, ArrayList<FoodDrink> items){
        this.context = context;
        this.items = items;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent)
    {
        View popView = layoutInflater.inflate(R.layout.list_ordereditems,null);
        TextView txtItemName = popView.findViewById(R.id.txtPopupListItemName);
        TextView txtItemCost = popView.findViewById(R.id.txtPopupListItemCost);

        mAuth = FirebaseAuth.getInstance();

        txtItemName.setText(items.get(i).getName());
        txtItemCost.setText(String.valueOf(items.get(i).getCost()).concat("₺"));


        return popView;
    }
}
