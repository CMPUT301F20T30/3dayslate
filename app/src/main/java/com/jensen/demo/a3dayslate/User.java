package com.jensen.demo.a3dayslate;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.lang.ref.Reference;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/*
 * User Class
 *
 * Version 1.0.0
 *
 * October 19,2020
 *
 *Copyright [2020] [Anita Ferenc]
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

/**
 * Creates a user object with the user's username and email
 */
public class User implements Serializable {

    private String username;
    private String email;

    public User(){
        //DON'T DELETE THIS
        // Needed to be able to do methods such as toObject() from the database
        // and to put objects directly into the database
    };

    public User(String username, String email) {
        // gets username and email and sets it to username and email variables
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        // returns username
        return username;
    }

    public String getEmail() {
        // returns email
        return email;
    }

    public void setEmail(String email) {
        // gets email input and sets the email
        this.email = email;
    }
}

