package com.jensen.demo.a3dayslate;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/* IncomingRequestsBook Class

   Version 1.0.0

   October 31 2020

   Copyright [2020] [Larissa Zhang/Jensen Khemchandani]

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

/** This activity handler both borrower and owner sides
 * of the process of borrowing a book with an accepted request.
 * The process allows for both borrower and owner to choose a book
 * that has been cleared to borrow and will carry out the process,
 * with the owner going first and the afterwards the borrower.
 *
 * @author Larissa Zhang
 * @author Jensen Khemchandani
 * @see BarcodeScannerActivity
 * @version 1.0.0
 */

public class AcceptedRequestsActivity extends AppCompatActivity {


    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    final FirebaseAuth uAuth = FirebaseAuth.getInstance();
    final FirebaseUser currentUser = uAuth.getCurrentUser();

    // Request codes
    int borrow_scan = 1;

    /** Creates a listview that shows all the books that
     * have accepted requests and are associated with the current user
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_requests);

        ListView listBooks = findViewById(R.id.accepted_requests_book_listview);

        ArrayList<String> titleBooks = new ArrayList<>();
        ArrayList<Book> reqBooks = new ArrayList<>();
        ArrayList<Request> allRequests = new ArrayList<>();
        ArrayAdapter reqBooksAdapter = new ArrayAdapter<String>(this, R.layout.content_accepted_requests, titleBooks);

        listBooks.setAdapter(reqBooksAdapter);
        //going through all requests to get the book that have requests
        db.collection("users").document(currentUser.getDisplayName()).
                collection("incomingRequests")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Request newRequest = document.toObject(Request.class);
                                Log.d("REQUEST:","Request Added");
                                if(newRequest.getStatus() == Book.statuses.ACCEPTED) {
                                    allRequests.add(newRequest);
                                    Book book = newRequest.getBook();
                                    // Test line!
                                    book.setBorrower(newRequest.getRequester());
                                    if (!reqBooks.contains(book)) {
                                        reqBooks.add(book);
                                        titleBooks.add(book.getTitle());
                                        Log.d("TITLE:", book.getTitle());
                                    }
                                }
                            }
                            reqBooksAdapter.notifyDataSetChanged();
                        }
                    }
                });

        db.collection("users").document(currentUser.getDisplayName()).
                collection("outgoingRequests")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Request newRequest = document.toObject(Request.class);
                                Log.d("REQUEST:","Request Added");
                                if(newRequest.getStatus() == Book.statuses.ACCEPTED) {
                                    allRequests.add(newRequest);
                                    Book book = newRequest.getBook();
                                    // Test line!
                                    book.setBorrower(newRequest.getRequester());
                                    if (!reqBooks.contains(book)) {
                                        reqBooks.add(book);
                                        titleBooks.add(book.getTitle());
                                        Log.d("TITLE:", book.getTitle());
                                    }
                                }
                            }
                            reqBooksAdapter.notifyDataSetChanged();
                        }
                    }
                });

        //when item clicked will take selected book to next activity
        listBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = reqBooks.get(position);
                Log.w("ARA", book.getBorrower());
                String requester = "";
                if(!book.getOwner().equals(currentUser.getDisplayName())) {
                    Log.w("OWNER?", "got here");
                    requester = currentUser.getDisplayName();
                    // Check here if the owner has scanned yet! (Can display message like owner needs to scan first) -> Need to check db for this!
                    if(book.getCurrentStatus() != Book.statuses.SCANNED) {
                        Toast.makeText(AcceptedRequestsActivity.this, "Owner must scan first!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // Only switch activity if you are the owner, or if you are the requester and the owner has already scanned
                        Intent intent= new Intent(AcceptedRequestsActivity.this, BarcodeScannerActivity.class);
                        intent.putExtra("book", book);
                        intent.putExtra("requester", requester);
                        intent.putExtra("action", "borrow");
                        startActivityForResult(intent, borrow_scan);
                    }
                }
                else {
                    if(book.getCurrentStatus() == Book.statuses.SCANNED) {
                        Toast.makeText(AcceptedRequestsActivity.this, "Already scanned!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // Only switch activity if you are the owner, or if you are the requester and the owner has already scanned
                        Intent intent= new Intent(AcceptedRequestsActivity.this, BarcodeScannerActivity.class);
                        intent.putExtra("book", book);
                        intent.putExtra("requester", requester);
                        intent.putExtra("action", "borrow");
                        startActivityForResult(intent, borrow_scan);
                    }
                }
            }
        });
    }

    /**Handles the result from the scanner activity, checking if the scanned
     * book matches the request and who has scanned the book.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getBundleExtra("bundle");
        String isbn = bundle.getString("ISBN");
        String requester = bundle.getString("requester");
        Book book_in_request = (Book) bundle.getSerializable("book");
        Log.w("ARA", "ISBN OF SCANNED BOOK: " + isbn);
        Log.w("ARA", "Requester: " + requester);
        Log.w("ARA",  "ISBN OF BOOK: " + book_in_request.getIsbn());
        // Check to see if the book that was scanned is the correct book!
        if(isbn.equals(book_in_request.getIsbn())) {
            // Here check if you are the owner or borrower and change stuff as necessary?
            if(currentUser.getDisplayName().equals(book_in_request.getOwner())) {
                Toast.makeText(AcceptedRequestsActivity.this, "Owner, your book has been scanned", Toast.LENGTH_SHORT).show();
                // Change the database to denote that the owner has scanned
                book_in_request.setCurrentStatus(Book.statuses.SCANNED);
                db.collection("users").document(book_in_request.getOwner()).
                        collection("books").document(book_in_request.getIsbn()).set(book_in_request);
                db.collection("books").document(book_in_request.getIsbn()).set(book_in_request);
                // Change the book inside the request!
                // Rebuild the request here!
                String key = book_in_request.getOwner() + book_in_request.getBorrower() + book_in_request.getIsbn();
                Log.w("ARA", key);
                db.collection("users").document(book_in_request.getOwner()).collection("incomingRequests").document(key).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                   DocumentSnapshot doc = task.getResult();
                                   Log.d("ARA", "DocumentSnapshot data: " + doc.getData());
                                   Request newRequest = doc.toObject(Request.class);
                                   Log.w("ARA", newRequest.getOwner());
                                   newRequest.setBook(book_in_request);
                                   db.collection("users").document(book_in_request.getOwner()).collection("incomingRequests").document(key).set(newRequest);
                                   db.collection("users").document(book_in_request.getBorrower()).collection("outgoingRequests").document(key).set(newRequest);
                                }
                            }
                        });
                finish();
            }
            else {
                // Denote the book as borrowed, and delete the request
                Toast.makeText(AcceptedRequestsActivity.this, "Borrower, you successfully borrowed the book", Toast.LENGTH_SHORT).show();
                book_in_request.setBorrower(currentUser.getDisplayName());
                book_in_request.setCurrentStatus(Book.statuses.BORROWED);
                // Re-set the book in the database with the new borrower
                db.collection("users").document(book_in_request.getOwner()).
                        collection("books").document(book_in_request.getIsbn()).set(book_in_request);
                db.collection("books").document(book_in_request.getIsbn()).set(book_in_request);
                // Delete the request in all of the database areas
                String key = book_in_request.getOwner() + currentUser.getDisplayName() + book_in_request.getIsbn();
                db.collection("users").document(book_in_request.getOwner()).collection("incomingRequests").document(key).delete();
                db.collection("users").document(currentUser.getDisplayName()).collection("outgoingRequests").document(key).delete();
                db.collection("users").document(currentUser.getDisplayName()).collection("borrowedBooks").document(book_in_request.getIsbn()).set(book_in_request);
                Intent intent = new Intent(AcceptedRequestsActivity.this, BorrowedBooksActivity.class);
                startActivity(intent);

            }
        }
        else {
            // The person scanned the wrong book
            Toast.makeText(AcceptedRequestsActivity.this, "Scanned wrong book!", Toast.LENGTH_SHORT).show();
        }
    }
}