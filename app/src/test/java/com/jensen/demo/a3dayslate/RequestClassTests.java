package com.jensen.demo.a3dayslate;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class RequestClassTests {
    private ArrayList<String> MockAuthor(){
        ArrayList<String> testAuthor = new ArrayList<String>();
        testAuthor.add("Author name");
        return testAuthor;
    }
    private Book MockBook(){
        ArrayList<String> testAuthor = MockAuthor();
        Book book = new Book("test","9780801417443",testAuthor, "Owner");
        book.setBorrower("Borrower");
        return book;
    }

    private Request MockRequest(){
        Request request = new Request("Requester", MockBook());
        return request;
    }

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

    @Test
    public void TestSetBook(){
        Request request = MockRequest();
        ArrayList<String> testAuthor = MockAuthor();
        Book newBook = new Book("test","1234567890123",testAuthor, "Not Owner");
        request.setBook(newBook);
        assertEquals(request.getBook(),newBook);
    }

    @Test
    public void TestGetOwner(){
        Book book = MockBook();
        Request request = MockRequest();
        assertEquals(request.getOwner(),book.getOwner());
    }

    @Test
    public void TestGetStatus(){
        Book book = MockBook();
        Request request = MockRequest();
        assertEquals(request.getStatus(),book.getCurrentStatus());
        assertEquals(request.getBook().getCurrentStatus(),book.getCurrentStatus());
    }

    @Test
    public void TestGetRequester(){
        Book book = MockBook();
        Request request = MockRequest();
        assertEquals(request.getRequester(),"Requester");
    }

    @Test
    public void TestSetStatus(){
        Request request = MockRequest();
        request.setStatus(Book.statuses.ACCEPTED);
        request.getBook().setCurrentStatus(Book.statuses.ACCEPTED);
        assertEquals(request.getStatus(), Book.statuses.ACCEPTED);
        assertEquals(request.getBook().getCurrentStatus(), Book.statuses.ACCEPTED);
    }

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
