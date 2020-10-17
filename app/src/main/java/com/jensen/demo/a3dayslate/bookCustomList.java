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

public class bookCustomList extends ArrayAdapter<Book> {


    //init context
    private ArrayList<Book> books;
    private Context context;

    //constructor implementing super construction from ArrayAdapter
    public bookCustomList(Context context, ArrayList<Book> books){
        super(context,0, books);
        this.books = books;
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = convertView;

        //inflate view
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.book_list_content,parent,false);
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
        if (book.getCurrentStatus() == Book.statuses.ACCEPTED) {
            bookStatus.setText("Accepted"); //HOW TO TURN ENUM TO STRING?
        }
        else if (book.getCurrentStatus() == Book.statuses.AVAILABLE) {
            bookStatus.setText("Available"); //HOW TO TURN ENUM TO STRING?
        }
        else if (book.getCurrentStatus() == Book.statuses.REQUESTED) {
            bookStatus.setText("Requested"); //HOW TO TURN ENUM TO STRING?
        }
        else if (book.getCurrentStatus() == Book.statuses.BORROWED) {
            bookStatus.setText("Borrowed"); //HOW TO TURN ENUM TO STRING?
        }

        return view;
    }

}
