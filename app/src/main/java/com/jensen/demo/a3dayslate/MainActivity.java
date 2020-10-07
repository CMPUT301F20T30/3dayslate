package com.jensen.demo.a3dayslate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    // Declare buttons for login screen
    private Button loginButton;
    private Button signUpButton;

    // Declare text fields for user information
    private EditText enterEmail;
    private EditText enterPassword;

    // Variable to keep track of the current user's ID
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect to FireBase FireStore
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Connect to the FireBase Auth server
        final FirebaseAuth uAuth = FirebaseAuth.getInstance();

         /* This comment contains a test for database insertion!
        final CollectionReference collectionReference = db.collection("Test");

        HashMap<String, String> data = new HashMap<>();
        data.put("attr1", "1");
        data.put("attr2", "2");
        collectionReference
                .document("Test")
                .set(data);
        */

        // Map buttons and EditText fields to XML
        loginButton = findViewById(R.id.login_button);
        signUpButton = findViewById(R.id.signup_button);
        enterEmail = findViewById(R.id.enter_email);
        enterPassword = findViewById(R.id.enter_password);


        // Login button functionality
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = enterEmail.getText().toString();
                String userPassword = enterPassword.getText().toString();
                // Could do password hashing here?
                // Here, we should check the data with the FireStore DB to check for successful login

            }
        });

        // SignUp button functionality
        signUpButton.setOnClickListener(new View.OnClickListener() {
            Boolean success = false;
            @Override
            public void onClick(View view) {
                // Get the sign-up data from EditText fields
                final String userEmail = enterEmail.getText().toString();
                String userPassword = enterPassword.getText().toString();
                if(userEmail.length() == 0 || userPassword.length() == 0) {
                    return;
                }
                // Use FireBase Auth to set up and authenticate a new User!
                uAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    success = true;
                                    userID = uAuth.getCurrentUser().getUid();
                                    DocumentReference documentReference = db.collection("users").document(userID);
                                    HashMap<String, Object> user = new HashMap<>();
                                    user.put("email", userEmail);
                                    documentReference
                                            .set(user)
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d("TAG", "Failure when trying to sign up user!");
                                                }
                                            })
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("TAG", "User profile is created for " + userID);
                                                }
                                    });
                                }
                                else {
                                    // If sign in fails, display a message to the user.
                                    Log.e("TAG", "onComplete: Failed=" + task.getException().getMessage());
                                }

                                // ...
                            }
                        });
            }
        });

    }
}