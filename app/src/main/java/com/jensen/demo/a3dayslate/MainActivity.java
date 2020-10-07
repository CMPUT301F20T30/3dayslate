package com.jensen.demo.a3dayslate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.CollectionReference;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect to FireBase FireStore
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get the "Users" collection here! **NOT DONE**
        // ...


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
            @Override
            public void onClick(View view) {
                String userEmail = enterEmail.getText().toString();
                // Could do password hashing here?
                String userPassword = enterPassword.getText().toString();
                // Here, we should insert into the DB if correct information has been entered
            }
        });

    }
}