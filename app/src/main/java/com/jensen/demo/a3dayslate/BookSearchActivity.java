package com.jensen.demo.a3dayslate;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

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
        final FirebaseUser currentUser = uAuth.getCurrentUser();

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

        // query database for all books
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
                                Book newBook = document.toObject(Book.class);
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
                    //requestID is owner Username + borrower Username + isbn
                    String borrower = currentUser.getDisplayName();
                    Request request = new Request(borrower, clickedBook);
                    String owner = clickedBook.getOwner();
                    String isbn = clickedBook.getIsbn();
                    String requestID = owner + borrower + isbn;
                    if (clickedBook.getCurrentStatus() != Book.statuses.REQUESTED){
                        Log.w("Available Larissa","Testing on Available books");
                        request.setStatus(Book.statuses.REQUESTED);
                        clickedBook.setCurrentStatus(Book.statuses.REQUESTED);
                        db.collection("users").document(owner).collection("books").document(isbn).set(clickedBook);
                        db.collection("books").document(isbn).set(clickedBook);
                    }
                    db.collection("books").document(isbn).set(clickedBook);
                    db.collection("users").document(borrower).collection("outgoingRequests").document(requestID).set(request);
                    db.collection("users").document(owner).collection("incomingRequests").document(requestID).set(request);
                    bookAdapter.notifyDataSetChanged();

                    // Send notification to owner
                    sendNotificationToOwner(db, owner, borrower);

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

    public void sendNotificationToOwner(FirebaseFirestore db, String owner, String currentUser) {
        db.collection("users").document(owner).collection("device-token").document("token").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    String token = (String) document.getData().get("deviceToken");
                    Log.w("RC", token);
                    String json = "{\"to\":\"" + token + "\",\"notification\":{\"title\":\"Incoming request\",\"body\":\"From: " + currentUser + "\"}}";
                    Log.w("RC", json);
                    String apiKey = "key=AAAAuSNnUgU:APA91bEe66HTssr9aKZtPmsIL_14Nw-PpIj6Hp_ULBA5rpezXbWEIRr4uujh6nIt0m2JcDd4CBPjYuQMOIGqVf5TKa2Q23EoUQNhcUg1850iFqvq9V7I3Lr8Uax2_bRSfE6cd4ydMTXI";
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = RequestBody.create(MediaType.parse("application/json"),json);
                    Log.w("RC", body.toString());
                    okhttp3.Request httpRequest = new okhttp3.Request.Builder()
                            .url("https://fcm.googleapis.com/fcm/send")
                            .header("content-type","application/json")
                            .header("Authorization", apiKey)
                            .post(body)
                            .build();
                    Log.w("RC1", httpRequest.toString());
                   client.newCall(httpRequest)
                           .enqueue(new Callback() {
                               @Override
                               public void onFailure(Call call, IOException e) {
                                   runOnUiThread(new Runnable() {
                                       @Override
                                       public void run() {
                                           // Can display a toast here
                                       }
                                   });
                               }

                               @Override
                               public void onResponse(Call call, Response response) throws IOException {
                                   Log.w("RC", Integer.toString(response.code()) + "BODY=" + response.body().toString());
                               }
                           });

                }
            }
        });
    }

}
