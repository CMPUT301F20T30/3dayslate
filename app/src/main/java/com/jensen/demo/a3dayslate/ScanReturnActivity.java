package com.jensen.demo.a3dayslate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/* ScanReturnActivity Class

   Version 1.0.0

   November 23 2020

   Copyright [2020] [Larissa Zhang/Houston Le]

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

/**This activity shows all the books that a user is
 * currently borrowing, and allows for either viewing or
 * returning of a book
 *
 * @author Larissa Zhang
 * @author Houston Le
 * @see BarcodeScannerActivity
 * @version 1.0.0
 */

public class ScanReturnActivity extends AppCompatActivity {

    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    final FirebaseAuth uAuth = FirebaseAuth.getInstance();
    final FirebaseUser currentUser = uAuth.getCurrentUser();
    private int return_owner = 1;

    /** Creates a list view with all books that are currently
     * scanned under the user
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_return);

        ListView listBooks = findViewById(R.id.owner_book_listview);

        ArrayList<String> titleBooks = new ArrayList<>();
        ArrayList<Book> reqBooks = new ArrayList<>();
        ArrayAdapter reqBooksAdapter = new ArrayAdapter<String>(this, R.layout.content_incoming_request_book,titleBooks);
        Log.d("TITLE:","got here");

        listBooks.setAdapter(reqBooksAdapter);
        //going through all requests to get the book that have requests
        db.collection("users").document(currentUser.getDisplayName()).
                collection("books")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Book book = document.toObject(Book.class);
                                Log.d("TITLE:",book.getTitle());
                                Log.d("TITLE:",book.getCurrentStatus().toString());
                                if (book.getCurrentStatus().equals(Book.statuses.SCANNED)){
                                    reqBooks.add(book);
                                    titleBooks.add(book.getTitle());
                                    Log.d("TITLE:",book.getTitle());
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
                Intent intent= new Intent(ScanReturnActivity.this, BarcodeScannerActivity.class);
                intent.putExtra("book",book);
                intent.putExtra("action", "return");
                startActivityForResult(intent,return_owner);
            }
        });
    }

    /**Handles if the owner scanned the same book as
     * selected and also denotes book as available if
     * everything is scanned correct
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
        Book book_in_request = (Book) bundle.getSerializable("book");
        Log.w("ARA", "ISBN OF SCANNED BOOK: " + isbn);
        Log.w("ARA",  "ISBN OF BOOK: " + book_in_request.getIsbn());

        // Check to see if the book that was scanned is the correct book!
        if(isbn.equals(book_in_request.getIsbn())) {
            // Here check if you are the owner or borrower
            if(currentUser.getDisplayName().equals(book_in_request.getOwner())) {
                // Change the database to denote that the book is available
                Toast.makeText(ScanReturnActivity.this, "Owner, your book is now available", Toast.LENGTH_SHORT).show();
                book_in_request.setCurrentStatus(Book.statuses.AVAILABLE);
                book_in_request.setBorrower("");
                db.collection("users").document(book_in_request.getOwner()).
                        collection("books").document(book_in_request.getIsbn()).set(book_in_request);
                db.collection("books").document(book_in_request.getIsbn()).set(book_in_request);
                db.collection("users").document(book_in_request.getBorrower()).
                        collection("borrowedBooks").document(book_in_request.getIsbn()).delete();
                finish();
            }
        }
        else {
            Toast.makeText(ScanReturnActivity.this, "Scanned wrong book!", Toast.LENGTH_SHORT).show();
        }
    }
}

