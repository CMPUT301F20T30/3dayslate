package com.jensen.demo.a3dayslate;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class BookClassTests {
    private ArrayList<String> MockAuthor(){
        ArrayList<String> testAuthor = new ArrayList<String>();
        testAuthor.add("Author name");
        return testAuthor;
    }
    private Book MockBook(){
        ArrayList<String> testAuthor = MockAuthor();
        Book book = new Book("test","9780801417443",testAuthor, "Owner");
        return book;
    }

    @Test
    void TestGetStatus(){
        Book book = MockBook();
        Book.statuses status = book.getCurrentStatus();
        assertEquals(Book.statuses.AVAILABLE,status);
    }

    @Test
    void TestSetStatus(){
        Book book = MockBook();
        book.setCurrentStatus(Book.statuses.BORROWED);
        Book.statuses status = book.getCurrentStatus();
        assertEquals(Book.statuses.BORROWED,status);
    }

    @Test
    void TestGetTitle(){
        Book book = MockBook();
        String title = book.getTitle();
        assertEquals("test",title);
    }

    @Test
    void TestSetTitle(){
        Book book = MockBook();
        book.setTitle("No");
        String title = book.getTitle();
        assertEquals("No",title);
    }

    @Test
    void TestGetISBN(){
        Book book = MockBook();
        String isbn = book.getIsbn();
        assertEquals("9780801417443",isbn);
    }

    @Test
    void TestGetAuthors(){
        Book book = MockBook();
        ArrayList<String> testAuthors = MockAuthor();
        ArrayList<String> authors = book.getAuthors();
        for (int i = 0;i < testAuthors.size(); i++){
            assertEquals(testAuthors.get(i),authors.get(i));
        }
    }

    @Test
    void TestSetAuthors(){
        Book book = MockBook();
        ArrayList<String> authors = new ArrayList<String>();
        ArrayList<String> bookAuthors = book.getAuthors();
        authors.add("New name");
        book.setAuthors(authors);
        for (int i = 0;i < bookAuthors.size(); i++){
            assertEquals(authors.get(i),authors.get(i));
        }
    }

    @Test
    void TestGetOwner(){
        Book book = MockBook();
        String owner = book.getOwner();
        assertEquals("Owner",owner);
    }

    @Test
    void TestSetOwner(){
        Book book = MockBook();
        String owner = "new owner";
        book.setOwner(owner);
        assertEquals(owner,book.getOwner());
    }

    @Test
    void TestGetBorrower(){
        Book book = MockBook();
        String borrower = book.getBorrower();
        assertEquals("",borrower);
    }

    @Test
    void TestSetBorrower(){
        Book book = MockBook();
        String borrower = "borrower";
        book.setBorrower(borrower);
        assertEquals(borrower,book.getBorrower());
    }

}
