package com.jensen.demo.a3dayslate;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class   BookSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_search_activity);

        //declare all xml elements
        Button viewBook;
        Button requestBook;
        Button searchBook;
        ListView bookList;
        viewBook = findViewById(R.id.view_book_button);
        requestBook = findViewById(R.id.request_book_button);
        searchBook = findViewById(R.id.book_search_button);
        bookList = findViewById(R.id.book_search_listview);


        //TODO make an adapter for this utilizing book_list_content.xml


        searchBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //on click listener for view book button
        viewBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //on click listener for request book button
        requestBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //on item click listener for list of books
        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //background colour of latest clicked book should be highlighted #C0EFE5
            }
        });


    }
}
