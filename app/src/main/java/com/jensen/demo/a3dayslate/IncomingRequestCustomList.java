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

public class IncomingRequestCustomList extends ArrayAdapter<Request> {
    private ArrayList<Request> requests ;
    private Context context;


    // Constructor
    public IncomingRequestCustomList(Context context, ArrayList<Request> requests){
        super(context,0, requests);
        this.requests = requests;
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
            view = LayoutInflater.from(context).inflate(R.layout.incoming_requests_content,parent,false);
        }

        Request request = requests.get(position);

        TextView bookTitle = view.findViewById(R.id.incoming_requests_book);
        TextView bookRequester = view.findViewById(R.id.incoming_requests_requester);
        TextView requestStatus= view.findViewById(R.id.incoming_requests_status);


        bookTitle.setText("Title: " + request.getBook().getTitle());
        bookRequester.setText("Requester: " + request.getRequester());

        if (request.getStatus() == Book.statuses.REQUESTED) {
            requestStatus.setText("Pending...");
        }
        else if (request.getStatus() == Book.statuses.ACCEPTED) {
            requestStatus.setText("Accepted!");
        }

        return view;

    }

}
