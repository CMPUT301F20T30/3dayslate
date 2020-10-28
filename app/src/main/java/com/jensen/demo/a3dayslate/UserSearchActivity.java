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
    /*
      Allows a user to search for other users

      @author: Anita Ferenc
      @see: Rewrite for .java classes that use it
      @version:1.0.0
    */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_search_activity);

        //declare xml attributes
        Button searchUserButton;
        TextView searchUser;
        TextView errorMessage;
        searchUser = (TextView) findViewById(R.id.user_search_bar);
        searchUserButton = findViewById(R.id.user_search_button);
        errorMessage = findViewById(R.id.error_message);
        ListView userListView = findViewById(R.id.user_search_listview);


        ArrayList<User> allUsers = new ArrayList<User>();
        ArrayList<User> matchedUsers = new ArrayList<User>();
        ArrayList<String> matchedUserStrings = new ArrayList<String>();

        ArrayAdapter userAdapter = new ArrayAdapter<String>(this, R.layout.user_search_content,matchedUserStrings);
        userListView.setAdapter(userAdapter);


        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //make new bookObject
                                User user = document.toObject(User.class);
                                allUsers.add(user);

                            }
                        } else {
                            Log.w("Users:", "Error getting documents");
                        }
                        Log.w("Users:", allUsers.toString());

                    }

                });

        //on click listener for search button
        /*
        searchUserButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                // if there was an error message, set visibility to invisible
                errorMessage.setVisibility(View.INVISIBLE);

                // Resources: https://firebase.google.com/docs/firestore/query-data/get-data
                // Date written: October 16, 2020
                // Date accessed: October 24,2020
                // License: Apache 2.0 License
                DocumentReference docRef = db.collection("users").document(searchUser.getText().toString());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            // collection "user" found
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                // user exist
                                String email = documentSnapshot.getData().get("email").toString(); // get users email
                                openDisplay(searchUser.getText().toString(), email); // open the DisplayUserSearchActivity
                            } else {
                                // user does not exist, display error message
                                errorMessage.setVisibility(View.VISIBLE);
                            }
                        } else {
                            // error, user collection does not exist
                            Log.d("Failed", "get failed with ", task.getException());
                        }

                        // clear the fields
                        searchUser.setText("");

                    }
                });

            }

        });
    }*/
        searchUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchedUsers.clear();
                errorMessage.setVisibility(View.INVISIBLE);
                String keyword = searchUser.getText().toString().toLowerCase();


                for (int i=0; i < allUsers.size(); i++){

                    User user = allUsers.get(i);

                    if (user.getUsername().toLowerCase().contains(keyword)){
                        matchedUsers.add(user);
                        matchedUserStrings.add(user.getUsername());
                        Log.d("User ADDED", user.getUsername());
                    }
                }
                if (matchedUsers.size() == 0){
                    errorMessage.setVisibility(View.VISIBLE);
                }
                userAdapter.notifyDataSetChanged();
            }
        });
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = matchedUsers.get(position);
                openDisplay(user);
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
