package com.example.smartcafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class SplashActivity extends AppCompatActivity {

    /*
    FoodDrink item1;
    FoodDrink item2;
    FoodDrink item3;
    FoodDrink item4;
    FoodDrink item5;
    FoodDrink item6;
    FoodDrink item7;
    FoodDrink item8;
    FoodDrink item9;
    FoodDrink item10;
    FoodDrink item11;
    FoodDrink item12;
    FoodDrink item13;
    FoodDrink item14;
    FoodDrink item15;
    FoodDrink item16;
    FoodDrink item17;
    FoodDrink item18;
    FoodDrink item19;
    FoodDrink item20;
    FoodDrink item21;
    FoodDrink item22;
    FoodDrink item23;
    FoodDrink item24;
    FoodDrink item25;
    FoodDrink item26;
    FoodDrink item27;
    FoodDrink item28;
    FoodDrink item29;
    FoodDrink item30;
    FoodDrink item31;
    FoodDrink item32;
    FoodDrink item33;
    FoodDrink item34;
    FoodDrink item35;
    FoodDrink item36;
    FoodDrink item37;
    FoodDrink item38;
    FoodDrink item39;
    FoodDrink item40;
    FoodDrink[] fd;
    RestaurantMenu menu1;
    Restaurant rest1;
    DatabaseReference ref;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {

                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = "Token returned as: " + task.getResult();

                        // Log and toast
                        String msg = token;
                        Log.d("TAG", msg);

                    }
                });

        if(MyConnectivityChecker.isConnected(this))
        {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this,AuthActivity.class));
                    overridePendingTransition(R.anim.anim_slide_in_right,
                            R.anim.anim_slide_out_right);
                    finish();
                }
            },2500);
        }
        else
        {
            showAlertDialog(SplashActivity.this, "Connection Not Found !",
                    "Make sure that you are connected to the Internet", false);
        }

        /*ref = FirebaseDatabase.getInstance().getReference();
        item1 = new FoodDrink("Coke",4.0,"Beverages","Res1","Res1Item1");
        item2 = new FoodDrink("Soda",2.5,"Beverages","Res1","Res1Item2");
        item3 = new FoodDrink("Ayran",3.0,"Beverages","Res1","Res1Item3");
        item4 = new FoodDrink("Water",2.0,"Beverages","Res1","Res1Item4");
        item5 = new FoodDrink("CheeseBurger Menu",26.75,"Burgers","Res1","Res1Item5");
        item6 = new FoodDrink("Chief Special Burger Menu",28.5,"Burgers","Res1","Res1Item6");
        item7 = new FoodDrink("Homemade Burger Menu",25.5,"Burgers","Res1","Res1Item7");
        item8 = new FoodDrink("Jumbo CheeseBurger Menu",38.25,"Burgers","Res1","Res1Item8");
        item9 = new FoodDrink("Texas Hot Burger Menu",25.25,"Burgers","Res1","Res1Item9");
        item10 = new FoodDrink("Italian Sandwich",20.95,"Sandwiches","Res1","Res1Item10");
        item11 = new FoodDrink("Steak & Cheese Sandwich",29.50,"Sandwiches","Res1","Res1Item11");
        item12 = new FoodDrink("Meatball Sandwich",24.50,"Sandwiches","Res1","Res1Item12");
        item13 = new FoodDrink("Chicken Breast Sandwich",20.95,"Sandwiches","Res1","Res1Item13");
        item14 = new FoodDrink("Vegetarian Sandwich",17.50,"Sandwiches","Res1","Res1Item14");
        item15 = new FoodDrink("Tuna Fish Sandwich",24.0,"Sandwiches","Res1","Res1Item15");
        item16 = new FoodDrink("Spicy Italian Salad",24.50,"Salads","Res1","Res1Item16");
        item17 = new FoodDrink("Mozarella Cheese Salad",24.50,"Salads","Res1","Res1Item17");
        item18 = new FoodDrink("Steak & Cheese Salad",34.90,"Salads","Res1","Res1Item18");
        item19 = new FoodDrink("Chickem Ham Salad",28.90,"Salads","Res1","Res1Item19");
        item20 = new FoodDrink("Roast Beef Salad",33.50,"Salads","Res1","Res1Item20");
        item21 = new FoodDrink("Vegetarian Salad",21.50,"Salads","Res1","Res1Item21");
        item22 = new FoodDrink("Tuna Fish Salad",28.50,"Salads","Res1","Res1Item22");
        item23 = new FoodDrink("Belgian Waffle",23.50,"Desserts & Waffles","Res1","Res1Item23");
        item24 = new FoodDrink("Bubble Waffle",24.0,"Desserts & Waffles","Res1","Res1Item24");
        item25 = new FoodDrink("Brownie Cake",18.0,"Desserts & Waffles","Res1","Res1Item25");
        item26 = new FoodDrink("Souffle",19.90,"Desserts & Waffles","Res1","Res1Item26");
        item27 = new FoodDrink("Chocolate Donut",9.50,"Desserts & Waffles","Res1","Res1Item27");
        item28 = new FoodDrink("Chocolate Muffin",12.0,"Desserts & Waffles","Res1","Res1Item28");
        item29 = new FoodDrink("Lentil Soup",13.0,"Soups","Res1","Res1Item29");
        item30 = new FoodDrink("Chicken Soup",15.50,"Soups","Res1","Res1Item30");
        item31 = new FoodDrink("Tripe Soup",18.0,"Soups","Res1","Res1Item31");
        item32 = new FoodDrink("Onion Soup",16.5,"Soups","Res1","Res1Item32");
        item33 = new FoodDrink("Vegetarian Soup",12.50,"Soups","Res1","Res1Item33");
        item34 = new FoodDrink("Fried Potatoes",7.50,"Fried Foods","Res1","Res1Item34");
        item35 = new FoodDrink("Chicken Nuggets",9.90,"Fried Foods","Res1","Res1Item35");
        item36 = new FoodDrink("Crispy Onion",11.50,"Fried Foods","Res1","Res1Item36");
        item37 = new FoodDrink("Chicken Wings",13.90,"Fried Foods","Res1","Res1Item37");
        item38 = new FoodDrink("Spicy Wings",14.90,"Fried Foods","Res1","Res1Item38");
        item39 = new FoodDrink("Ice-Tea",4.0,"Beverages","Res1","Res1Item39");
        item40 = new FoodDrink("MilkShake",6.50,"Beverages","Res1","Res1Item40");
        menu1 = new RestaurantMenu("Menu1","Res1");

        rest1 = new Restaurant("Res1","Menu1","Smart Restaurant & Cafe",40.4351,29.14943);
        ref.child("Restaurants").child(rest1.getResID()).setValue(rest1);
        ref.child("Menus").child(menu1.getResID()).setValue(menu1);

        ref.child("FoodsDrinks").child(item1.getItemID()).setValue(item1);
        ref.child("FoodsDrinks").child(item2.getItemID()).setValue(item2);
        ref.child("FoodsDrinks").child(item3.getItemID()).setValue(item3);
        ref.child("FoodsDrinks").child(item4.getItemID()).setValue(item4);
        ref.child("FoodsDrinks").child(item5.getItemID()).setValue(item5);
        ref.child("FoodsDrinks").child(item6.getItemID()).setValue(item6);
        ref.child("FoodsDrinks").child(item7.getItemID()).setValue(item7);
        ref.child("FoodsDrinks").child(item8.getItemID()).setValue(item8);
        ref.child("FoodsDrinks").child(item9.getItemID()).setValue(item9);
        ref.child("FoodsDrinks").child(item10.getItemID()).setValue(item10);
        ref.child("FoodsDrinks").child(item11.getItemID()).setValue(item11);
        ref.child("FoodsDrinks").child(item12.getItemID()).setValue(item12);
        ref.child("FoodsDrinks").child(item13.getItemID()).setValue(item13);
        ref.child("FoodsDrinks").child(item14.getItemID()).setValue(item14);
        ref.child("FoodsDrinks").child(item15.getItemID()).setValue(item15);
        ref.child("FoodsDrinks").child(item16.getItemID()).setValue(item16);
        ref.child("FoodsDrinks").child(item17.getItemID()).setValue(item17);
        ref.child("FoodsDrinks").child(item18.getItemID()).setValue(item18);
        ref.child("FoodsDrinks").child(item19.getItemID()).setValue(item19);
        ref.child("FoodsDrinks").child(item20.getItemID()).setValue(item20);
        ref.child("FoodsDrinks").child(item21.getItemID()).setValue(item21);
        ref.child("FoodsDrinks").child(item22.getItemID()).setValue(item22);
        ref.child("FoodsDrinks").child(item23.getItemID()).setValue(item23);
        ref.child("FoodsDrinks").child(item24.getItemID()).setValue(item24);
        ref.child("FoodsDrinks").child(item25.getItemID()).setValue(item25);
        ref.child("FoodsDrinks").child(item26.getItemID()).setValue(item26);
        ref.child("FoodsDrinks").child(item27.getItemID()).setValue(item27);
        ref.child("FoodsDrinks").child(item28.getItemID()).setValue(item28);
        ref.child("FoodsDrinks").child(item29.getItemID()).setValue(item29);
        ref.child("FoodsDrinks").child(item30.getItemID()).setValue(item30);
        ref.child("FoodsDrinks").child(item31.getItemID()).setValue(item31);
        ref.child("FoodsDrinks").child(item32.getItemID()).setValue(item32);
        ref.child("FoodsDrinks").child(item33.getItemID()).setValue(item33);
        ref.child("FoodsDrinks").child(item34.getItemID()).setValue(item34);
        ref.child("FoodsDrinks").child(item35.getItemID()).setValue(item35);
        ref.child("FoodsDrinks").child(item36.getItemID()).setValue(item36);
        ref.child("FoodsDrinks").child(item37.getItemID()).setValue(item37);
        ref.child("FoodsDrinks").child(item38.getItemID()).setValue(item38);
        ref.child("FoodsDrinks").child(item39.getItemID()).setValue(item39);
        ref.child("FoodsDrinks").child(item40.getItemID()).setValue(item40);*/
    }


    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        // alertDialog.setIcon((status) ? R.mipmap.ic_launcher : R.mipmap.ic_launcher);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                finish();
                System.exit(0);
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}