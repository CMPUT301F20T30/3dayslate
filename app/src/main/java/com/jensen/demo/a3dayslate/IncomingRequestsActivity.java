package com.jensen.demo.a3dayslate;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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

import java.util.ArrayList;

public class IncomingRequestsActivity extends AppCompatActivity {

    // Firebase instances
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    final FirebaseAuth uAuth = FirebaseAuth.getInstance();
    final FirebaseUser currentUser = uAuth.getCurrentUser();

    private Request clickedRequest = null;

    ArrayAdapter<Request> requestAdapter;
    ArrayList<Request> requestArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incoming_requests_activity);
        //declare xml attributes
        Button accept;
        Button decline;
        ListView incomingRequestsList;
        accept = findViewById(R.id.accept_request_button);
        decline = findViewById(R.id.decline_request_button);
        incomingRequestsList = findViewById(R.id.incoming_requests_listview);

        db.collection("users").document(currentUser.getDisplayName()).
                collection("incomingRequests")
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
                            requestAdapter = new IncomingRequestCustomList(IncomingRequestsActivity.this, requestArrayList);
                            incomingRequestsList.setAdapter(requestAdapter);
                        }
                    }
                });


        //on click listener for accept
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickedRequest != null) {
                    String key = clickedRequest.getOwner() + clickedRequest.getRequester() + clickedRequest.getBook().getIsbn();
                    clickedRequest.setStatus(Book.statuses.ACCEPTED);
                    clickedRequest.getBook().setCurrentStatus(Book.statuses.ACCEPTED);
                    db.collection("users").document(clickedRequest.getOwner()).collection("incomingRequests").document(key).set(clickedRequest);
                    db.collection("users").document(clickedRequest.getRequester()).collection("outgoingRequests").document(key).set(clickedRequest);
                    db.collection("users").document(clickedRequest.getOwner()).collection("books").document(clickedRequest.getBook().getIsbn()).set(clickedRequest.getBook());
                    db.collection("books").document(clickedRequest.getBook().getIsbn()).set(clickedRequest.getBook());

                    // Decline all requests for the same book
                    String isbn = clickedRequest.getBook().getIsbn();
                    for(Request request : requestArrayList) {
                        if(request != clickedRequest) {
                            String id = request.getOwner() + request.getRequester() + request.getBook().getIsbn();
                            // Update the database as necessary
                            db.collection("users").document(request.getOwner()).collection("incomingRequests").document(id).delete();
                            db.collection("users").document(request.getRequester()).collection("outgoingRequests").document(id).delete();
                            requestArrayList.remove(request);
                        }
                    }
                    clickedRequest = null;
                    requestAdapter.notifyDataSetChanged();
                    // Set a location here somehow -> Google maps
                }
            }
        });

        //on click listener for decline button
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickedRequest != null) {
                    String key = clickedRequest.getOwner() + clickedRequest.getRequester() + clickedRequest.getBook().getIsbn();
                    Toast.makeText(IncomingRequestsActivity.this, "Request declined", Toast.LENGTH_SHORT).show();
                    // Update the database as necessary
                    db.collection("users").document(clickedRequest.getOwner()).collection("incomingRequests").document(key).delete();
                    db.collection("users").document(clickedRequest.getRequester()).collection("outgoingRequests").document(key).delete();
                    requestArrayList.remove(clickedRequest);
                    clickedRequest = null;
                    requestAdapter.notifyDataSetChanged();
                }
            }
        });

        //on item click listener or requests list
        incomingRequestsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickedRequest = (Request) parent.getItemAtPosition(position);
            }
        });
    }
}
