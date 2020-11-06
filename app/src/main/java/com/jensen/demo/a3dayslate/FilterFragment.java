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

/** Fragment for choosing how to filter books
 * in owned books
 * filters by book status:
 *  AVAILABLE
 *  REQUESTED
 *  ACCEPTED
 *  BORROWED
 *  NO FILTER
 * @author Eric Weber
 */
public class FilterFragment extends DialogFragment {

    View view;
    private OnFilterFragmentInteraction interaction;

    /**
     * Interface for handling result when button clicked
     */
    public interface OnFilterFragmentInteraction {
        void onFilterFragmentButtonClicked(int filterMode);
    }

    /** Attaches fragment to activity
     *
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnFilterFragmentInteraction){
            interaction = (OnFilterFragmentInteraction) context;
        }else{
            throw new RuntimeException(context.toString()
                    + " Must implement OnFilterFragmentInteraction");
        }
    }


    /** Sets up buttons in filter
     *
     * @param savedInstanceState
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        view = LayoutInflater.from(getActivity()).inflate(R.layout.filter_fragment, null);

        //declare buttons
        Button available = view.findViewById(R.id.filter_available);
        Button requested = view.findViewById(R.id.filter_requested);
        Button accepted = view.findViewById(R.id.filter_accepted);
        Button borrowed = view.findViewById(R.id.filter_borrowed);
        Button none = view.findViewById(R.id.filter_none);

        available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interaction.onFilterFragmentButtonClicked(1);
            }
        });

        requested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interaction.onFilterFragmentButtonClicked(2);
            }
        });

        accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interaction.onFilterFragmentButtonClicked(3);
            }
        });

        borrowed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interaction.onFilterFragmentButtonClicked(4);
            }
        });

        none.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interaction.onFilterFragmentButtonClicked(0);
            }
        });

        //make an alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Filter by: ")
                .setNegativeButton("Return", null)
                .create();

    }


}
