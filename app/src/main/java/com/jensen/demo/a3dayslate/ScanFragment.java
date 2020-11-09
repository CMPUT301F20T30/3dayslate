package com.jensen.demo.a3dayslate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/* FilterFragment

   Version 1.0.0

   November 7 2020

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
 * A simple fragment that allows a user to select what type of scan to perform. (Borrowing/adding/viewing a book)
 * @author Jensen Khemchandani
 * @version 1.0.0
 */
public class ScanFragment extends DialogFragment {

    // View of the activity we are in
    View view;

    /**
     * Attaches fragment to activity
     *
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    /**
     * Sets up buttons in filter
     *
     * @param savedInstanceState
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_scan, null);

        //declare buttons
        Button viewButton = view.findViewById(R.id.view_book_scan_button);
        Button addButton = view.findViewById(R.id.add_book_scan_button);
        Button borrowButton = view.findViewById(R.id.borrow_book_scan_button);
        Button returnButton = view.findViewById(R.id.return_book_scan_button);

        // View book button
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        // Add book button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GetBookByISBN.class);
                startActivity(intent);
            }
        });

        // Borrow book in agreed upon location button
        borrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AcceptedRequestsActivity.class);
                startActivity(intent);
            }
        });

        // Return book in agreed upon location button
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        // Build the DialogFragment
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Choose scan option")
                .setNegativeButton("Cancel", null)
                .create();

    }
}