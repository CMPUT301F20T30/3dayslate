package com.jensen.demo.a3dayslate;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;

/* BookCustomList class

   Version 1.0.0

   October 17 2020

   Copyright [2020] [Danny Zaiter]

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

/**
 * This class is an extension of an ArrayAdapter which is used to implement the
 * BookSearchActivity class, it stores an ArrayList of Books and defines the format
 * in which they are displayed (using R.layout.book_list_content XML)
 * @author Danny Zaiter
 * @version 1.0.0
 * @see BookSearchActivity
 *
 */
public class BookCustomList extends ArrayAdapter<Book> {

    //init context
    private ArrayList<Book> books;
    private Context context;

    /**
     * This method is a generic constructor used to create a bookCustomList object
     * @param context
     *        The necessary context to be passed in
     * @param books
     *        The ArrayList of Book objects to be displayed
     */
    public BookCustomList(Context context, ArrayList<Book> books){
        super(context,0, books);
        this.books = books;
        this.context = context;
    }

    /**
     * This method returns the defined View object
     *
     * @param position
     *        The position of the item
     * @param convertView
     *        In this case NULL, to be inflated
     * @param parent
     *        parent of the View object used to match dimensions
     *
     * @return a View Object
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = convertView;

        //inflate view
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content_book_list,parent,false);
        }

        //get necessary Gear from list
        Book book = books.get(position);

        //identifies all text boxes
        TextView bookTitle = view.findViewById(R.id.book_title);
        TextView bookAuthor = view.findViewById(R.id.book_author);
        TextView bookStatus = view.findViewById(R.id.book_status);
        TextView bookOwner = view.findViewById(R.id.book_owner);
        TextView bookISBN = view.findViewById(R.id.book_isbn);


        //setting all fields of list view (including joining of authors into string)
        bookTitle.setText(book.getTitle());
        bookAuthor.setText(String.join(", ", book.getAuthors()));
        bookOwner.setText(book.getOwner());
        bookISBN.setText(book.getIsbn());

        //check each Book ENUM individually to set display string
        if (book.getCurrentStatus() == Book.statuses.ACCEPTED || book.getCurrentStatus() == Book.statuses.SCANNED) {
            bookStatus.setText("Accepted");
        }
        else if (book.getCurrentStatus() == Book.statuses.AVAILABLE) {
            bookStatus.setText("Available");
        }
        else if (book.getCurrentStatus() == Book.statuses.REQUESTED) {
            bookStatus.setText("Requested");
        }
        else if (book.getCurrentStatus() == Book.statuses.BORROWED || book.getCurrentStatus() == Book.statuses.RETURNING) {
            bookStatus.setText("Borrowed");
        }

        return view;
    }

}
