package com.jensen.demo.a3dayslate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;
/* DisplayUserSearchActivity Activity

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
 Displays the profile of the searched user

 @author: Anita Ferenc
 @see: Rewrite for .java classes that use it
 @version:1.0.0
 */
public class DisplayUserSearchActivity extends AppCompatActivity {


    // declaring xml elements
    TextView usernameField;
    TextView emailField;
    User user;

    /**
     * Shows the information of the user searched
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_user_search);

        // set ids of xml elements
        usernameField = findViewById(R.id.view_profile_username);
        emailField = findViewById(R.id.view_profile_email);
        Bundle extras = getIntent().getExtras();

        // get the string extras from the UserSearchActivity
        user = (User) extras.getSerializable("user");

        // set the fields with the searched user information
        usernameField.setText(user.getUsername());
        emailField.setText(user.getEmail());
    }
}