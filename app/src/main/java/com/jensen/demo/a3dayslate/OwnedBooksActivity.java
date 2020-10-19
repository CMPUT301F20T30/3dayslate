package com.jensen.demo.a3dayslate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import java.util.HashMap;

public class OwnedBooksActivity extends AppCompatActivity {

    private ArrayList<Book>  myBooks = new ArrayList<>();
    private OwnedBooksAdapter booksAdapter;
    public int itempos = -1; //Used to keep track of last click


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owned_books_activity);


        //declare xml attributes
        final ListView ownedBooksList;
        Button addBook;
        Button deleteBook;
        Button filterBook;
        Button editBook;
        ownedBooksList = findViewById(R.id.owned_books_listview);
        addBook = findViewById(R.id.add_book_button);
        deleteBook = findViewById(R.id.delete_book_button);
        filterBook = findViewById(R.id.filter_book_button);
        editBook = findViewById(R.id.edit_book_button);

        // gets the current user from the database
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseAuth uAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = uAuth.getCurrentUser();



        // query database for all books owned by current user
        db.collection("users").document(currentUser.getDisplayName()).
                collection("books")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if (task.isSuccessful()) {
                           for (QueryDocumentSnapshot document : task.getResult()) {
                               // log book in database
                               Log.w("BOOK:", document.getId() + "=>" + document.getData());
                               //make new bookObject
                               Book newBook = new Book((String)document.get("title"), (String)document.get("isbn"), (ArrayList<String>)document.get("authorList"), (String)currentUser.getDisplayName());
                               // log new book object
                               Log.w("BOOK Object:", newBook.getTitle() + " " +newBook.getIsbn() + " " + newBook.getAuthors());
                               // add book to myBooks
                               myBooks.add(newBook);

                           }
                       } else {
                           Log.w("BOOK:", "Error getting documents");
                       }
                       Log.w("BOOK ARRAYLIST:", myBooks.toString());
                       adapter(myBooks, ownedBooksList);
                   }

               });



        //TODO make adapter using owned_book_list_content.xml



        ownedBooksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itempos = position;
            }
        });

        //on click listener for adding books
        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnedBooksActivity.this, getBookByISBN.class);
                startActivity(intent);
            }
        });

        //on click listener for deleting books
        deleteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itempos != -1) {
                    db.collection("users").document(currentUser.getDisplayName()).
                            collection("books").
                            document(myBooks.get(itempos).getIsbn()).delete();
                    //booksAdapter.notifyDataSetChanged();
                    for(int i = 0; i < myBooks.size(); i++){
                        if(myBooks.get(i).getIsbn().equals(myBooks.get(itempos).getIsbn())){
                            myBooks.remove(i);
                        }
                    }
                    adapter(myBooks, ownedBooksList);
                    itempos = -1;
                }
            }
        });

        //on click listener for filtering books
        filterBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //on click listener for edit book
        editBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void adapter(ArrayList<Book> books, ListView ownedBooksList){
        booksAdapter = new OwnedBooksAdapter(this, R.layout.owned_book_list_content, books);
        ownedBooksList.setAdapter(booksAdapter);
        myBooks = books;
        Log.w("BOOK ARRAYLIST:", myBooks.toString());
    }

}
