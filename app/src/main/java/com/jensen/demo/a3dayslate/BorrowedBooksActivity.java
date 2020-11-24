package com.jensen.demo.a3dayslate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

import java.io.Serializable;
import java.util.ArrayList;

/* BorrowedBooksActivity Class

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
 * @see ViewBookActivity
 * @see BarcodeScannerActivity
 * @version 1.0.0
 */

public class BorrowedBooksActivity extends AppCompatActivity {

    private final ArrayList<Book> myBooks = new ArrayList<>();
    private BorrowedBooksAdapter booksAdapter;
    private Book clickedBook = null;
    private int view_book = 0;
    private int scan_return = 1;

    // gets the current user from the database
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    final FirebaseAuth uAuth = FirebaseAuth.getInstance();
    final FirebaseUser currentUser = uAuth.getCurrentUser();

    /**Creates a listview and buttons showing all
     * books that a user owns
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowed_books);

        //declare listView
        ListView borrowedBooksList;
        Button viewButton= findViewById(R.id.view_borrowed_book_button);
        Button returnButton = findViewById(R.id.return_book_button);

        borrowedBooksList = findViewById(R.id.borrowed_books_listview);
        borrowedBooksList.setAdapter(booksAdapter);

        // query database for all books owned by current user
        db.collection("users").document(currentUser.getDisplayName()).
                collection("borrowedBooks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // log book in database
                                Log.w("BOOK:", document.getId() + "=>" + document.getData());
                                //make new bookObject
                                Book newBook = document.toObject(Book.class);
                                myBooks.add(newBook);

                            }
                        } else {
                            Log.w("BOOK:", "Error getting documents");
                        }
                        Log.w("BOOK ARRAYLIST:", myBooks.toString());
                        booksAdapter = new BorrowedBooksAdapter(BorrowedBooksActivity.this, R.layout.content_borrowed_book_list, myBooks);
                        borrowedBooksList.setAdapter(booksAdapter);

                    }

                });


        //item click listener for borrowed book list
        borrowedBooksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickedBook = (Book) parent.getItemAtPosition(position);
            }
        });

        //on click listener for viewing books
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickedBook!=null){
                    Intent intent = new Intent(v.getContext(), ViewBookActivity.class);
                    intent.putExtra("book", (Serializable) clickedBook);
                    startActivityForResult(intent, view_book);
                }
            }
        });

        //on click listener for returning books
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickedBook!=null){
                    Intent intent = new Intent(v.getContext(), BarcodeScannerActivity.class);
                    intent.putExtra("action", "return");
                    intent.putExtra("book", (Serializable) clickedBook);
                    startActivityForResult(intent, scan_return);
                }
            }
        });


    }

    /**Handles if the scanned book matched the book user
     * wanted to return and indicates so
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
            // Here check if you are the owner or borrower and change stuff as necessary?
            if(currentUser.getDisplayName().equals(book_in_request.getBorrower())) {
                Toast.makeText(BorrowedBooksActivity.this, "Borrower, your book has been scanned", Toast.LENGTH_SHORT).show();
                // Change the database to denote that the owner has scanned
                book_in_request.setCurrentStatus(Book.statuses.SCANNED);
                db.collection("users").document(book_in_request.getOwner()).
                        collection("books").document(book_in_request.getIsbn()).set(book_in_request);
                db.collection("books").document(book_in_request.getIsbn()).set(book_in_request);
                db.collection("users").document(book_in_request.getBorrower()).
                        collection("borrowedBooks").document(book_in_request.getIsbn()).set(book_in_request);
                finish();
            }
        }
        else {
            Toast.makeText(BorrowedBooksActivity.this, "Scanned wrong book!", Toast.LENGTH_SHORT).show();
        }
    }
}
