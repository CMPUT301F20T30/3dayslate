package com.jensen.demo.a3dayslate;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class IncomingRequestsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incoming_requests_activity);
        //declare xml attributes
        Button accept;
        Button decline;
        ListView incomingRequestsList;
        accept = findViewById(R.id.accept_request_button);
        decline = findViewById(R.id.decline_request_button);
        incomingRequestsList = findViewById(R.id.incoming_requests_listview);

        //TODO make adapter using incoming_requests content.xml

        //on click listerne for accept
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //on click listener for decline button
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //on item click listener or requests list
        incomingRequestsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}
