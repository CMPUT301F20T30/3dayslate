package com.jensen.demo.a3dayslate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;

/* OutgoingRequestsActivity

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
 * This activity grabs all of the outgoing requests associated with a user in the database, and displays them on the screen
 * This activity also provides the user with options to view existing requests' location and their statuses
 * @author Jensen Khemchandani
 * @version 1.0.0
 */

public class OutgoingRequestsActivity extends AppCompatActivity {
    // Firebase instances
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    final FirebaseAuth uAuth = FirebaseAuth.getInstance();
    final FirebaseUser currentUser = uAuth.getCurrentUser();

    ArrayAdapter<Request> requestAdapter;
    ArrayList<Request> requestArrayList = new ArrayList<>();
    Request selectedRequest;

    /**
     * This method sets up all of the buttons and listeners for the UI
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.outgoing_requests_activity);

        //declare listView
        ListView outgoingRequestsList;
        outgoingRequestsList = findViewById(R.id.outgoing_requests_listview);

        //set Request Adapter to custom list view

        // Update the view depending on the contents of the user's outgoing request db

        //** Make the primary key for a request a combination of the owner name and the borrower name and bookISBN!!
        db.collection("users").document(currentUser.getDisplayName()).
                collection("outgoingRequests")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.w("TESTOUTGOING", document.toObject(Request.class).getOwner());
                                Request newRequest = document.toObject(Request.class);
                                requestArrayList.add(newRequest);
                            }
                            requestAdapter = new RequestCustomList(OutgoingRequestsActivity.this, requestArrayList);
                            outgoingRequestsList.setAdapter(requestAdapter);
                        }
                    }
                });

        //on item click listener for outgoing request idk if we actually need this
        outgoingRequestsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedRequest = (Request) adapterView.getItemAtPosition(i);
                if(selectedRequest != null && selectedRequest.getStatus() == Book.statuses.ACCEPTED) {
                    // Start a location activity
                    Intent intent = new Intent(OutgoingRequestsActivity.this, ReviewLocationActivity.class);
                    intent.putExtra("LOCATION", (Serializable) selectedRequest.getLocation());
                    startActivity(intent);
                }
                return true;
            }
        });
    }


}
