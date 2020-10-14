package com.jensen.demo.a3dayslate;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ViewProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile_activity);
        //declare xml elements
        TextView name;
        TextView username;
        TextView email;
        TextView phone;
        Button editProfile;
        name = findViewById(R.id.view_profile_name);
        username = findViewById(R.id.view_profile_username);
        email = findViewById(R.id.view_profile_email);
        phone = findViewById(R.id.view_profile_phone);
        editProfile = findViewById(R.id.edit_profile_button);

        // gets the current user from the database
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseAuth uAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = uAuth.getCurrentUser();

        // sets the texts according to users firebase information
        username.setText(currentUser.getDisplayName());
        email.setText(currentUser.getEmail());
        // NAME????
        // PHONE NUMBER??

        //on click listener for edit profile button
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
