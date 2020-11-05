package com.jensen.demo.a3dayslate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.regex.Pattern;

/* MainActivity (LoginSignupActivity)

   Version 1.0.0

   November 5 2020

   Copyright [2020] [Jensen Khemchandani]

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
 * This is the startup activity of the app
 * Prompts the user to enter their login information and/or sign up for a new account
 * This activity interfaces with both Firestore and Firebase Auth databases for authentication
 * All activity and information inside the app will be associated with the logged in user's data
 * @author Jensen Khemchandani
 * @version 1.0.0
 */

public class MainActivity extends AppCompatActivity {

    // Declare buttons for login screen
    private Button loginButton;
    private Button signUpButton;

    // Declare text fields for user information
    private EditText enterEmail;
    private EditText enterPassword;
    private EditText enterUsername;

    //String deviceToken;

    /**
     * Sets up the login/signup screen with all of the buttons and the text fields
     * Also sets up the connection to the FireStore database and FireBase Auth service
     * to ensure proper authentication
     * @param savedInstanceState
     * The bundle object passed into the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect to FireBase FireStore
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Connect to the FireBase Auth server
        final FirebaseAuth uAuth = FirebaseAuth.getInstance();


        // Map buttons and EditText fields to XML
        loginButton = findViewById(R.id.login_button);
        signUpButton = findViewById(R.id.signup_button);
        enterEmail = findViewById(R.id.enter_email);
        enterPassword = findViewById(R.id.enter_password);
        enterUsername = findViewById(R.id.enter_username);

        // Login button functionality
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String userEmail = enterEmail.getText().toString();
                String userUsername = enterUsername.getText().toString();
                String userPassword = enterPassword.getText().toString();
                if (userEmail.length() == 0 || userPassword.length() == 0) {
                    Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Here, we should check the data with the FireStore DB to check for successful login
                uAuth.signInWithEmailAndPassword(userEmail, userPassword)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, set the current user, and navigate to next activity
                                    Log.d("TAG", "signInWithEmail:success");
                                    FirebaseUser user = uAuth.getCurrentUser();
                                    Toast.makeText(MainActivity.this, "Signed in as: " + user.getDisplayName(),
                                            Toast.LENGTH_SHORT).show();
                                    // -> Navigate to a next activity here!
                                    Intent intent = new Intent(view.getContext(), DashboardActivity.class);
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("TAG", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                enterEmail.setText("");
                enterUsername.setText("");
                enterPassword.setText("");
            }
        });

        // SignUp button functionality
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the sign-up data from EditText fields
                final String userEmail = enterEmail.getText().toString();
                final String userUsername = enterUsername.getText().toString();
                //HashMap<String, Object> user;
                final String userPassword = enterPassword.getText().toString();
                if (userEmail.length() == 0 || userPassword.length() == 0 || userUsername.length() == 0) {
                    Toast.makeText(MainActivity.this, "Sign-up failed.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Use FireBase Auth to set up and authenticate a new User!

                final DocumentReference documentReference = db.collection("users").document(userUsername);
                // This code below, checks the database for the username entered, to ensure that it is unique!
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("TAG", "Username is taken!");
                                Toast.makeText(MainActivity.this, "Username taken.", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                uAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull final Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    // Case for if the username is indeed unique! -> Actually add the user to the database!
                                                    User dbUser = new User(userUsername, userEmail);
                                                    documentReference
                                                            .set(dbUser)
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.d("TAG", "Failure when trying to sign up user!");
                                                                }
                                                            })
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Log.d("TAG", "User profile is created");
                                                                    FirebaseUser user = uAuth.getCurrentUser();
                                                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                                            .setDisplayName(userUsername).build();

                                                                    user.updateProfile(profileUpdates)
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        Log.d("TAG", "User profile updated.");
                                                                                        Intent intent = new Intent(view.getContext(), DashboardActivity.class);
                                                                                        startActivity(intent);
                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            });
                                                } else {
                                                    // If sign in fails, display a message to the user?
                                                    Log.e("TAG", "onComplete: Failed=" + task.getException().getMessage());
                                                    Toast.makeText(MainActivity.this, "Sign-up failed!",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                        else {
                            return;
                        }
                    }
                });
                enterEmail.setText("");
                enterPassword.setText("");
                enterUsername.setText("");
            }

        });
    }
}