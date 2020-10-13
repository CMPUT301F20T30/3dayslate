package com.jensen.demo.a3dayslate;

import android.media.Image;

import com.google.firebase.firestore.auth.User;

/* Implements the Book class which will be used in the app to
   hold information regarding a specific book object. Has a partially filled constructor for the
   first instantiation of a book.
   Has both getter and setter methods for all the aspects.

   Enums are for the status of each book as they will always
   remain constant. The impression is that we will use getter
   and setter methods to modify aspects after creation.

   @author: Larissa Zhang
   @see: Rewrite for .java classes that use it
   @version:1.0.0

 */

public class Book {
    //enums for statuses
    private enum statuses{
        AVAILABLE,
        REQUESTED,
        ACCEPTED,
        BORROWED
    }
    //on creation will always be considered available
    private statuses currentStatus = statuses.AVAILABLE;
    private String title;
    private String isbn;
    private String author;
    //NOTE THAT THIS IS SUBJECT TO CHANGE, I believe we may use the class Drawable to show images in res files or an ImageView
    private Image image;
    private User owner;
    //on creation will always not be considered having a borrower
    private User borrower = null;

    /* Creates an instance of Book item

    @params title, isbn, author, owner

     */

    public Book(String title, String isbn, String author, User owner) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.owner = owner;
    }

    /* gets the aspects of an instance of a Book object

    @return value of a aspect of gear

     */

    public statuses getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(statuses currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getBorrower() {
        return borrower;
    }

    public void setBorrower(User borrower) {
        this.borrower = borrower;
    }
}
