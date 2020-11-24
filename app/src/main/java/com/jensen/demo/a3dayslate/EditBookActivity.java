package com.jensen.demo.a3dayslate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;


/* EditBookActivity

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

/** This activity allows for the editing of a book
 *  It contains edit text field for editing the title and author of a book
 *  and a confirm edit button to finalize edits
 *
 *  It allows users to add, edit and delete images attached to books
 *
 * @author Eric Weber
 * @see OwnedBooksActivity
 * @version 1.0.0
 */
public class EditBookActivity extends AppCompatActivity implements Serializable{

    private Book book;
    private String authorString;

    // Firebase instances
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    final FirebaseAuth uAuth = FirebaseAuth.getInstance();
    final FirebaseUser currentUser = uAuth.getCurrentUser();
    private int PHOTO_GALLERY = 1;
    ImageView bookImage;
    public Boolean imagePresent = false;
    private byte[] imageByteArray;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference imagesRef = storageRef.child("images");
    StorageReference bookImagesRef;

    /** Sets up activity upon creation
     * including all buttons and editTexts
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);
        //declare xml attributes
        Button confirmEdit;
        Button editImage;
        Button deleteImage;
        EditText editTitle;
        EditText editAuthor;
        TextView isbn;
        TextView owner;
        TextView displayError;
        confirmEdit = findViewById(R.id.edit_book_confirm);
        editImage = findViewById(R.id.edit_book_edit_image);
        editTitle = findViewById(R.id.edit_book_title);
        editAuthor = findViewById(R.id.edit_book_author);
        isbn = findViewById(R.id.edit_book_isbn);
        owner = findViewById(R.id.edit_book_owner);
        displayError = findViewById(R.id.edit_book_error);
        bookImage = findViewById(R.id.edit_book_image);
        deleteImage = findViewById(R.id.edit_book_delete_image);

        Intent intent = getIntent();
        book = (Book)intent.getSerializableExtra("book");




        try{
            // display image if book already has images
            bookImagesRef = imagesRef.child(book.getIsbn());
            final long MBYTE = (4*1024)*(4*1024);
            bookImagesRef.getBytes(MBYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
               @Override
               public void onSuccess(byte[] bytes) {
                   Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                   imagePresent = true;
                   bookImage.setImageBitmap(Bitmap.createScaledBitmap(bmp, bookImage.getWidth(), bookImage.getHeight(), false));
                   imageByteArray = bytes;
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
        editTitle.setText(book.getTitle());
        editAuthor.setText(authorString);
        isbn.setText(book.getIsbn());
        owner.setText( ownerString);

        // on click listener for image
        bookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check that image is present
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

        //on click listener for editing images
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, PHOTO_GALLERY);
            }
        });

        //on click listener for deleting images
        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete the file
                try {
                    bookImagesRef.delete();
                    restartActivity();
                }catch (Exception e){
                    Log.d("DELETE ERROR", "image couldn't be deleted");
                }
            }

        });

        //on click listener for confirming edits
        confirmEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get new titles and authors from their editText fields
                String newTitle = editTitle.getText().toString();
                String newAuthor = editAuthor.getText().toString();

                //if the fields have proper input
                if(newTitle.length()!=0 && newAuthor.length()!=0){
                    book.setTitle(newTitle);

                    ArrayList<String> newAuthors = new ArrayList<>(Arrays.asList(newAuthor.split(",[ ]*")));
                    Log.d("NEWAUTHORS",newAuthors.toString());
                    book.setAuthors(newAuthors);

                    //put changes in database in all required places
                    db.collection("users").document(currentUser.getDisplayName()).collection("books").document(book.getIsbn()).set(book);
                    db.collection("books").document(book.getIsbn()).set(book);

                    //return to owned books activity
                    Intent returnItemIntent = new Intent();
                    returnItemIntent.putExtra("BOOK", book);
                    setResult(EditBookActivity.RESULT_OK, returnItemIntent);
                    finish();
                }else{
                    Log.d("IMPROPER ENTRY", newTitle.length() + " " + newAuthor.length());
                    displayError.setText("Invalid title or author(s) entered, try again.");
                }

            }
        });


    }

    /**
     * Restarts activity
     */
    private void restartActivity(){
            this.recreate();
    }

    /**
     * handles choosing image from photo gallery
     *
     * @param reqCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                bookImage.setImageBitmap(selectedImage);
                bookImagesRef = imagesRef.child(book.getIsbn());
                Log.d("UGHJ", "IobuIBOHPBVJKIJBOPIBJVKBOIHPBJVJBIOPIBJVKBIOPIBJVKKBGUJGBGOHU_");
                UploadTask uploadTask = bookImagesRef.putFile(imageUri);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Log.d("UNSUCCESSFUL UPLOAD", "");
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("UPLOAD SUCCESSFUL", bookImagesRef.getClass().toString());
                        restartActivity();

                        //Log.d("BOOK IMAGE REFERENCE", book.getImage().toString());


                        //put changes in database in all required places


                        // get download url and put as image field in book and then update book
                        //Log.d("SUCCESSFULUPLOAD", imagesRef.getDownloadUrl().getClass().toString());
                    }
                });

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.d("CAUGHT EXCPETION", "try again loser");
                //Toast.makeText(PostImage.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }
    }
}
