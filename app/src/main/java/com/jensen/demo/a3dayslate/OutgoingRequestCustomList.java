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

/* OutgoingRequestCustomList class

   Version 1.0.0

   October 17 2020

   Copyright [2020] [Jensen Khemchandani]

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
 * This class allows us to make a custom display for each Request object in our OutgoingRequestsActivity
 * Also attaches the listview to the ArrayList of Requests
 * @author Jensen Khemchandani
 * @version 1.0.0
 * @see OutgoingRequestsActivity
 */


public class OutgoingRequestCustomList extends ArrayAdapter<Request> {
    private ArrayList<Request> requests ;
    private Context context;

    /**
     * Constructor
     * @param context
     * The current context of the app
     * @param requests
     * The ArrayList of requests to attach the adapter to
     */

    public OutgoingRequestCustomList(Context context, ArrayList<Request> requests){
        super(context,0, requests);
        this.requests = requests;
        this.context = context;
    }

    /**
     * Gets and sets up the view for the adapter
     * @param position
     * The position in the ArrayList
     * @param convertView
     * The view to be converted
     * @param parent
     * The parent ViewGroup
     * @return
     * Returns a view for the adapter
     */

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = convertView;

        //inflate view
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.outgoing_requests_content,parent,false);
        }

        Request request = requests.get(position);

        TextView bookTitle = view.findViewById(R.id.outgoing_requests_book);
        TextView bookOwner = view.findViewById(R.id.outgoing_requests_owner);
        TextView requestStatus= view.findViewById(R.id.outgoing_requests_status);


        bookTitle.setText("Title: " + request.getBook().getTitle());
        bookOwner.setText("Owner: " + request.getOwner());

        if (request.getStatus() == Book.statuses.REQUESTED) {
            requestStatus.setText("Requested...");
        }
        else if (request.getStatus() == Book.statuses.ACCEPTED) {
            requestStatus.setText("Accepted! -> Long tap to see location!");
        }
        return view;
    }

}

