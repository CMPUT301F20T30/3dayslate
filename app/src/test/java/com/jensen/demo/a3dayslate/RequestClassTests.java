package com.jensen.demo.a3dayslate;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/* RequestClassTests Class

   Version 1.0.0

   November 3 2020

   Copyright [2020] [Larissa Zhang]

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

/**Unit testing for the Request class
 */

public class RequestClassTests {

    /**Creates a mock arraylist of authors
     *
     * @return testAuthor
     */

    private ArrayList<String> MockAuthor(){
        ArrayList<String> testAuthor = new ArrayList<String>();
        testAuthor.add("Author name");
        return testAuthor;
    }

    /**returns a mock book
     *
     * @return book
     */

    private Book MockBook(){
        ArrayList<String> testAuthor = MockAuthor();
        Book book = new Book("test","9780801417443",testAuthor, "Owner");
        book.setBorrower("Borrower");
        return book;
    }

    /**returns a mock request
     *
     * @return book
     */

    private Request MockRequest(){
        Request request = new Request("Requester", MockBook());
        return request;
    }

    /**Tests getting a book from a
     * MockRequest object
     */

    @Test
    public void TestGetBook(){
        Book book = MockBook();
        Request request = MockRequest();
        Book reqBook = request.getBook();
        assertEquals(reqBook.getCurrentStatus(),book.getCurrentStatus());
        assertEquals(reqBook.getAuthors(),book.getAuthors());
        assertEquals(reqBook.getOwner(),book.getOwner());
        assertEquals(reqBook.getBorrower(),book.getBorrower());
        assertEquals(reqBook.getTitle(),book.getTitle());
        assertEquals(reqBook.getIsbn(),book.getIsbn());
    }

    /**Tests setting a book in a
     * MockRequest object
     */

    @Test
    public void TestSetBook(){
        Request request = MockRequest();
        ArrayList<String> testAuthor = MockAuthor();
        Book newBook = new Book("test","1234567890123",testAuthor, "Not Owner");
        request.setBook(newBook);
        assertEquals(request.getBook(),newBook);
    }

    /**Tests getting a book from a
     * MockRequest object
     */

    @Test
    public void TestGetOwner(){
        Book book = MockBook();
        Request request = MockRequest();
        assertEquals(request.getOwner(),book.getOwner());
    }

    /**Tests getting a book from a
     * MockRequest object
     */

    @Test
    public void TestGetStatus(){
        Book book = MockBook();
        Request request = MockRequest();
        assertEquals(request.getStatus(),book.getCurrentStatus());
        assertEquals(request.getBook().getCurrentStatus(),book.getCurrentStatus());
    }

    /**Tests getting a book from a
     * MockRequest object
     */

    @Test
    public void TestGetRequester(){
        Book book = MockBook();
        Request request = MockRequest();
        assertEquals(request.getRequester(),"Requester");
    }

    /**Tests setting a status in a
     * MockRequest object
     */

    @Test
    public void TestSetStatus(){
        Request request = MockRequest();
        request.setStatus(Book.statuses.ACCEPTED);
        request.getBook().setCurrentStatus(Book.statuses.ACCEPTED);
        assertEquals(request.getStatus(), Book.statuses.ACCEPTED);
        assertEquals(request.getBook().getCurrentStatus(), Book.statuses.ACCEPTED);
    }

    /**Tests getting and setting a location from a
     * MockRequest object
     */

    @Test
    public void TestGetSetLocation(){
        double latitude = 53.5461;
        double longitude = 113.4938;
        ExchangeLocation location = new ExchangeLocation(latitude,longitude);
        Request request  = MockRequest();
        request.setLocation(location);
        assertEquals(request.getLocation(),location);
    }
}
