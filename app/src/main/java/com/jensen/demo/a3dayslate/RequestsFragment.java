package com.jensen.demo.a3dayslate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/* FilterFragment

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
 * Fragment for choosing what requests to view
 * @author Eric Weber
 * @version 1.0.0
 * @see DashboardActivity
 */

public class RequestsFragment extends DialogFragment{

    View view;

    /** Attaches fragment to activity
     *
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    /** Sets up buttons in filter
     *
     * @param savedInstanceState
     */
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
