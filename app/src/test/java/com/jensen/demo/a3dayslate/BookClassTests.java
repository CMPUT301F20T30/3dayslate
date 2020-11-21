package com.jensen.demo.a3dayslate;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/* BookClassTests Class

   Version 1.0.0

   November 2 2020

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

/**Unit testing for the Books class
 *
 */

public class BookClassTests {
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
        return book;
    }

    /**Tests getting a status from a
     * MockBook object
     */

    @Test
    public void TestGetStatus(){
        Book book = MockBook();
        Book.statuses status = book.getCurrentStatus();
        assertEquals(Book.statuses.AVAILABLE,status);
    }

    /**Tests setting a status in a
     * MockBook object
     */

    @Test
    public void TestSetStatus(){
        Book book = MockBook();
        book.setCurrentStatus(Book.statuses.BORROWED);
        Book.statuses status = book.getCurrentStatus();
        assertEquals(Book.statuses.BORROWED,status);
    }

    /**Tests getting a title from a
     * MockBook object
     */

    @Test
    public void TestGetTitle(){
        Book book = MockBook();
        String title = book.getTitle();
        assertEquals("test",title);
    }

    /**Tests setting a title in a
     * MockBook object
     */

    @Test
    public void TestSetTitle(){
        Book book = MockBook();
        book.setTitle("No");
        String title = book.getTitle();
        assertEquals("No",title);
    }

    /**Tests getting a isbn from a
     * MockBook object
     */

    @Test
    public void TestGetISBN(){
        Book book = MockBook();
        String isbn = book.getIsbn();
        assertEquals("9780801417443",isbn);
    }

    /**Tests getting a list of authors from a
     * MockBook object
     */

    @Test
    public void TestGetAuthors(){
        Book book = MockBook();
        ArrayList<String> testAuthors = MockAuthor();
        ArrayList<String> authors = book.getAuthors();
        for (int i = 0;i < testAuthors.size(); i++){
            assertEquals(testAuthors.get(i),authors.get(i));
        }
    }

    /**Tests setting a list of authors in a
     * MockBook object
     */

    @Test
    public void TestSetAuthors(){
        Book book = MockBook();
        ArrayList<String> authors = new ArrayList<String>();
        ArrayList<String> bookAuthors = book.getAuthors();
        authors.add("New name");
        book.setAuthors(authors);
        for (int i = 0;i < bookAuthors.size(); i++){
            assertEquals(authors.get(i),authors.get(i));
        }
    }

    /**Tests getting a owner from a
     * MockBook object
     */

    @Test
    public void TestGetOwner(){
        Book book = MockBook();
        String owner = book.getOwner();
        assertEquals("Owner",owner);
    }

    /**Tests setting a owner in a
     * MockBook object
     */

    @Test
    public void TestSetOwner(){
        Book book = MockBook();
        String owner = "new owner";
        book.setOwner(owner);
        assertEquals(owner,book.getOwner());
    }

    /**Tests getting a borrower from a
     * MockBook object
     */

    @Test
    public void TestGetBorrower(){
        Book book = MockBook();
        String borrower = book.getBorrower();
        assertEquals("",borrower);
    }

    /**Tests setting a borrower in a
     * MockBook object
     */

    @Test
    public void TestSetBorrower(){
        Book book = MockBook();
        String borrower = "borrower";
        book.setBorrower(borrower);
        assertEquals(borrower,book.getBorrower());
    }

}
