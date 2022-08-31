package com.example.smartcafe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

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

public class ExpandListViewAdapter extends BaseExpandableListAdapter {


    public List<String> list_parent;
    public HashMap<String, ArrayList<FoodDrink>> list_child;
    public Context context;
    public TextView txtGroup;
    public ImageView categoryImage;
    public TextView txt_childName;
    public TextView txt_childCost;
    public TextView txt_childId;
    public Button btn_addToCart;
    public LayoutInflater inflater;
    private FirebaseAuth mAuth;

    @Override
    public int getGroupCount() {

        return list_parent.size();
    }

    public ExpandListViewAdapter(Context context, List<String> list_parent, HashMap<String, ArrayList<FoodDrink>> list_child)
    {
        this.context = context;
        this.list_parent = list_parent;
        this.list_child = list_child;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return list_child.get(list_parent.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {

        return list_parent.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return list_child.get(list_parent.get(groupPosition)).get(childPosition);

    }

    @Override
    public long getGroupId(int groupPosition) {

        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {

        return childPosition;
    }

    @Override
    public boolean hasStableIds() {

        return false;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View view, ViewGroup parent) {
        String title_name = (String)getGroup(groupPosition);

        if(view == null)
        {
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_group,null);
        }

        txtGroup = (TextView)view.findViewById(R.id.txtCategory);
        txtGroup.setText(title_name);

        categoryImage = view.findViewById(R.id.imgCategory);
        if(txtGroup.getText().toString().equals("Beverages"))
        {
            categoryImage.setImageResource(R.drawable.beverage);
        }
        else if(txtGroup.getText().toString().equals("Sandwiches"))
        {
            categoryImage.setImageResource(R.drawable.sandwich);
        }
        else if(txtGroup.getText().toString().equals("Salads"))
        {
            categoryImage.setImageResource(R.drawable.salad);
        }
        else if(txtGroup.getText().toString().equals("Desserts & Waffles"))
        {
            categoryImage.setImageResource(R.drawable.dessert);
        }
        else if(txtGroup.getText().toString().equals("Soups"))
        {
            categoryImage.setImageResource(R.drawable.soup);
        }
        else if(txtGroup.getText().toString().equals("Fried Foods"))
        {
            categoryImage.setImageResource(R.drawable.fried);
        }
        else if(txtGroup.getText().toString().equals("Burgers"))
        {
            categoryImage.setImageResource(R.drawable.burger);
        }


        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent)
    {
        FoodDrink selectedItem = (FoodDrink) getChild(groupPosition, childPosition);
        // kaçıncı pozisyonda ise başlığımızın elemanı onun ismini alarak string e atıyoruz
        //String txt_child_name = (String)getChild(groupPosition, 0);
       // String txt_child_cost = (String)getChild(groupPosition, childPosition);

        if(view==null)
        {
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_child, null);
            // fonksiyon adından da anlaşılacağı gibi parent a bağlı elemanlarının layoutunu inflate ediyoruz böylece o görüntüye ulaşmış oluyoruz
        }

        // listview_child ulaştığımıza göre içindeki bileşeni de kullanabiliyoruz daha sonradan view olarak return ediyoruz
        txt_childName = (TextView)view.findViewById(R.id.txtItem);
        txt_childCost = (TextView)view.findViewById(R.id.txtPrice);
        txt_childId = (TextView)view.findViewById(R.id.txtItemId);
        btn_addToCart = (Button) view.findViewById(R.id.btnDelete);
        txt_childName.setText(selectedItem.getName());
        txt_childCost.setText(selectedItem.getCost() + "₺");
        txt_childId.setText(selectedItem.getItemID());
        mAuth = FirebaseAuth.getInstance();

        btn_addToCart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference cartRefffff = cartRef.child("OrdersInProgress").child(mAuth.getUid()).child(selectedItem.getItemID().toString());
                cartRefffff.setValue(selectedItem);

               /* DatabaseReference costRef = FirebaseDatabase.getInstance().getReference().child("OrdersInProgress").child(mAuth.getUid()).child("TotalCost");
                Query qCostRef = costRef;
                costRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists() == true)
                        {
                            costRef.setValue(snapshot.getValue(Double.class) + selectedItem.getCost());
                        }
                        else
                        {
                            costRef.setValue(selectedItem.getCost());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });*/

               /* DatabaseReference refffffffff = FirebaseDatabase.getInstance().getReference();
                Query queryCart = refffffffff.child("FoodsDrinks").orderByChild("itemID").equalTo(txt_childId.getText().toString());
                queryCart.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot ssCart)
                    {
                        for (DataSnapshot dsCart : ssCart.getChildren())
                        {
                            FoodDrink result = dsCart.getValue(FoodDrink.class);
                            refffffffff.child("OrdersInProgress").child(mAuth.getUid().toString()).child(result.getItemID()).setValue(result);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });*/
                showMsg(selectedItem.getName() + " was successfully added to cart",1000);


            }
        });


        return view;
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
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;  // expandablelistview de tıklama yapabilmek için true olmak zorunda
    }



}