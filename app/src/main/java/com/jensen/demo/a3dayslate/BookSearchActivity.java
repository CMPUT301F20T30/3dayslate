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

public class BookSearchActivity extends AppCompatActivity implements Serializable{

    private bookCustomList bookAdapter;
    private Book clickedBook = null;
    private int VIEW_BOOK_ACTIVITY = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set view
        setContentView(R.layout.book_search_activity);

        /// database connection
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseAuth uAuth = FirebaseAuth.getInstance();

        //declare array and adapter
        ArrayAdapter<Book> bookAdapter;
        ArrayList<Book> bookDataList = new ArrayList<>();
        ArrayList<Book> searchDataList = new ArrayList<>();

        //declare all xml elements
        Button viewBook;
        Button requestBook;
        Button searchBook;
        ListView bookList;
        EditText searchBar;

        //find view by ID commands
        viewBook = findViewById(R.id.view_book_button);
        requestBook = findViewById(R.id.request_book_button);
        searchBook = findViewById(R.id.book_search_button);
        bookList = findViewById(R.id.book_search_listview);
        searchBar = findViewById(R.id.book_search_bar);

        //set Book Adapter to custom list view
        bookAdapter = new bookCustomList(this, searchDataList);
        bookList.setAdapter(bookAdapter);

        // query database for all books owned by current user
        db.collection("books")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // log book in database
                                Log.w("BOOK:", document.getId() + "=>" + document.getData());
                                //make new bookObject
                                Book newBook = new Book((String)document.get("title"),
                                        (String)document.get("isbn"),
                                        (ArrayList<String>)document.get("authorList"),
                                        (String)document.get("owner"));

                                String status = (String)document.get("availability");

                                if (status.equals("AVAILABLE")){
                                    newBook.setCurrentStatus(Book.statuses.AVAILABLE);
                                }
                                else if (status.equals("BORROWED")){
                                    newBook.setCurrentStatus(Book.statuses.BORROWED);
                                }
                                else if (status.equals("REQUESTED")){
                                    newBook.setCurrentStatus(Book.statuses.REQUESTED);
                                }
                                else if (status.equals("ACCEPTED")){
                                    newBook.setCurrentStatus(Book.statuses.ACCEPTED);
                                }

                                // log new book object
                                Log.w("BOOK Object:", newBook.getTitle() + " " +newBook.getIsbn() + " " + newBook.getAuthors());
                                // add book to myBooks
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

        //a little test
        /*ArrayList<String> authors = new ArrayList<>();
        authors.add("Author1");
        authors.add("Author2");
        Book book1 = new Book("BookTitle", "1234567890", authors, "Barry");
        bookAdapter.add(book1);*/
        //end test

        searchBook.setOnClickListener(new View.OnClickListener() {
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
            @Override
            public void onClick(View v) {
                if(clickedBook!=null){

                }
            }
        });

        //on item click listener for list of books
        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
