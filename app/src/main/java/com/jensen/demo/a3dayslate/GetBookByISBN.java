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
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


 public class GetBookByISBN extends AppCompatActivity {

     /*
       Implements the ability for an owner to add a new book to their collection.
       Allows the user to accomplish this task by scanning in a barcode with their camera, or entering an ISBN code manually

       Uses the Google books API for mapping an ISBN to a book

       Contains a method to store a newly created book in the database under the owner

       @author: Jensen Khemchandani
       @see: Rewrite for .java classes that use it
       @version:1.0.0

   */

    private TextView bookText;
    public static TextView ISBNResult; // Temporary public variable
    private Button scan_button;
    private Button enter_button;
    private EditText enter_isbn;
    private String titleText;
    String bodyText;

    // Request codes
    int scanISBNRequestCode = 1;

    // Camera stuff
    private static final int REQUEST_CAMERA_PERMISSION = 1144;
    private BarcodeScanner scanner;

    // URL Stuff
    final String baseURL = "https://www.googleapis.com/books/v1/volumes?q=ISBN:";
    final OkHttpClient httpClient = new OkHttpClient();

    // Firebase instances
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    final FirebaseAuth uAuth = FirebaseAuth.getInstance();
    final FirebaseUser currentUser = uAuth.getCurrentUser();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getbookbyisbnlayout);
        scan_button = (Button) findViewById(R.id.scanISBNButton);
        enter_button = (Button) findViewById(R.id.enterISBNButton);
        enter_isbn = (EditText) findViewById(R.id.enterISBNCode);
        scan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(), BarcodeScannerActivity.class), scanISBNRequestCode);
            }
        });
        enter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String isbn;
                String url;
                if(enter_isbn.getText().length() == 0) {
                    return;
                }
                isbn = enter_isbn.getText().toString();
                url = baseURL + isbn; // Get the actual URL for the request
                ArrayList<String> authorList = new ArrayList<String>();

                Request request = new Request.Builder()
                        .url(url)
                        .build();

                // Access the google books API to get the relevant information for a book based on it's ISBN
                httpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        e.printStackTrace();
                        return;
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if (response.isSuccessful()) {
                            //Log.w("TAG", "ADD BOOK TEST " + titleText[0]);
                            bodyText = response.body().string();
                            try {
                                // This code will be refactored later and does not currently accomplish anything
                                JSONObject jsonObject = new JSONObject(bodyText);
                                JSONArray itemsArray = jsonObject.getJSONArray("items");
                                JSONObject items = itemsArray.getJSONObject(0);
                                JSONObject volInfo = items.getJSONObject("volumeInfo"); // Contains relevant info about the book
                                JSONArray authors = volInfo.getJSONArray("authors");
                                // Actual fields for the book
                                titleText = volInfo.getString("title"); // Title
                                // Populate the authors list
                                for(int i = 0; i < authors.length(); i++) {
                                    authorList.add(authors.getString(i));
                                }
                                Log.w("TAG", "ADD BOOK TEST " + titleText + " " + authorList.get(0));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return;
                            }

                            Book createdBook = new Book(titleText, isbn, authorList, currentUser.getDisplayName());
                            // Add book to the database here!! -----------------------
                            Log.w("TAG", "JENSEN" + createdBook.getTitle() + createdBook.getIsbn() + createdBook.getAuthors().get(0) + createdBook.getOwner());
                            addBook(createdBook);

                        }
                        // If response not successful, do nothing?
                    }
                });
                enter_isbn.setText("");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // For receiving information from the barcode-scanner
        if(resultCode == scanISBNRequestCode) {
            boolean valid = true;
            Bundle bundle = data.getBundleExtra("bundle");
            String isbn = bundle.getString("ISBN");
            String baseURL = "https://www.googleapis.com/books/v1/volumes?q=ISBN:";
            String url = baseURL + isbn;
            ArrayList<String> authorList = new ArrayList<String>();
            //Book createdBook = getBook(url, httpClient, isbn);
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            // Access the google books API to get the relevant information for a book based on it's ISBN
            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    e.printStackTrace();
                    return;
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if (response.isSuccessful()) {
                        //Log.w("TAG", "ADD BOOK TEST " + titleText[0]);
                        bodyText = response.body().string();
                        try {
                            // This code will be refactored later and does not currently accomplish anything
                            JSONObject jsonObject = new JSONObject(bodyText);
                            JSONArray itemsArray = jsonObject.getJSONArray("items");
                            JSONObject items = itemsArray.getJSONObject(0);
                            JSONObject volInfo = items.getJSONObject("volumeInfo"); // Contains relevant info about the book
                            JSONArray authors = volInfo.getJSONArray("authors");
                            // Actual fields for the book
                            titleText = volInfo.getString("title"); // Title
                            // Populate the authors list
                            for (int i = 0; i < authors.length(); i++) {
                                authorList.add(authors.getString(i));
                            }
                            Log.w("TAG", "ADD BOOK TEST " + titleText + " " + authorList.get(0));
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                        // Store it to the DB and other fun stuff
                        Book createdBook = new Book(titleText, isbn, authorList, currentUser.getDisplayName());
                        Log.w("TAG", "JENSEN" + createdBook.getTitle() + createdBook.getIsbn() + createdBook.getAuthors().get(0) + createdBook.getOwner());
                        // Add book to the database here!! -----------------
                        addBook(createdBook);

                    }
                    // If response not successful, do nothing?
                }
            });
        }
    }

    private void addBook(Book book){
        //Adds book to a specific user
        db.collection("users").document(currentUser.getDisplayName()).
                collection("books").document(book.getIsbn()).set(book);
        db.collection("books").document(book.getIsbn()).set(book);
    }//end addBook


}

