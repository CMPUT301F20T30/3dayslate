package com.jensen.demo.a3dayslate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

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
                /* Check email regex -> Probably don't need?
                Boolean correct = Pattern.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", userEmail);
                */
                // Could do password hashing here?
                String userPassword = enterEmail.getText().toString();
                // Here, we should check the data with the FireStore DB to check for successful login
            }
        });

        // SignUp button functionality
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = enterEmail.getText().toString();
                // Could do password hashing here?
                String userPassword = enterEmail.getText().toString();
                // Here, we should insert into the DB if correct information has been entered
            }
        });

    }
}