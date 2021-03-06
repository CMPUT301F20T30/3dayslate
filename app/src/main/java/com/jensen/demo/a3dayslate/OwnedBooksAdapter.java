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
/* OwnedBooksAdapter

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
 * @see OwnedBooksActivity
 */
public class OwnedBooksAdapter extends ArrayAdapter<Book> {
    private Context mContext;
    private int mResource;
    private String author;

    /**Constructor for adapter
     *
     * @param context
     * @param resource
     * @param books
     */
    public OwnedBooksAdapter(@NonNull Context context, int resource, ArrayList<Book> books) {
        super(context, resource, books);
        this.mContext = context;
        mResource = resource;
    }

    /** gets the view for the apdapter
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
        Book.statuses status = getItem(position).getCurrentStatus();
        String statusString;
        if(status == Book.statuses.SCANNED) {
            status = Book.statuses.ACCEPTED;
            statusString = status.toString();
        } else if (status == Book.statuses.RETURNING){
            status = Book.statuses.BORROWED;
            statusString = status.toString();
        }
        else {
            statusString = status.toString();
        }
        String title = getItem(position).getTitle();
        String borrowerName = getItem(position).getBorrower();
        if(borrowerName.equals("") || status == Book.statuses.AVAILABLE){
            borrowerName = "No Borrower";
        }

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
        TextView bookTitle = convertView.findViewById(R.id.owned_book_title);
        TextView bookAuthor = convertView.findViewById(R.id.owned_book_author);
        TextView bookStatus = convertView.findViewById(R.id.owned_book_status);
        TextView bookIsbn = convertView.findViewById(R.id.owned_book_isbn);
        TextView bookBorrower = convertView.findViewById(R.id.owned_book_borrower);

        //set textViews
        bookTitle.setText(title);
        bookAuthor.setText(author);
        bookStatus.setText(statusString);
        bookIsbn.setText(isbn);
        bookBorrower.setText(borrowerName);

        return convertView;
    }
}
