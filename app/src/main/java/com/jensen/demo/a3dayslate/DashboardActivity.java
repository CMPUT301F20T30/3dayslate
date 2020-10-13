package com.jensen.demo.a3dayslate;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;



public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        // declare all xml elements
        TextView userDisplay;
        Button bookSearch;
        Button peopleSearch;
        Button myBooks;
        Button borrowedBooks;
        Button myProfile;
        Button scanISBN;
        Button myRequests;
        userDisplay = findViewById(R.id.usernameDisplay);
        bookSearch = findViewById(R.id.bookSearchButton);
        peopleSearch = findViewById(R.id.peopleSearchButton);
        myBooks = findViewById(R.id.myBooksButton);
        borrowedBooks = findViewById(R.id.borrowedBooksButton);
        myProfile = findViewById(R.id.myProfileButton);
        scanISBN = findViewById(R.id.scanISBNButton);
        myRequests = findViewById(R.id.myRequestsButton);

        /*
        once user is actually passed from login
        set userDisplay text here
        userDisplay.setText("Welcome " + user.get_username());
         */
        //on click listener for bookSearch

        bookSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start the book search activity
                Intent intent = new Intent(DashboardActivity.this, BookSearchActivity.class);
                startActivity(intent);
            }
        });

        //on click listener for peopleSearch
        peopleSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start the user search activity
                Intent intent = new Intent(DashboardActivity.this, UserSearchActivity.class);
                startActivity(intent);
            }
        });

        //on click listener for myBooks
        myBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //on click listener for borrowedBooks
        borrowedBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //on click listener for myProfile
        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //on click listener for scanISBN
        scanISBN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, getBookByISBN.class);
                startActivity(intent);
            }
        });

        //on click listener for myRequests
        myRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
