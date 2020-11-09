package com.jensen.demo.a3dayslate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

import java.util.ArrayList;

//activity that has not been worked on yet

public class BorrowedBooksActivity extends AppCompatActivity {

    private final ArrayList<Book> myBooks = new ArrayList<>();
    private BorrowedBooksAdapter booksAdapter;
    private Book clickedBook = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.borrowed_books_activity);

        //declare listView
        ListView borrowedBooksList;
        borrowedBooksList = findViewById(R.id.borrowed_books_listview);
        borrowedBooksList.setAdapter(booksAdapter);

        //TODO make adapter utilizing book_list_content.xml
        //This can be the same adapter used for OwnedBooksActivity

        // gets the current user from the database
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseAuth uAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = uAuth.getCurrentUser();



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
                        booksAdapter = new BorrowedBooksAdapter(BorrowedBooksActivity.this, R.layout.borrowed_book_list_content, myBooks);
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
    }
}
