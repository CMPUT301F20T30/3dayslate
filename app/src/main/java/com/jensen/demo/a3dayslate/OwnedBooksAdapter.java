package com.jensen.demo.a3dayslate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class OwnedBooksAdapter extends ArrayAdapter<Book> {
    private Context mContext;
    private int mResource;
    private String author;

    public OwnedBooksAdapter(@NonNull Context context, int resource, ArrayList<Book> books) {
        super(context, resource, books);
        this.mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String isbn = getItem(position).getIsbn();
        ArrayList<String> authors = getItem(position).getAuthors();
        Book.statuses status = getItem(position).getCurrentStatus();
        String statusString = status.toString();
        String title = getItem(position).getTitle();


        //make a string for all authors
        for(int i =0; i<authors.size(); i++ ){
            if(i==0) {
                author = authors.get(i);
            }else{
                author = author + ", " + authors.get(i);
            }
        }
        //TODO implement user class so this can be done
        // User borrower = getItem(position).getBorrower();
        // String borrowerName = borrower.getName();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        //declare textViews
        TextView bookTitle = convertView.findViewById(R.id.owned_book_title);
        TextView bookAuthor = convertView.findViewById(R.id.owned_book_author);
        TextView bookStatus = convertView.findViewById(R.id.owned_book_status);
        TextView bookIsbn = convertView.findViewById(R.id.owned_book_isbn);
        //TextView bookBorrower = convertView.findViewById(R.id.owned_book_borrower)

        //set textViews
        bookTitle.setText(title);
        bookAuthor.setText(author);
        bookStatus.setText(statusString);
        bookIsbn.setText(isbn);
        //bookBorrower.setText(borrowerName)



        return convertView;
    }
}
