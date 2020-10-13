package com.jensen.demo.a3dayslate;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class OwnedBooksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owned_books_activity);

        //declare xml attributes
        ListView ownedBooksList;
        Button addBook;
        Button deleteBook;
        Button filterBook;
        Button editBook;
        ownedBooksList = findViewById(R.id.owned_books_listview);
        addBook = findViewById(R.id.add_book_button);
        deleteBook = findViewById(R.id.delete_book_button);
        filterBook = findViewById(R.id.filter_book_button);
        editBook = findViewById(R.id.edit_book_button);

        //TODO make adapter using owned_book_list_content.xml

        ownedBooksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        //on click listener for adding books
        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //on click listener for deleting books
        deleteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
}
