package com.jensen.demo.a3dayslate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class UserSearchActivity extends AppCompatActivity {
    /* UserSearchActivity

   Version 1.2.0

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
      Allows a user to search for other users

      @author: Anita Ferenc
      @see: Rewrite for .java classes that use it
      @version:1.2.0
    */

    //declare xml attributes
    Button searchUserButton;
    TextView searchUser;
    TextView errorMessage;
    ArrayList<User> allUsers;
    ArrayList<User> matchedUsers ;
    ArrayList<String> matchedUserStrings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_search_activity);

        // setting up the ids for xml elements
        searchUser = (TextView) findViewById(R.id.user_search_bar);
        searchUserButton = findViewById(R.id.user_search_button);
        errorMessage = findViewById(R.id.error_message);
        ListView userListView = findViewById(R.id.user_search_listview);

         allUsers = new ArrayList<User>(); // contains all the users in the database
         matchedUsers = new ArrayList<User>(); // contains User objects that match search word
         matchedUserStrings = new ArrayList<String>(); // contains usernames of users that match search word

        ArrayAdapter userAdapter = new ArrayAdapter<String>(this, R.layout.user_search_content,matchedUserStrings);
        userListView.setAdapter(userAdapter);

        // Query database to find all the users
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //make new userObject
                                User user = document.toObject(User.class);
                                allUsers.add(user);
                            }
                        } else {
                            Log.w("Users:", "Error getting documents");
                        }
                        Log.w("Users:", allUsers.toString());
                    }
                });

        searchUserButton.setOnClickListener(new View.OnClickListener() {
            // when search button is clicked
            @Override
            public void onClick(View v) {
                // clear lists from previous search
                matchedUserStrings.clear();
                matchedUsers.clear();

                errorMessage.setVisibility(View.INVISIBLE); // ensure error message is not displayed
                String keyword = searchUser.getText().toString().toLowerCase();

                for (int i=0; i < allUsers.size(); i++){
                    // go through every user in all user list
                    User user = allUsers.get(i);
                    if (user.getUsername().toLowerCase().contains(keyword)){
                        // if the user in allUser matches the search word
                        matchedUsers.add(user);
                        matchedUserStrings.add(user.getUsername());
                        Log.d("User ADDED", user.getUsername());
                    }
                }
                if (matchedUsers.size() == 0){
                    // there were no matches
                    errorMessage.setVisibility(View.VISIBLE);
                }

                userAdapter.notifyDataSetChanged();
            }
        });
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // when a user is clicked to see more information about user
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = matchedUsers.get(position);
                openDisplay(user); // open DisplayUserActivity
            }
        });
    }

    public void openDisplay(User user){
        // gets the username, email, and phone number
        // starts DisplayUserSearchActivity
        Intent intent = new Intent(this, DisplayUserSearchActivity.class);
        Log.d("User chosen", user.getUsername());
        // send username and email to DisplayUserSearchActivity
        intent.putExtra("user", user);

        startActivityForResult(intent,1);
    }
}
