package com.jensen.demo.a3dayslate;

/* Request class

   Version 1.0.0

   November 5 2020

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
 * Implements the Request class which holds a book, owner, requester, location, and status
 * Has both getter and setter methods for all the aspects.
 * @author Jensen Khemchandani
 * @see IncomingRequestsActivity
 * @see OutgoingRequestsActivity
 * @see LocationActivity
 * @see OutgoingRequestCustomList
 * @see IncomingRequestCustomList
 * @see IncomingRequestsBooks
 * @see ReviewLocationActivity
 * @version 1.0.0
 */

public class Request {
    private String owner;
    private String requester;
    private Book book;
    private Book.statuses status;
    private ExchangeLocation location;

    /**
     * Constructor method
     * @param requester
     * a requester's username
     * @param book
     * A Book object that the request is for
     */

    public Request(String requester, Book book) {
        this.book = book;
        this.owner = book.getOwner();
        this.requester = requester;
        this.status = book.getCurrentStatus();
    }

    /**
     * Empty constructor for Requests
     */

    public Request() {
        // Do NOT delete this
        // Needed to be able to do methods such as toObject() from the database
        // and to put objects directly into the database
    }

    /**
     * Gets the owner of the book
     * @return
     * Return the owner's username as a String
     */

    public String getOwner() {
        return owner;
    }

    /**
     * Gets the requester of the book
     * @return
     * Return the requester's username as a String
     */

    public String getRequester() {
        return requester;
    }

    /**
     * Gets the Book in the request
     * @return
     * Return the book as a Book object
     */

    public Book getBook() {
        return book;
    }

    /**
     * This sets the Book of the request
     * @param book
     * This is the Book object to set
     */

    public void setBook(Book book) {
        this.book = book;
    }

    /**
     * This gets the status of the request
     * @return
     * Return the status as a Book.statuses enum value
     */

    public Book.statuses getStatus() {
        return status;
    }

    /**
     * This sets the status of the request
     * @param status
     * This is the Books.statuses value to set
     */

    public void setStatus(Book.statuses status) {
        this.status = status;
    }

    /**
     * Gets the Location from the request
     * @return
     * Return the location as an ExchangeLocation object
     */

    public ExchangeLocation getLocation() {
        return location;
    }

    /**
     * This sets the location of the request
     * @param location
     * This is the ExchangeLocation object to set
     */


    public void setLocation(ExchangeLocation location) {
        this.location = location;
    }
}
