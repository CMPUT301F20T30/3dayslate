package com.jensen.demo.a3dayslate;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class BookSearchActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<BookSearchActivity> rule =
            new ActivityTestRule<>(BookSearchActivity.class,true,true);

    /**
     * Runs before all tests and creates solo instance
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    /**
     * This test verifies the search button is functional
     */
    @Test
    public void checkSearchButton(){
        solo.assertCurrentActivity("Wrong Activity", BookSearchActivity.class);

        BookSearchActivity activity = (BookSearchActivity) solo.getCurrentActivity();
        final ListView bookList = activity.bookList; //Get the listview

        assertEquals(0, bookList.getAdapter().getCount());

        solo.clickOnButton("search");
        solo.waitForText("SEARCH", 1, 2000);

        assertNotEquals(0, bookList.getAdapter().getCount());

    }

    /**
     * This test verifies the search bar can accept input
     */

    @Test
    public void checkSearchBar() {
        solo.assertCurrentActivity("Wrong Activity", BookSearchActivity.class);

        BookSearchActivity activity = (BookSearchActivity) solo.getCurrentActivity();
        final ListView bookList = activity.bookList; //Get the listview

        solo.enterText((EditText) solo.getView(R.id.book_search_bar), "hello");
        solo.waitForText("hello", 1, 2000);
        assertEquals("hello", ((EditText) solo.getView(R.id.book_search_bar)).getText().toString());
    }

    /**
     * This test verifies that a search produces only correct results
     * The search query must match either Title, Authors, or ISBN
     */
    @Test
    public void checkSearchIsCorrect(){
        solo.assertCurrentActivity("Wrong Activity", BookSearchActivity.class);

        BookSearchActivity activity = (BookSearchActivity) solo.getCurrentActivity();
        final ListView bookList = activity.bookList; //Get the listview

        String text = "wing";//pick any desired string before running test

        solo.enterText((EditText) solo.getView(R.id.book_search_bar), text);
        solo.clickOnButton("search");
        solo.waitForText("SEARCH", 1, 2000);

        for (int i=0; i<bookList.getAdapter().getCount(); i++){
            Book book = (Book) bookList.getItemAtPosition(i);

            String bookInfo = book.getIsbn() + book.getTitle();
            for (int j=0; j < book.getAuthors().size(); j++){
                bookInfo = bookInfo + book.getAuthors().get(j);
            }

            assertTrue(bookInfo.toLowerCase().contains(text.toLowerCase()));
        }
    }

    /**
     * This test verifies that an activity switch is performed to View a book
     * after selecting a book and then clicking the View button, and the activity
     * successfully switches back with the back button press
     */
    @Test
    public void checkViewActivitySwitch(){
        solo.assertCurrentActivity("Wrong Activity", BookSearchActivity.class);

        BookSearchActivity activity = (BookSearchActivity) solo.getCurrentActivity();
        final ListView bookList = activity.bookList; //Get the listview

        solo.clickOnButton("search");
        solo.waitForText("SEARCH", 1, 2000);
        assertNotEquals(0, bookList.getAdapter().getCount());//ensure there is a book to click

        solo.scrollToTop();//ensures that first book can be clicked rather than search bar
        solo.clickInList(1);

        solo.clickOnButton("View");

        solo.assertCurrentActivity("Wrong Activity", ViewBookActivity.class);

        solo.goBack();

        solo.assertCurrentActivity("Wrong Activity", BookSearchActivity.class);

    }

    /**
     * This test verifies that the ViewBook activity displays the correct book
     * as selected from the list view and all fields are populated with
     * the correct information
     */

    @Test
    public void checkViewActivityBook(){
        solo.assertCurrentActivity("Wrong Activity", BookSearchActivity.class);

        BookSearchActivity activity = (BookSearchActivity) solo.getCurrentActivity();
        final ListView bookList = activity.bookList; //Get the listview

        solo.clickOnButton("search");
        solo.waitForText("SEARCH", 1, 2000);
        assertNotEquals(0, bookList.getAdapter().getCount());//ensure there is a book to click

        solo.scrollToTop();//ensures that first book can be clicked rather than search bar
        Book book = (Book) activity.bookList.getItemAtPosition(0);
        solo.clickInList(1);

        solo.clickOnButton("View");

        solo.assertCurrentActivity("Wrong Activity", ViewBookActivity.class);

        assertEquals(book.getTitle(), ((TextView) solo.getView(R.id.view_book_title)).getText().toString());
        assertEquals(book.getAuthors().toString().replaceAll("\\[|\\]", ""),
                ((TextView) solo.getView(R.id.view_book_author)).getText().toString());
        assertEquals(book.getIsbn(), ((TextView) solo.getView(R.id.view_book_isbn)).getText().toString());
        assertEquals("Owner: " + book.getOwner(), ((TextView) solo.getView(R.id.view_book_owner)).getText().toString());

    }

    /**
     * Finishes any open activities after testing
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
