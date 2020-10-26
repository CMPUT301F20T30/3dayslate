package com.jensen.demo.a3dayslate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DisplayUserSearchActivity extends AppCompatActivity {
    /*
      Displays the profile of the searched user

      @author: Anita Ferenc
      @see: Rewrite for .java classes that use it
      @version:1.0.0
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_user_search);

        // declare and set ids of xml elements
        TextView usernameField = findViewById(R.id.view_profile_username);
        TextView emailField = findViewById(R.id.view_profile_email);
        Bundle extras = getIntent().getExtras();

        // get the string extras from the UserSearchActivity
        String username = extras.getString("username"); //username of user searched
        String email = extras.getString("email"); // email of user searched

        // set the fields with the searched user information
        usernameField.setText(username);
        emailField.setText(email);
    }
}