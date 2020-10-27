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

                        editEmail = email.getText().toString();
                        editPhoneNum = phone.getText().toString();

                        // update email if not empty line, else set it to previous email
                        if (editEmail.length() > 0) {
                            firebaseUser.updateEmail(email.getText().toString());
                            Log.d("Email update:","Successful");
                            User newEmail = new User(firebaseUser.getDisplayName(),editEmail);
                            db.collection("users").document(firebaseUser.getDisplayName()).set(newEmail);
                        } else {
                            editEmail = firebaseUser.getEmail();
                            Log.d("Email set to: ",firebaseUser.getEmail());
                        }

                        // set previous activity fields with new updated fields information
                        Log.d("APPLYING TEXTS","Applied");
                        listener.applyTexts(editEmail,editPhoneNum, usernameTaken);
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
        void applyTexts(String email, String phoneNumber, boolean usernameTaken);
    }
}
