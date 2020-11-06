package com.jensen.demo.a3dayslate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;

/* ViewBookActivity

   Version 1.0.0

   October 17 2020

   Copyright [2020] [Eric Weber]

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

/** View Book Activity allows for the viewing of information of 1 (one) book
 *  it displays the book owner, isbn, title, author(s)
 *
 */
public class ViewBookActivity extends AppCompatActivity implements Serializable {

    private Book book;
    private String authorString;

    //declare xml attributes
    TextView viewTitle;
    TextView viewAuthor;
    TextView isbn;
    TextView owner;

    /** sets up activity upon creation
     * including
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_book_activity);


        viewTitle = findViewById(R.id.view_book_title);
        viewAuthor = findViewById(R.id.view_book_author);
        isbn = findViewById(R.id.view_book_isbn);
        owner = findViewById(R.id.view_book_owner);

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
        viewTitle.setText(book.getTitle());
        viewAuthor.setText(authorString);
        isbn.setText(book.getIsbn());
        owner.setText(ownerString);




    }

    /** returns to previous activity in activity stack
     *  when back button is pressed
     */
    @Override
    public void onBackPressed() {
        setResult(ViewBookActivity.RESULT_OK);
        finish();
    }
}

