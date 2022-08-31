package com.example.smartcafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity
{
    EditText editProfileName;
    EditText editProfilePhone;
    EditText editProfileEmail;
    EditText editProfileBirth;

    Button btnChangePassword;
    Button btnUpdateProfile;
    ImageButton btnCloseEditProfileActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editProfileName = findViewById(R.id.editProfileName);
        editProfilePhone = findViewById(R.id.editProfilePhone);
        editProfileEmail = findViewById(R.id.editProfileEmail);
        editProfileBirth = findViewById(R.id.editProfileBirth);

        btnCloseEditProfileActivity = findViewById(R.id.btnCloseEditProfileActivity);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        btnChangePassword = findViewById(R.id.btnProfileChangePassword);

        editProfileBirth.setEnabled(false);
        editProfileEmail.setEnabled(false);
        editProfileBirth.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DCDCDC")));
        editProfileEmail.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DCDCDC")));

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid().toString());
        Query qRef = ref;

        ref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                User received = snapshot.getValue(User.class);
                editProfileBirth.setText(received.getDateOfBirth());
                editProfileEmail.setText(received.getEmail());
                editProfilePhone.setText(received.getPhone());
                editProfileName.setText(received.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });


        btnUpdateProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid().toString());
                ref2.child("name").setValue(editProfileName.getText().toString());
                ref2.child("phone").setValue(editProfilePhone.getText().toString());

                final Dialog dialog = new Dialog(EditProfileActivity.this);
                dialog.setContentView(R.layout.successful_alertdialog);

                TextView txtAlert = (TextView) dialog.findViewById(R.id.txtTitle);
                ImageView imgIcon = (ImageView) dialog.findViewById(R.id.imgIcon);

                txtAlert.setText("Profile was updated successfully");
                imgIcon.setImageResource(R.drawable.eathamburger);

                dialog.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(EditProfileActivity.this,MainActivity.class));
                    }
                },2000);




            }
        });

        btnCloseEditProfileActivity.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog dialog = new Dialog(EditProfileActivity.this);
                dialog.setContentView(R.layout.ensure_dialog);

                ImageButton btnYes = dialog.findViewById(R.id.btnUnderstood);
                ImageButton btnNo = dialog.findViewById(R.id.btnNo);

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(EditProfileActivity.this,MainActivity.class));
                    }
                });

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });



        btnChangePassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();

                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),
                                    "Check Your Inbox to Reset Your Password", Toast.LENGTH_LONG).show();
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

}