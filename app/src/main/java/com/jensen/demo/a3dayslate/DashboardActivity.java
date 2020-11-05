package com.jensen.demo.a3dayslate;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {
    TextView userDisplay;
    Button bookSearch;
    Button peopleSearch;
    Button myBooks;
    Button borrowedBooks;
    Button myProfile;
    Button scanISBN;
    Button myRequests;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        // declare all xml elements

        userDisplay = findViewById(R.id.usernameDisplay);
        bookSearch = findViewById(R.id.bookSearchButton);
        peopleSearch = findViewById(R.id.peopleSearchButton);
        myBooks = findViewById(R.id.myBooksButton);
        borrowedBooks = findViewById(R.id.borrowedBooksButton);
        myProfile = findViewById(R.id.myProfileButton);
        scanISBN = findViewById(R.id.scanISBNButton);
        myRequests = findViewById(R.id.myRequestsButton);

        // gets the current user from the database
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseAuth uAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = uAuth.getCurrentUser();

        // Device ID storage
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(DashboardActivity.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                Map<String, Object> token = new HashMap<>();
                token.put("deviceToken", instanceIdResult.getToken());
                db.collection("users").document(currentUser.getDisplayName()).collection("device-token").document("token").set(token);
            }
        });

        String userString = "Welcome " + currentUser.getDisplayName();
        userDisplay.setText(userString);

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
                //start the owned books activity
                Intent intent = new Intent(DashboardActivity.this, OwnedBooksActivity.class);
                startActivity(intent);
            }
        });

        //on click listener for borrowedBooks
        borrowedBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start view borrowed books activity
                Intent intent = new Intent(DashboardActivity.this, BorrowedBooksActivity.class);
                startActivity(intent);
            }
        });

        //on click listener for myProfile
        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ViewProfileActivity.class);
                startActivity(intent);
            }
        });

        //on click listener for scanISBN
        scanISBN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, GetBookByISBN.class);
                startActivity(intent);
            }
        });

        //on click listener for myRequests
        myRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RequestsFragment().show(getSupportFragmentManager(), "REQUESTS");
            }
        });

    }
}
