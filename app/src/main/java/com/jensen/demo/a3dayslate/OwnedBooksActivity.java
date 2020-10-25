package com.jensen.demo.a3dayslate;

import android.content.Intent;
import android.graphics.Color;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/** This activity displays all books owned by the logged in user
 *  It contains buttons for adding a book, deleting a book, editing a book, and filtering the list (NOT IMPLEMENTED YET)
 * @ author Eric Weber
 */
public class OwnedBooksActivity extends AppCompatActivity implements Serializable {

    private ArrayList<Book>  myBooks = new ArrayList<>();
    private OwnedBooksAdapter booksAdapter;
    private Book clickedBook = null;
    private int EDIT_BOOK_ACTIVITY =1;

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

        ownedBooksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                clickedBook = (Book)parent.getItemAtPosition(position);

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
                if(clickedBook!=null) {
                    db.collection("users").document(currentUser.getDisplayName()).
                            collection("books").
                            document(clickedBook.getIsbn()).delete();
                    db.collection("books").document(clickedBook.getIsbn()).delete();
                    //booksAdapter.notifyDataSetChanged();

                    if(clickedBook!=null){
                        myBooks.remove(clickedBook);
                        clickedBook = null;
                    }

                    adapter(myBooks, ownedBooksList);
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
                // if book is clicked then go to edit activity for it
                if(clickedBook!=null){
                    Intent intent = new Intent(v.getContext(), EditBookActivity.class);
                    intent.putExtra("book", (Serializable) clickedBook);
                    startActivityForResult(intent, EDIT_BOOK_ACTIVITY);
                }
            }
        });
    }

    /** Sets up the adapter for owned books
     *
     * @param books ArrayList of books
     * @param ownedBooksList listview for books
     */
    private void adapter(ArrayList<Book> books, ListView ownedBooksList){
        booksAdapter = new OwnedBooksAdapter(this, R.layout.owned_book_list_content, books);
        ownedBooksList.setAdapter(booksAdapter);
        myBooks = books;
        Log.w("BOOK ARRAYLIST:", myBooks.toString());
    }

    /** Handles returning from activities with results
     *  Updates ArrayList of owned books with book returned
     * @param requestCode acitivity result requested
     * @param resultCode result of activity
     * @param data data returned from activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EDIT_BOOK_ACTIVITY){
            if (resultCode == EditBookActivity.RESULT_OK){
                Book editedBook = (Book)data.getExtras().getSerializable("BOOK");
                int pos =0;
                for(int i=0; i< myBooks.size(); i++){
                    if(myBooks.get(i).equals(clickedBook)){

                        pos = i;
                    }
                }
                myBooks.set(pos, editedBook);
                booksAdapter.notifyDataSetChanged();

            }
        }
    }
}
