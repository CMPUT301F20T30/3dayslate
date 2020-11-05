package com.jensen.demo.a3dayslate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

/* BookSearchActivity

   Version 1.0.0

   October 17 2020

   Copyright [2020] [Danny Zaiter, Eric Weber]

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
 * Book Search Activity allows for the searching of books in order to view their
 * information or make a request to borrow, the books are filtered through a keyword
 * search implemented in this Activity
 */
public class BookSearchActivity extends AppCompatActivity implements Serializable{

    private bookCustomList bookAdapter;
    private Book clickedBook = null;
    private int VIEW_BOOK_ACTIVITY = 0;

    //declare all xml elements
    Button viewBook;
    Button requestBook;
    Button searchBook;
    ListView bookList;
    EditText searchBar;

    /**
     * This method begins the activity
     * @param savedInstanceState
     *        Bundle
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set view
        setContentView(R.layout.book_search_activity);

        /// database connection
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseAuth uAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = uAuth.getCurrentUser();

        //declare array and adapter
        ArrayAdapter<Book> bookAdapter;
        ArrayList<Book> bookDataList = new ArrayList<>();
        ArrayList<Book> searchDataList = new ArrayList<>();


        //find view by ID commands
        viewBook = findViewById(R.id.view_book_button);
        requestBook = findViewById(R.id.request_book_button);
        searchBook = findViewById(R.id.book_search_button);
        bookList = findViewById(R.id.book_search_listview);
        searchBar = findViewById(R.id.book_search_bar);

        //set Book Adapter to custom list view
        bookAdapter = new bookCustomList(this, searchDataList);
        bookList.setAdapter(bookAdapter);

        // query database for all books
        db.collection("books")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    /**
                     * This method pulls all books from the DB into the search
                     * @param task
                     */
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // log book in database
                                Log.w("BOOK:", document.getId() + "=>" + document.getData());
                                //make new bookObject
                                Book newBook = document.toObject(Book.class);
                                bookDataList.add(newBook);

                            }
                        } else {
                            Log.w("BOOK:", "Error getting documents");
                        }
                        Log.w("BOOK ARRAYLIST:", bookDataList.toString());

                        //bookDataList.setAdapter(bookAdapter);
                        Log.w("BOOK ARRAYLIST:", bookDataList.toString());

                    }

                });

        searchBook.setOnClickListener(new View.OnClickListener() {
            /**
             * This method initializes a new search with a keyword specified in the search bar
             * @param v
             *        The activity's View
             */
            @Override
            public void onClick(View v) {

                searchDataList.clear();
                String keyword = searchBar.getText().toString().toLowerCase();


                for (int i=0; i < bookDataList.size(); i++){

                    Book book = bookDataList.get(i);

                    String bookInfo = book.getIsbn() + book.getTitle();
                    for (int j=0; j < book.getAuthors().size(); j++){
                        bookInfo = bookInfo + book.getAuthors().get(j);
                    }

                    if (bookInfo.toLowerCase().contains(keyword)
                            && (book.getCurrentStatus() == Book.statuses.AVAILABLE || book.getCurrentStatus() == Book.statuses.REQUESTED)){
                        searchDataList.add(book);
                        Log.d("BOOK ADDED", book.getTitle());
                    }
                }
                bookAdapter.notifyDataSetChanged();

            }
        });

        //on click listener for view book button
        viewBook.setOnClickListener(new View.OnClickListener() {
            /**
             * This method opens the ViewBookActivity for a selected book,
             * or does nothing if no book is selected after a click on the viewBook button
             * @param v
             *        The activity's View
             */
            @Override
            public void onClick(View v) {
                //view clicked book
                if(clickedBook!=null){
                    Intent intent = new Intent(v.getContext(), ViewBookActivity.class);
                    intent.putExtra("book", (Serializable) clickedBook);
                    startActivityForResult(intent, VIEW_BOOK_ACTIVITY);
                }
            }
        });

        //on click listener for request book button
        requestBook.setOnClickListener(new View.OnClickListener() {
            /**
             * This method initializes a Request object for a selected book
             * and modifies the DB accordingly after a click on the requestBook button
             * @param v
             *        The activity's View
             */
            @Override
            public void onClick(View v) {
                if(clickedBook!=null){
                    //requestID is owner Username + borrower Username + isbn
                    String borrower = currentUser.getDisplayName();
                    Request request = new Request(borrower, clickedBook);
                    String owner = clickedBook.getOwner();
                    String isbn = clickedBook.getIsbn();
                    String requestID = owner + borrower + isbn;
                    if (clickedBook.getCurrentStatus() != Book.statuses.REQUESTED){
                        Log.w("Available Larissa","Testing on Available books");
                        request.setStatus(Book.statuses.REQUESTED);
                        clickedBook.setCurrentStatus(Book.statuses.REQUESTED);
                        db.collection("users").document(owner).collection("books").document(isbn).set(clickedBook);
                        db.collection("books").document(isbn).set(clickedBook);
                    }
                    db.collection("books").document(isbn).set(clickedBook);
                    db.collection("users").document(borrower).collection("outgoingRequests").document(requestID).set(request);
                    db.collection("users").document(owner).collection("incomingRequests").document(requestID).set(request);
                    bookAdapter.notifyDataSetChanged();
                }
            }
        });


        //on item click listener for list of books
        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * This method facilitates the selection of a Book object via a click on the ListView
             * @param parent
             *        The parent AdapterView
             * @param view
             *        The activity's View
             * @param position
             *        The position of the item
             * @param id
             *        The ID of the item
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //background colour of latest clicked book should be highlighted #C0EFE5

                clickedBook = (Book)parent.getItemAtPosition(position);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
