package com.jensen.demo.a3dayslate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class OutgoingRequestsActivity extends AppCompatActivity {

    // Firebase instances
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    final FirebaseAuth uAuth = FirebaseAuth.getInstance();
    final FirebaseUser currentUser = uAuth.getCurrentUser();

    ArrayAdapter<Request> requestAdapter;
    ArrayList<Request> requestArrayList = new ArrayList<>();
    Request selectedRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.outgoing_requests_activity);

        //declare listView
        ListView outgoingRequestsList;
        outgoingRequestsList = findViewById(R.id.outgoing_requests_listview);
        /*
        Book test_book = new Book("test", "123", new ArrayList<String>(), "Eric");
        Request testReq = new Request("hihihi", test_book);
        testReq.setStatus(Book.statuses.REQUESTED);
        //requestArrayList.add(testReq);
        */
        // Gets the collection reference for the user's outgoing requests
        // Test for adding request to the database
        /*
        HashMap<String, Object> myRequest = new HashMap<>();
        myRequest.put("requester", "hihihi");
        myRequest.put("book", test_book);
        */
        //db.collection("users").document(currentUser.getDisplayName()).collection("outgoingRequests").document("TEST").set(testReq);


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
                            requestAdapter = new requestCustomList(OutgoingRequestsActivity.this, requestArrayList);
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
