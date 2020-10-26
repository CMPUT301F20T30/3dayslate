package com.jensen.demo.a3dayslate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
        errorMessage = findViewById(R.id.errorMessageView);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        //on click listener for search button
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
    }

    public void openDisplay(String username, String email){
        // gets the username, email, and phone number
        // starts DisplayUserSearchActivity
        Intent intent = new Intent(this, DisplayUserSearchActivity.class);

        // send username and email to DisplayUserSearchActivity
        intent.putExtra("username",username);
        intent.putExtra("email",email);

        startActivity(intent);
    }
}
