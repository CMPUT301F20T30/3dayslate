package com.jensen.demo.a3dayslate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class EditProfileDialog extends AppCompatDialogFragment {
    /* EditProfileDialog

   Version 1.0.0

   November 3 2020

   Copyright [2020] [Anita Ferenc]
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
      A dialog that gets input from the user to change their contact information
      and saves changes information to the database

      @author: Anita Ferenc
      @see: Rewrite for .java classes that use it
      @version:1.0.0
    */

    //declare xml elements
    private TextView username;
    private TextView email;
    private TextView phone;
    private Button editProfile;
    private ProfileDialogListener listener;
    String editEmail;
    String editPhoneNum;
    Boolean usernameTaken = false;
    Context context;

    /**
     * Gets the context of the activity that calls method
     * @param context
     */
    public void setContext(Context context){
        // gets the context of the View Profile Activity
        this.context = context;
    }

    /**
     * opens dialog to edit current user information
     * @param savedInstanceState
     * @return
     * returns a dialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // creates the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // links to edit_profile_dialog resource file
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_profile_dialog,null);

        // set the ids
        email = view.findViewById(R.id.view_profile_edit_email);
        phone = view.findViewById(R.id.view_profile_phone);
        editProfile = view.findViewById(R.id.edit_profile_button);

        // get current user signed in
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseAuth uAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = uAuth.getCurrentUser();

        // set the email text field to current users email
        email.setText(firebaseUser.getEmail());

        return builder
                .setView(view)
                .setTitle("Editing Profile")
                .setMessage("Change the fields you want")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // get the user input from the text fields
                        editEmail = email.getText().toString();
                        editPhoneNum = phone.getText().toString();

                        // update email if not empty line, else set it to previous email
                        if (editEmail.length() > 0) {

                            firebaseUser.updateEmail(email.getText().toString())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("Email update:","Successful");
                                            Toast.makeText(context, "Successfully changed email", Toast.LENGTH_SHORT ).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            editEmail = firebaseUser.getEmail();
                                            Log.d("Email update:","Unsuccessful");
                                            Log.d("Unsuccessful:", editEmail);
                                            Toast.makeText(context, "Invalid email or taken email", Toast.LENGTH_SHORT ).show();
                                        }
                                    });

                            // update the database
                            User newEmail = new User(firebaseUser.getDisplayName(),editEmail);
                            db.collection("users").document(firebaseUser.getDisplayName()).set(newEmail);
                        } else {
                            Toast.makeText(context, "Empty email field", Toast.LENGTH_SHORT ).show();
                            editEmail = firebaseUser.getEmail();
                        }

                        // wait for the database to have time to update information
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // set previous activity fields with new updated fields information
                        listener.applyTexts(firebaseUser.getEmail(),editPhoneNum);
                    }
                }).create();
    }

    /**
     * sets the listener for the dialog
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            // checks if listener implement ProfileDialogListener
            listener = (ProfileDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement ProfileDialogListener");
        }
    }

    /**
     * Interface to send information back to ViewProfileActivity
     */
    public interface ProfileDialogListener {
        // sets text of previous activity with updated information
        void applyTexts(String email, String phoneNumber);
    }
}
