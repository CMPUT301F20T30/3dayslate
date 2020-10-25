package com.jensen.demo.a3dayslate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/** This activity allows for the editing of a book
 *  It contains edit text field for editing the title and author of a book
 *  and a confirm edit button to finalize edits
 * @ author Eric Weber
 */
public class EditBookActivity extends AppCompatActivity implements Serializable {

    private Book book;
    private String authorString;

    // Firebase instances
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    final FirebaseAuth uAuth = FirebaseAuth.getInstance();
    final FirebaseUser currentUser = uAuth.getCurrentUser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_book_activity);
        //declare xml attributes
        Button confirmEdit;
        EditText editTitle;
        EditText editAuthor;
        TextView isbn;
        TextView owner;
        TextView displayError;
        confirmEdit = findViewById(R.id.edit_book_confirm);
        editTitle = findViewById(R.id.edit_book_title);
        editAuthor = findViewById(R.id.edit_book_author);
        isbn = findViewById(R.id.edit_book_isbn);
        owner = findViewById(R.id.edit_book_owner);
        displayError = findViewById(R.id.edit_book_error);

        Intent intent = getIntent();
        book = (Book)intent.getSerializableExtra("book");

        //get a string for all authors
        ArrayList<String> authors = book.getAuthors();
        for(int i =0; i<authors.size(); i++ ){
            if(i==0) {
                authorString = authors.get(i);
            }else{
                authorString = authorString + ", " + authors.get(i);
            }
        }
        //set text for relevant fields
        String ownerString = "Owner: " + book.getOwner();
        editTitle.setText(book.getTitle());
        editAuthor.setText(authorString);
        isbn.setText(book.getIsbn());
        owner.setText( ownerString);

        confirmEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get new titles and authors from their editText fields
                String newTitle = editTitle.getText().toString();
                String newAuthor = editAuthor.getText().toString();

                //if the fields have proper input
                if(newTitle.length()!=0 && newAuthor.length()!=0){
                    book.setTitle(newTitle);

                    ArrayList<String> newAuthors = new ArrayList<>(Arrays.asList(newAuthor.split(",[ ]*")));
                    Log.d("NEWAUTHORS",newAuthors.toString());
                    book.setAuthors(newAuthors);

                    //put changes in database in all required places
                    db.collection("users").document(currentUser.getDisplayName()).
                            collection("books").document(book.getIsbn())
                            .update("title", newTitle, "authorList", newAuthors);
                    db.collection("books").document(book.getIsbn())
                            .update("title", newTitle, "authorList", newAuthors);
                    //return to owned books activity
                    Intent returnItemIntent = new Intent();
                    returnItemIntent.putExtra("BOOK", book);
                    setResult(EditBookActivity.RESULT_OK, returnItemIntent);
                    finish();
                }else{
                    //TODO display error message on activity
                    Log.d("IMPROPER ENTRY", newTitle.length() + " " + newAuthor.length());
                    displayError.setText("Invalid title or author(s) entered, try again.");
                }

            }
        });

    }
}
