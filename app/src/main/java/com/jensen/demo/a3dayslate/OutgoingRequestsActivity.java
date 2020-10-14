package com.jensen.demo.a3dayslate;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class OutgoingRequestsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.outgoing_requests_activity);

        //declare listView
        ListView outgoingRequestsList;
        outgoingRequestsList = findViewById(R.id.outgoing_requests_listview);

        //TODO make an adapter using outgoing_requests_content.xml

        //on item click listener for outgoing request
        outgoingRequestsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}
