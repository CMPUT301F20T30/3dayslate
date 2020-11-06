package com.jensen.demo.a3dayslate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/* IncomingRequestsBook Class

   Version 1.0.0

   October 31 2020

   Copyright [2020] [Larissa Zhang]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

/**A redirect activity within Incoming requests that
 * allows for a owner to look for requests by book. When
 * book is tapped, will send specific book to IncomingRequestsActivity
 * to show individual requests.
 *
 * @author Larissa Zhang
 * @see IncomingRequestsActivity
 * @version 1.0.0
 */

public class IncomingRequestsBooks extends AppCompatActivity {

    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    final FirebaseAuth uAuth = FirebaseAuth.getInstance();
    final FirebaseUser currentUser = uAuth.getCurrentUser();

    /** Creates a listview that shows all the books that
     * have requests for the current user.
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_requests_books);

        ListView listBooks = findViewById(R.id.incoming_requests_book_listview);

        ArrayList<String> titleBooks = new ArrayList<>();
        ArrayList<Book> reqBooks = new ArrayList<>();
        ArrayList<Request> allRequests = new ArrayList<>();
        ArrayAdapter reqBooksAdapter = new ArrayAdapter<String>(this, R.layout.incoming_request_book_content,titleBooks);

        listBooks.setAdapter(reqBooksAdapter);
        //going through all requests to get the book that have requests
        db.collection("users").document(currentUser.getDisplayName()).
                collection("incomingRequests")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Request newRequest = document.toObject(Request.class);
                                Log.d("REQUEST:","Request Added");
                                allRequests.add(newRequest);
                                Book book = newRequest.getBook();
                                if (!reqBooks.contains(book)){
                                    reqBooks.add(book);
                                    titleBooks.add(book.getTitle());
                                    Log.d("TITLE:",book.getTitle());
                                }
                            }
                            reqBooksAdapter.notifyDataSetChanged();
                        }
                    }
                });
        //when item clicked will take selected book to next activity

        listBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = reqBooks.get(position);
                Intent intent= new Intent(IncomingRequestsBooks.this, IncomingRequestsActivity.class);
                intent.putExtra("book",book);
                startActivity(intent);
            }
        });
    }
}