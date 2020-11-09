package com.jensen.demo.a3dayslate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
/* BorrowedBooksAdapter

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

/**
 * Adapter for use with the OwnedBooksActivity
 * @author Eric Weber
 * @version 1.0.0
 * @see BorrowedBooksActivity
 */

public class BorrowedBooksAdapter extends ArrayAdapter<Book> {
    private Context mContext;
    private int mResource;
    private String author;

    /**Constructor for adapter
     *
     * @param context
     * @param resource
     * @param books
     */
    public BorrowedBooksAdapter(@NonNull Context context, int resource, ArrayList<Book> books) {
        super(context, resource, books);
        this.mContext = context;
        mResource = resource;
    }

    /** gets the view for the adapter
     *
     * @param position
     * @param convertView
     * @param parent
     * @return convertedView
     */

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String isbn = getItem(position).getIsbn();
        ArrayList<String> authors = getItem(position).getAuthors();
        String title = getItem(position).getTitle();
        String owner = getItem(position).getOwner();

        //make a string for all authors
        for(int i =0; i<authors.size(); i++ ){
            if(i==0) {
                author = authors.get(i);
            }else{
                author = author + ", " + authors.get(i);
            }
        }

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        //declare textViews
        TextView bookTitle = convertView.findViewById(R.id.borrowed_book_title);
        TextView bookAuthor = convertView.findViewById(R.id.borrowed_book_author);
        TextView bookIsbn = convertView.findViewById(R.id.borrowed_book_isbn);
        TextView bookOwner = convertView.findViewById(R.id.borrowed_book_owner);

        //set textViews
        bookTitle.setText(title);
        bookAuthor.setText(author);
        bookIsbn.setText(isbn);
        bookOwner.setText(owner);

        return convertView;
    }
}
