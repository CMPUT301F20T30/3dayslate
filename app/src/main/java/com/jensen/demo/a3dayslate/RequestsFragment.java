package com.jensen.demo.a3dayslate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class RequestsFragment extends DialogFragment{

    View view;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        view = LayoutInflater.from(getActivity()).inflate(R.layout.requests_fragment, null);

        //declare buttons
        Button incoming = view.findViewById(R.id.incoming_requests_button);
        Button outgoing = view.findViewById(R.id.outgoing_requests_button);

        incoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent= new Intent(getActivity(), IncomingRequestsActivity.class);
                Intent intent= new Intent(getActivity(), IncomingRequestsBooks.class);
                startActivity(intent);
            }
        });

        outgoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), OutgoingRequestsActivity.class);
                startActivity(intent);
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Which Requests?")
                .setNegativeButton("Cancel", null)
                .create();


    }


}
