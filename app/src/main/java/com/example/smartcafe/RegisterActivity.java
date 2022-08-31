package com.example.smartcafe;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Selection;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;


public class RegisterActivity extends AppCompatActivity {

    final Context con = this;
    EditText regEmail,regPassword,regName,regPhone,regBirth;
    Button btnRegister;
    FirebaseAuth fAuth;
    DatabaseReference ref;
    User newUser;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private int year, month, dayOfMonth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnRegister = findViewById(R.id.btnRegister);
        regBirth = findViewById(R.id.regBirth);
        regName = findViewById(R.id.regName);
        regEmail = findViewById(R.id.regEmail);
        regPassword = findViewById(R.id.regPassword);
        regPhone = findViewById(R.id.regPhone);
        fAuth = FirebaseAuth.getInstance();

        regBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(RegisterActivity.this, R.style.MyDialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day)
                            {
                                regBirth.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v)
           {

             if(TextUtils.isEmpty(regBirth.getText()) || TextUtils.isEmpty(regEmail.getText()) || TextUtils.isEmpty(regName.getText()) || TextUtils.isEmpty(regPassword.getText()) || TextUtils.isEmpty(regPhone.getText()))
             {
                 final Dialog dialog = new Dialog(con);
                 dialog.setContentView(R.layout.custom_alertdialog);
                 dialog.show();

                 Button btnConfirm = (Button) dialog.findViewById(R.id.btnConfirm);
                 TextView txtAlert = (TextView) dialog.findViewById(R.id.txtTitle);
                 ImageView imgIcon = (ImageView) dialog.findViewById(R.id.imgIcon);

                 txtAlert.setText("All the fields must be provided !");
                 imgIcon.setImageResource(R.drawable.sadhamburger);
                 btnConfirm.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v)
                     {
                         dialog.dismiss();
                     }
                 });
             }

             else if(regPassword.getText().length() < 8)
             {
                 final Dialog dialog = new Dialog(con);
                 dialog.setContentView(R.layout.custom_alertdialog);
                 dialog.show();

                 Button btnConfirm = (Button) dialog.findViewById(R.id.btnConfirm);
                 TextView txtAlert = (TextView) dialog.findViewById(R.id.txtTitle);
                 ImageView imgIcon = (ImageView) dialog.findViewById(R.id.imgIcon);

                 txtAlert.setText("Password length must be at least 8 characters !");
                 imgIcon.setImageResource(R.drawable.sadhamburger);
                 btnConfirm.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v)
                     {
                         dialog.dismiss();
                     }
                 });
             }




             else
             {
                 fAuth.createUserWithEmailAndPassword(regEmail.getText().toString(),regPassword.getText().toString()).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {

                     // Checking if user is registered successfully.
                         if(task.isSuccessful())
                         {
                             ref = FirebaseDatabase.getInstance().getReference();
                             newUser = new User();
                             newUser.setDateOfBirth(regBirth.getText().toString().trim());
                             newUser.setEmail(regEmail.getText().toString().trim());
                             newUser.setName(regName.getText().toString().trim());
                             newUser.setPassword(regPassword.getText().toString().trim());
                             newUser.setPhone(regPhone.getText().toString().trim());
                             newUser.setLatitude(0);
                             newUser.setLongitude(0);
                             newUser.setUserBalance(0.0);
                             newUser.setCurrentRestaurant("");
                             newUser.setResTable("");

                             String email = regEmail.getText().toString();
                             newUser.setUserID(FirebaseAuth.getInstance().getCurrentUser().getUid());
                             ref.child("Users").child(newUser.getUserID()).setValue(newUser);
                             regBirth.setText("");
                             regName.setText("");
                             regEmail.setText("");
                             regPassword.setText("");
                             regPhone.setText("");
                             final Dialog dialog = new Dialog(con);
                             dialog.setContentView(R.layout.custom_alertdialog);
                             dialog.show();

                             Button btnConfirm = (Button) dialog.findViewById(R.id.btnConfirm);
                             TextView txtAlert = (TextView) dialog.findViewById(R.id.txtTitle);
                             ImageView imgIcon = (ImageView) dialog.findViewById(R.id.imgIcon);

                             txtAlert.setText("To Start Eating, You should verify your e-mail by using the link sent. YUM !");
                             imgIcon.setImageResource(R.drawable.eathamburger);
                             FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task)
                                 {
                                     if(task.isSuccessful())
                                     {

                                     }
                                     else
                                     {
                                         Toast.makeText(RegisterActivity.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                     }

                                 }
                             });

                             btnConfirm.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     dialog.dismiss();
                                     startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                                     finish();
                                 }
                             });
                         }
                         else
                         {
                            final Dialog dialog = new Dialog(con);
                            dialog.setContentView(R.layout.custom_alertdialog);
                            dialog.show();

                            Button btnConfirm = (Button) dialog.findViewById(R.id.btnConfirm);
                            TextView txtAlert = (TextView) dialog.findViewById(R.id.txtTitle);
                            ImageView imgIcon = (ImageView) dialog.findViewById(R.id.imgIcon);

                            txtAlert.setText("E-Mail Address is invalid or in use");
                            imgIcon.setImageResource(R.drawable.sadhamburger);

                            btnConfirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();

                                }
                            });
                        }
                     }
                 });

             }


           }
       });




































    }































































}