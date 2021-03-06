package com.jensen.demo.a3dayslate;

import java.io.Serializable;

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
 * Implements the User class which will be user in the app to create a user with
 * the users email and username.
 * Has getter for email and username and setter for email.
 * @author Anita Ferenc
 * @version 1.0.0
 */
public class User implements Serializable {

    private String username;
    private String email;

    /**
     * Empty constructor for User
     */

    public User(){
        //DON'T DELETE THIS
        // Needed to be able to do methods such as toObject() from the database
        // and to put objects directly into the database
    };

    /**
     * Creates a user object with username and email parameters
     * @param username
     * @param email
     */
    public User(String username, String email) {
        // gets username and email and sets it to username and email variables
        this.username = username;
        this.email = email;
    }

    /**
     * Returns the set username
     * @return
     * Returns username as a string
     */
    public String getUsername() {
        // returns username
        return username;
    }

    /**
     * returns the set emails
     * @return
     * returns email as a string
     */
    public String getEmail() {
        // returns email
        return email;
    }

    /**
     * sets the email for the user
     * @param email
     * gets email input as a string
     */
    public void setEmail(String email) {
        // gets email input and sets the email
        this.email = email;
    }
}

