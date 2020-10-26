package com.jensen.demo.a3dayslate;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class ViewProfileActivity extends AppCompatActivity implements EditProfileDialog.ProfileDialogListener {
    /*
      Allow user to view their own contact information and edit username and email fields

      @author: Anita Ferenc
      @see: Rewrite for .java classes that use it
      @version:1.0.0
    */

    //declare xml elements
    TextView username;
    TextView email;
    TextView phone;
    Button editProfile;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile_activity);

        // get ids of xml elements
        username = findViewById(R.id.view_profile_username);
        email = findViewById(R.id.view_profile_email);
        phone = findViewById(R.id.view_profile_phone);
        editProfile = findViewById(R.id.edit_profile_button);

        // gets the current user from the database
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseAuth uAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = uAuth.getCurrentUser();

        if (currentUser != null) {
            // sets the texts according to users firebase information
            username.setText("Username: " + currentUser.getDisplayName());
            email.setText("Email: " + currentUser.getEmail());
        }
        else {
            Log.d("NULL USER","VIEW PROFILE");
            return;
        }

        //on click listener for edit profile button
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // creates new dialog when edit profile button clicked
                EditProfileDialog editProfileDialog = new EditProfileDialog();
                editProfileDialog.show(getSupportFragmentManager(), "edit profile dialog");
            }
        });
    }

    @Override
    public void applyTexts(String editUsername, String editEmail, String editPhoneNumb, boolean usernameTaken) {
        if (usernameTaken == true){
            // username was taken, make toast message
            Log.d("VIEW PROFILE","Username take");
            Toast.makeText(ViewProfileActivity.this, "Username Taken",Toast.LENGTH_SHORT).show();
        } else {
            Log.d("BOOL", "False");
        }

        // set the text fields with updated information from the dialog
        username.setText("Username: "+editUsername);
        email.setText("Email: "+editEmail);
        phone.setText("Phone Number: "+ editPhoneNumb);
    }
}
