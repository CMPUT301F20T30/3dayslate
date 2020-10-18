package com.jensen.demo.a3dayslate;

import android.media.Image;

import com.google.firebase.firestore.auth.User;

import java.lang.reflect.Array;
import java.util.ArrayList;

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
    protected enum statuses{
        AVAILABLE,
        REQUESTED,
        ACCEPTED,
        BORROWED
    }
    //on creation will always be considered available
    private statuses currentStatus = statuses.AVAILABLE;
    private String title;
    private String isbn;
    private ArrayList<String> authorList;
    //NOTE THAT THIS IS SUBJECT TO CHANGE, I believe we may use the class Drawable to show images in res files or an ImageView
    private Image image;
    private String owner;
    //on creation will always not be considered having a borrower
    private User borrower = null;



    public Book(String title, String isbn, ArrayList<String> authorList, String owner) {
        this.title = title;
        this.isbn = isbn;
        this.authorList = authorList;
        this.owner = owner;
    }

    /* gets the aspects of an instance of a Book object

    @return value of a aspect of gear

     */

    /**
     * Gets the current status of the book
     * @return
     * Return the book's status as an enum
     */

    public statuses getCurrentStatus() {
        return currentStatus;
    }

    /**
     * This sets the current status of the book
     * @param currentStatus
     * This is the status to be set
     */

    public void setCurrentStatus(statuses currentStatus) {
        this.currentStatus = currentStatus;
    }


    /**
     * Gets the title of the book
     * @return
     * Return the book's title as a string
     */

    public String getTitle() {
        return title;
    }

    /**
     * This sets the title of the book
     * @param title
     * This is the title to be set
     */

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the ISBN of the book
     * @return
     * Return the book's ISBN code as a string
     */

    public String getIsbn() {
        return isbn;
    }

    /**
     * Gets the Authors of the book
     * @return
     * Return the book's author(s) as a String ArrayList
     */

    public ArrayList<String> getAuthors() {
        return authorList;
    }

    /**
     * This sets the authors of the book
     * @param authors
     * This is the ArrayList of authors to set
     */

    public void setAuthors(ArrayList<String> authors) {
        for(int i = 0; i < authors.size(); i++) {
            authorList.set(i, authors.get(i));
        }
    }

    /**
     * Gets the Image of the book
     * @return
     * Return the book's image as an Image object
     */

    public Image getImage() {
        return image;
    }

    /**
     * This sets the image of the book
     * @param image
     * This is the Image to set
     */

    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Gets the owner of the book
     * @return
     * Return the book's owner as a string containing their username
     */

    public String getOwner() {
        return owner;
    }

    /**
     * This sets the owner of the book
     * @param owner
     * This is the owner's username to set
     */

    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Gets the current borrower of the book
     * @return
     * Return the book's borrower as a string containing their username, if there is no borrower, returns null
     */

    public User getBorrower() {
        return borrower;
    }

    /**
     * This sets the current borrower of the book
     * @param borrower
     * This is the borrower's username to set
     */

    public void setBorrower(User borrower) {
        this.borrower = borrower;
    }
}
