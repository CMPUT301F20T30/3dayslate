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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class EditProfileDialog extends AppCompatDialogFragment {
    /*
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
    String editUsername;
    String editEmail;
    String editPhoneNum;
    Boolean usernameTaken = false;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // creates the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // links to edit_profile_dialog resource file
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_profile_dialog,null);

        // set the ids
        username = view.findViewById(R.id.view_profile_username);
        email = view.findViewById(R.id.view_profile_email);
        phone = view.findViewById(R.id.view_profile_phone);
        editProfile = view.findViewById(R.id.edit_profile_button);


        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseAuth uAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = uAuth.getCurrentUser();

        // set the fields with the previous information
       /* User currentUser = new User();
        FirebaseUser firebaseUser = currentUser.getCurrentUser();*/

        if (firebaseUser != null) {
            username.setText(firebaseUser.getDisplayName());
            email.setText(firebaseUser.getEmail());
        } else {
            Log.d("NULL USER","EDIT DIALOG");
        }

        return builder
                .setView(view)
                .setTitle("Editing Profile")
                .setMessage("Change the fields you want")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // get the user input from the text fields
                        editUsername = username.getText().toString();
                        editEmail = email.getText().toString();
                        editPhoneNum = phone.getText().toString();

                        if (editUsername.length() > 0) {

                            // check if the username already exits
                            final DocumentReference documentReference = db.collection("users").document(editUsername);
                            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        // username is already taken
                                        Log.d("TAG", "Username is taken!");
                                        usernameTaken = true;
                                        editUsername = firebaseUser.getDisplayName();

                                    } else {
                                        // username is not taken
                                        // update username in database

                                        // Resources used: https://firebase.google.com/docs/auth/android/manage-users#update_a_users_profile
                                        // Date accessed: October 21, 2020
                                        // Date written: October 16, 2020
                                        // License: Apache 2.0 License
                                        usernameTaken = false;
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(editUsername)
                                                .build();
                                        firebaseUser.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Log.i("Username update", "Success");
                                                    }
                                                });
                                    }
                                }
                            });
                        } else {
                            // empty space for username, set to previous username
                            editUsername = firebaseUser.getDisplayName();
                        }

                        // update email if not empty line, else set it to previous email
                        if (editEmail.length() > 0) {
                            firebaseUser.updateEmail(email.getText().toString());
                            Log.d("Email update:","Successful");
                        } else {
                            editEmail = firebaseUser.getEmail();
                            Log.d("Email set to: ",firebaseUser.getEmail());
                        }

                        // set previous activity fields with new updated fields information
                        Log.d("APPLYING TEXTS","Applied");
                        listener.applyTexts(editUsername,editEmail,editPhoneNum, usernameTaken);
                    }
                }).create();


    }

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

    public interface ProfileDialogListener {
        // sets text of previous activity with updated information
        void applyTexts(String username, String email, String phoneNumber, boolean usernameTaken);
    }
}
