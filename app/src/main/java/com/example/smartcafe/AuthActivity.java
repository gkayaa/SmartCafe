package com.example.smartcafe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthActivity extends AppCompatActivity
{
    EditText inpEmail,inpPassword;
    Button btnLogin;
    FirebaseAuth mAuth;
    final Context con = this;
    TextView register;
    TextView forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        inpEmail = findViewById(R.id.regEmail);
        inpPassword = findViewById(R.id.inpPassword);
        btnLogin = findViewById(R.id.btnLogin);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent i = new Intent(AuthActivity.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
            // User is signed out
        }

        //Authenticate the user if needed
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                inpEmail.setText("goktugkaya97@gmail.com");
                inpPassword.setText("123123123");
                String email = inpEmail.getText().toString().trim();
                String password = inpPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    inpEmail.setError("Please Enter Your E-Mail Address !");
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    inpPassword.setError("Please Enter Your Password !");
                    return;
                }

                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            final Dialog dialog = new Dialog(con);
                            dialog.setContentView(R.layout.successful_alertdialog);

                            TextView txtAlert = (TextView) dialog.findViewById(R.id.txtTitle);
                            ImageView imgIcon = (ImageView) dialog.findViewById(R.id.imgIcon);

                            txtAlert.setText("Authentication Succesful !");
                            imgIcon.setImageResource(R.drawable.eathamburger);

                            dialog.show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    finish();
                                }
                            },1500);
                        }
                        else
                        {
                            inpEmail.setText("");
                            inpPassword.setText("");
                            final Dialog dialog = new Dialog(con);
                            dialog.setContentView(R.layout.custom_alertdialog);

                            Button btnConfirm = (Button) dialog.findViewById(R.id.btnConfirm);
                            TextView txtAlert = (TextView) dialog.findViewById(R.id.txtTitle);
                            ImageView imgIcon = (ImageView) dialog.findViewById(R.id.imgIcon);

                            txtAlert.setText("Authentication Failed");
                            imgIcon.setImageResource(R.drawable.sadhamburger);
                            btnConfirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v)
                                {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    }
                });

            }
        });


        register = (TextView) findViewById(R.id.txtRegister);
        register .setOnClickListener(new View.OnClickListener() {

            public void onClick(View view)
            {
                startActivity(new Intent(AuthActivity.this,RegisterActivity.class));
            }
        });


        forgot = (TextView) findViewById(R.id.txtForgot);
        forgot .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final Dialog dialog = new Dialog(con);
                dialog.setContentView(R.layout.input_alertdialog);
                dialog.show();

                Button btnConfirm = (Button) dialog.findViewById(R.id.btnConfirm);
                EditText txtEmail = (EditText) dialog.findViewById(R.id.inpEmail);
                ImageView imgIcon = (ImageView) dialog.findViewById(R.id.imgIcon);
                btnConfirm.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String email = txtEmail.getText().toString().trim();


                            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        txtEmail.setText("");
                                        Toast.makeText(getApplicationContext(),
                                                "Check Your Inbox to Reset Your Password", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),
                                                task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                    }
                });

            }
        });















    }
























}