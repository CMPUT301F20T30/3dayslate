package com.jensen.demo.a3dayslate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
 * @author Eric Weber
 * @version 1.0.0
 * @see OwnedBooksActivity
 */
public class ViewBookActivity extends AppCompatActivity implements Serializable {

    private Book book;
    private String authorString;

    //declare xml attributes
    TextView viewTitle;
    TextView viewAuthor;
    TextView isbn;
    TextView owner;
    ImageView bookImage;
    Boolean imagePresent = false;
    private byte[] imageByteArray;


    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference imagesRef = storageRef.child("images");
    StorageReference bookImagesRef;

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
        bookImage = findViewById(R.id.view_book_image);
        Intent intent = getIntent();
        book = (Book)intent.getSerializableExtra("book");


        try{
            bookImagesRef = imagesRef.child(book.getIsbn());
            final long MBYTE = (4*1024)*(4*1024);
            bookImagesRef.getBytes(MBYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageByteArray = bytes;
                    bookImage.setImageBitmap(Bitmap.createScaledBitmap(bmp, bookImage.getWidth(), bookImage.getHeight(), false));
                    imagePresent = true;
                }
            });

        }catch (Exception e){
            Log.d("ERROR", "e");
        }

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



        bookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imagePresent){
                    //start filter here
                    Bundle b = new Bundle();
                    b.putByteArray("image", imageByteArray);
                    ImageFragment fragment = new ImageFragment();
                    fragment.setArguments(b);
                    fragment.show(getSupportFragmentManager(), "IMAGE");
                }
            }
        });
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

