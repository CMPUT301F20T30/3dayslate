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
    /* ViewProfileActivity

   Version 1.0.0

   November 3 2020

   Copyright [2020] [Anita Ferenc]
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
       http://www.apache.org/licenses/LICENSE-2.0
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   */

    /**
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
    FirebaseUser currentUser;

    /**
     * Sets the information of the xml elements with the current user information
     *
     * @param savedInstanceState
     */
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
        currentUser = uAuth.getCurrentUser();

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
                editProfileDialog.setContext(ViewProfileActivity.this);
                editProfileDialog.show(getSupportFragmentManager(), "edit profile dialog");
            }
        });
    }

    /**
     * Gets the updated information from EditProfilDialog
     * @param editEmail
     * @param editPhoneNumb
     * editEmail is the changed email from the user
     * editPhoneNumb is the changed phone number of the user
     */
    @Override
    public void applyTexts(String editEmail, String editPhoneNumb) {
        // set the text fields with updated information from the dialog
        email.setText("Email: "+editEmail);
        phone.setText("Phone Number: "+ editPhoneNumb);
    }
}
