package com.jensen.demo.a3dayslate;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//activity that has not been worked on yet

public class BorrowedBooksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.borrowed_books_activity);

        //declare listView
        ListView borrowedBooks;
        borrowedBooks = findViewById(R.id.borrowed_books_listview);

        //TODO make adapter utilizing book_list_content.xml
        //This can be the same adapter used for BookSearchActivity

        //item click listener for borrowed book list
        borrowedBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}
