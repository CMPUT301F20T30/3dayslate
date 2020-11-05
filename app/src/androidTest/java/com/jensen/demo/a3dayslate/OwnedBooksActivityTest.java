package com.jensen.demo.a3dayslate;

import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class or the owned books activity
 */
public class OwnedBooksActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws  Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     *  Tests that the activity can be gotten to through dashboard
     */
    @Test
    public void checkActivity(){
        solo.assertCurrentActivity("WRONG ACTIVITY", MainActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@ownedbooks.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "testownedbooks");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class);
        solo.assertCurrentActivity("NOT DASHBOARD", DashboardActivity.class);
        solo.clickOnButton("My Books");
        solo.waitForActivity(OwnedBooksActivity.class);
        solo.assertCurrentActivity("NOT OWNED BOOKS", OwnedBooksActivity.class);
    }

    /**
     *  Tests that book(s) are being displayed
     */
    @Test
    public void checkList(){
        // get to owned books
        solo.assertCurrentActivity("WRONG ACTIVITY", MainActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@ownedbooks.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "testownedbooks");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class);
        solo.assertCurrentActivity("NOT DASHBOARD", DashboardActivity.class);
        solo.clickOnButton("My Books");
        solo.waitForActivity(OwnedBooksActivity.class);
        solo.assertCurrentActivity("NOT OWNED BOOKS", OwnedBooksActivity.class);

        //check proper book is at activity
        OwnedBooksActivity activity = (OwnedBooksActivity) solo.getCurrentActivity();
        final ListView ownedBooksList = activity.ownedBooksList;
        solo.waitForText("AVAILABLE",1,1000);
        Book book = (Book) ownedBooksList.getAdapter().getItem(0);
        String title = book.getTitle();
        assertEquals("TESTBOOKOB1", title);
    }

    /**
     * Checks that clicking the add book button takes the user to the proper activity
     */
    @Test
    public void checkAddBook(){
        // get to owned books
        solo.assertCurrentActivity("WRONG ACTIVITY", MainActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@ownedbooks.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "testownedbooks");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class);
        solo.assertCurrentActivity("NOT DASHBOARD", DashboardActivity.class);
        solo.clickOnButton("My Books");
        solo.waitForActivity(OwnedBooksActivity.class);
        solo.assertCurrentActivity("NOT OWNED BOOKS", OwnedBooksActivity.class);
        solo.clickOnButton("add");
        solo.waitForActivity(GetBookByISBN.class);
        solo.assertCurrentActivity("NOT GET BOOK BY ISBN", GetBookByISBN.class);
    }

    /**
     *  Tests that clicking a book and then clicking edit takes user to proper activity
     */
    @Test
    public void checkEditBook(){
        // get to owned books
        solo.assertCurrentActivity("WRONG ACTIVITY", MainActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@ownedbooks.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "testownedbooks");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class);
        solo.assertCurrentActivity("NOT DASHBOARD", DashboardActivity.class);
        solo.clickOnButton("My Books");
        solo.waitForActivity(OwnedBooksActivity.class);
        solo.assertCurrentActivity("NOT OWNED BOOKS", OwnedBooksActivity.class);
        //test that book needs to be clicked to get to edit book
        solo.clickOnButton("edit");
        solo.waitForActivity(EditBookActivity.class,1000);
        solo.assertCurrentActivity("EDIT GONE TO WITH NO BOOK",OwnedBooksActivity.class);
        //test that edit button goes to activity when book selected
        solo.waitForText("TESTBOOKOB1");
        //click on first book in list
        solo.clickInList(1);
        solo.clickOnButton("edit");
        solo.waitForActivity(EditBookActivity.class);
        solo.assertCurrentActivity("NOT EDIT BOOK ACTIVITY", EditBookActivity.class);
    }

    /**
     *  Test for the filtering by status
     */
    @Test
    public void checkFilter(){
        // get to owned books
        solo.assertCurrentActivity("WRONG ACTIVITY", MainActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@ownedbooks.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "testownedbooks");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class);
        solo.assertCurrentActivity("NOT DASHBOARD", DashboardActivity.class);
        solo.clickOnButton("My Books");
        solo.waitForActivity(OwnedBooksActivity.class);
        solo.assertCurrentActivity("NOT OWNED BOOKS", OwnedBooksActivity.class);
        solo.waitForText("TESTBOOKOB");

        OwnedBooksActivity activity = (OwnedBooksActivity) solo.getCurrentActivity();
        final ListView ownedBooksList = activity.ownedBooksList;
        Book book = (Book) ownedBooksList.getAdapter().getItem(4);
        String title = book.getTitle();
        //test that all books are being displayed (list has 5 books)
        assertEquals("TESTBOOKOB5", title);

        // test available filter works
        // second book will be TESTBOOKOB5 in this case
        solo.clickOnButton("filter");
        solo.sleep(250);
        solo.clickOnButton("AVAILABLE");
        solo.clickOnButton("Return");
        book = (Book) ownedBooksList.getAdapter().getItem(1);
        title = book.getTitle();
        assertEquals("TESTBOOKOB5", title);

        //test requested filter works
        solo.clickOnButton("filter");
        solo.sleep(250);
        solo.clickOnButton("REQUESTED");
        solo.clickOnButton("Return");
        book = (Book) ownedBooksList.getAdapter().getItem(0);
        title = book.getTitle();
        assertEquals("TESTBOOKOB2", title);

        //test accepted filter works
        solo.clickOnButton("filter");
        solo.sleep(250);
        solo.clickOnButton("ACCEPTED");
        solo.clickOnButton("Return");
        book = (Book) ownedBooksList.getAdapter().getItem(0);
        title = book.getTitle();
        assertEquals("TESTBOOKOB3", title);

        //test requested filter works
        solo.clickOnButton("filter");
        solo.sleep(250);
        solo.clickOnButton("BORROWED");
        solo.clickOnButton("Return");
        book = (Book) ownedBooksList.getAdapter().getItem(0);
        title = book.getTitle();
        assertEquals("TESTBOOKOB4", title);

        //test all filter works
        solo.clickOnButton("filter");
        solo.sleep(250);
        solo.clickOnButton("ALL");
        solo.clickOnButton("Return");
        book = (Book) ownedBooksList.getAdapter().getItem(4);
        title = book.getTitle();
        assertEquals("TESTBOOKOB5", title);
    }

    @Test
    public void checkDeleteEdit(){
        // get to owned books
        solo.assertCurrentActivity("WRONG ACTIVITY", MainActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@ownedbooks.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "testownedbooks");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class);
        solo.assertCurrentActivity("NOT DASHBOARD", DashboardActivity.class);
        solo.clickOnButton("My Books");
        solo.waitForActivity(OwnedBooksActivity.class);
        solo.assertCurrentActivity("NOT OWNED BOOKS", OwnedBooksActivity.class);
        solo.waitForText("TESTBOOKOB");

        //check that delete doesn't do anything when no book is selected
        OwnedBooksActivity activity = (OwnedBooksActivity) solo.getCurrentActivity();
        solo.clickOnButton("delete");
        ListView ownedBooksList = activity.ownedBooksList;
        Book book = (Book) ownedBooksList.getAdapter().getItem(4);
        String title = book.getTitle();
        //test that all books are being displayed (no books have been deleted)
        //assertEquals("TESTBOOKOB5", title);
        //test that books are being deleted
        solo.clickInList(5);
        solo.clickOnButton("delete");
        try{
            book = (Book) ownedBooksList.getAdapter().getItem(4);
            title = book.getTitle();
            Assert.fail("BOOK NOT DELETED");
        }catch (Exception e){
           assertEquals("a", "a", "a");
        }

        //add the book back
        solo.clickOnButton("add");
        solo.waitForActivity(GetBookByISBN.class);
        solo.enterText((EditText)solo.getView(R.id.enterISBNCode), "9781787125933");
        solo.clickOnButton("Enter ISBN Code");
        solo.sendKey(KeyEvent.KEYCODE_BACK);
        solo.hideSoftKeyboard();
        solo.goBack();
        // owned books activity uses recreate() which is why its needs to be waited for twice in this case
        solo.waitForActivity(DashboardActivity.class);
        //edit the book to have the proper title and test
        solo.clickOnButton("My Books");
        solo.waitForActivity(OwnedBooksActivity.class);
        solo.assertCurrentActivity("NOT OWNED BOOKS", OwnedBooksActivity.class);
        solo.clickInList(5);
        solo.clickOnButton("edit");
        solo.waitForActivity(EditBookActivity.class);
        solo.enterText((EditText)solo.getView(R.id.edit_book_title), "");
        solo.enterText((EditText)solo.getView(R.id.edit_book_title), "TESTBOOKOB5");
        solo.clickOnButton("Confirm Edits");
        solo.waitForActivity(OwnedBooksActivity.class);
        //give activity time to refresh
        solo.sleep(250);
        //needs to be done as this is a different activity than previous
        activity = (OwnedBooksActivity) solo.getCurrentActivity();
        ownedBooksList = activity.ownedBooksList;
        book = (Book) ownedBooksList.getAdapter().getItem(4);
        title = book.getTitle();
        assertEquals("TESTBOOKOB5", title);
    }
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
