package com.jensen.demo.a3dayslate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/* IncomingRequestsActivity

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
 * This activity grabs all of the incoming requests associated with a user in the database, and displays them on the screen
 * This activity also provides the user with options to accept/deny incoming requests
 @author Jensen Khemchandani
 @version 1.0.0
 @see IncomingRequestsBooks
 */

public class IncomingRequestsActivity extends AppCompatActivity {

    // Firebase instances
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    final FirebaseAuth uAuth = FirebaseAuth.getInstance();
    final FirebaseUser currentUser = uAuth.getCurrentUser();
    final int LOCATION_ACTIVITY_CODE = 1;
    private Request clickedRequest = null;

    ArrayAdapter<Request> requestAdapter;
    ArrayList<Request> requestArrayList = new ArrayList<>();
    /**
     * This method sets up all of the buttons and listeners for the UI (Accepting/denying requests  and setting up the ListView)
     * @param savedInstanceState
     * The bundle contains a Book object from the previous IncomingRequestsBooks activity
     */
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

        Intent intent = getIntent();
        Book requestedBook = (Book) intent.getSerializableExtra("book");

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
                                Log.d("REQUESTED BOOK",newRequest.getBook().getIsbn() + requestedBook.getIsbn());
                                if (newRequest.getBook().getIsbn().equals(requestedBook.getIsbn())){
                                    requestArrayList.add(newRequest);
                                    Log.d("DID IT WORK", "Added request");
                                }
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
                    Intent intent = new Intent(IncomingRequestsActivity.this, LocationActivity.class);
                    startActivityForResult(intent, LOCATION_ACTIVITY_CODE);
                    // Set a location here somehow -> Google maps
                }
            }
        });

        //TODO need to fix issue with declining an accepted request
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

    /**
     * This method gets a Location object from the ExchangeLocation method, and processes it/adds it to the database
     * Also, sends a notification to the borrower if a location was successfully chosen
     * @param requestCode
     * This is the request code specifying the action to take in this method
     * @param resultCode
     * This is the result code specifying action to take in this method
     * @param data
     * This is the data Intent passed over from the Location activity
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(IncomingRequestsActivity.this, "A notification has been sent to the borrower", Toast.LENGTH_SHORT).show();
        if(requestCode == LOCATION_ACTIVITY_CODE){
            if (resultCode == LocationActivity.RESULT_OK){
                String key = clickedRequest.getOwner() + clickedRequest.getRequester() + clickedRequest.getBook().getIsbn();
                ExchangeLocation location = (ExchangeLocation) data.getExtras().getSerializable("LOCATION");
                clickedRequest.setLocation(location);
                clickedRequest.setStatus(Book.statuses.ACCEPTED);
                clickedRequest.getBook().setCurrentStatus(Book.statuses.ACCEPTED);
                //Log.w("TESTLOCATION", String.valueOf(location.getLatitude()) + " " + String.valueOf(location.getLongitude()));

                db.collection("users").document(clickedRequest.getOwner()).collection("incomingRequests").document(key).set(clickedRequest);
                db.collection("users").document(clickedRequest.getRequester()).collection("outgoingRequests").document(key).set(clickedRequest);
                db.collection("users").document(clickedRequest.getOwner()).collection("books").document(clickedRequest.getBook().getIsbn()).set(clickedRequest.getBook());
                db.collection("books").document(clickedRequest.getBook().getIsbn()).set(clickedRequest.getBook());
                // Decline all requests for the same book
                String isbn = clickedRequest.getBook().getIsbn();
                Iterator<Request> i = requestArrayList.iterator();
                while(i.hasNext()) {
                    Request request = i.next();
                    if(request != clickedRequest) {
                        String id = request.getOwner() + request.getRequester() + request.getBook().getIsbn();
                        // Update the database as necessary
                        db.collection("users").document(request.getOwner()).collection("incomingRequests").document(id).delete();
                        db.collection("users").document(request.getRequester()).collection("outgoingRequests").document(id).delete();
                        i.remove();
                    }
                }
                // Send notification to borrower!
                sendNotificationToBorrower(db, currentUser.getDisplayName(), clickedRequest.getRequester(), clickedRequest.getBook());
                clickedRequest = null;
                requestAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * This method takes information about both users involved a request, and sends a notification to the borrower' device regarding an accepted request
     * @param db
     * This is the database reference to store the new info in
     * @param owner
     * This is the Owner of the book involved in the request
     * @param borrower
     * This is the borrower of the book involved in the request
     * @param book
     * This is the book object involved in the request
     */
    public void sendNotificationToBorrower(FirebaseFirestore db, String owner, String borrower, Book book) {
        db.collection("users").document(borrower).collection("device-token").document("token").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    String token = (String) document.getData().get("deviceToken");
                    Log.w("RC", token);
                    String json = "{\"to\":\"" + token + "\",\"notification\":{\"title\":\"Request Accepted\",\"body\":\"For: " + book.getTitle() + "\"}}";
                    Log.w("RC", json);
                    String apiKey = "key=AAAAuSNnUgU:APA91bEe66HTssr9aKZtPmsIL_14Nw-PpIj6Hp_ULBA5rpezXbWEIRr4uujh6nIt0m2JcDd4CBPjYuQMOIGqVf5TKa2Q23EoUQNhcUg1850iFqvq9V7I3Lr8Uax2_bRSfE6cd4ydMTXI"; // Environment variable?
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = RequestBody.create(MediaType.parse("application/json"),json);
                    Log.w("RC", body.toString());
                    okhttp3.Request httpRequest = new okhttp3.Request.Builder()
                            .url("https://fcm.googleapis.com/fcm/send")
                            .header("content-type","application/json")
                            .header("Authorization", apiKey)
                            .post(body)
                            .build();
                    Log.w("RC1", httpRequest.toString());
                    client.newCall(httpRequest)
                            .enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Can display a toast here
                                        }
                                    });
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    Log.w("RC", Integer.toString(response.code()) + "BODY=" + response.body().toString());
                                }
                            });

                }
            }
        });
    }
}
