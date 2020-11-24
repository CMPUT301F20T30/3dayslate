package com.jensen.demo.a3dayslate;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.maps.MapView;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/* IncomingRequestsActivityTest Class

   Version 1.0.1

   October 31 2020

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

/**Testing class that goes through the activty
 *  IncomingRequestsActivityTest using robotium
 *
 */

public class IncomingRequestsActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

    /**Method that runs before all tests and gets solo instance
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    //testing partner (requests the books) is test@buddyforincoming.act and password is 123456
    //book isbns in order 9780743253338 9781484732748 9781894810647

    /**Ensures that all requests made under one book are only for
     * the specific book.
     */

    @Test
    public void checkARequest(){
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@incomingreqsactivity.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "123456");
        solo.clickOnButton("Login");
        //emulator can take forever to login sometimes
        solo.waitForActivity(DashboardActivity.class,20000);
        solo.assertCurrentActivity("Wrong Activity (Dashboard)", DashboardActivity.class);
        solo.clickOnButton("My Requests");
        solo.waitForFragmentByTag("REQUESTS");
        solo.clickOnButton("Incoming Requests");
        solo.waitForDialogToClose(200);
        solo.waitForActivity(IncomingRequestsBooks.class);

        //checks the current books with reqs
        assertTrue(solo.waitForText("Williams-Sonoma Collection: Asian",1,2000));
        //assertTrue(solo.waitForText("The Trials of Apollo Book One The Hidden Oracle",1,2000));
        //assertTrue(solo.waitForText("Complete Englishsmart",1,2000));
        solo.clickInList(1);
        solo.assertCurrentActivity("Did not change",IncomingRequestsActivity.class);
        assertTrue(solo.waitForText("Incoming Requests",1,2000));
        //only one requester
        assertTrue(solo.waitForText("Title: Williams-Sonoma Collection: Asian",1,2000));
        assertTrue(solo.waitForText("Requester: buddytesterincoming",1,2000));
        assertTrue(solo.waitForText("Pending...",1,2000));
        solo.goBack();
    }

    /**Tests that location from mapview was not selected
     * and request status does not change
     */

    @Test
    public void checkLocationAccept(){
        //adds the book to the test owner
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@incomingreqsactivity.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "123456");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class,10000);
        solo.assertCurrentActivity("Wrong Activity", DashboardActivity.class);
        solo.clickOnButton("Scan ISBN");
        solo.clickOnButton("Add book to owned books");
        solo.enterText((EditText)solo.getView(R.id.enterISBNCode), "9781484732748");
        solo.clickOnButton("Enter ISBN Code");
        solo.goBack();
        solo.goBack();
        solo.goBack();

        //requests the book with the test borrower
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@buddyforincoming.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "123456");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class,10000);
        solo.clickOnButton("Search for Books");
        solo.waitForActivity(BookSearchActivity.class);
        solo.enterText((EditText)solo.getView(R.id.book_search_bar),"The Trials of Apollo Book One The Hidden Oracle");
        solo.clickOnButton("search");
        solo.clickInList(1);
        solo.clickOnButton("Request");
        solo.goBack();
        solo.goBack();

        //checks the actual request accepting
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@incomingreqsactivity.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "123456");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class,10000);
        solo.assertCurrentActivity("Wrong Activity (Dashboard)", DashboardActivity.class);
        solo.clickOnButton("My Requests");
        solo.waitForFragmentByTag("REQUESTS");
        solo.clickOnButton("Incoming Requests");
        solo.waitForDialogToClose(200);
        solo.waitForActivity(IncomingRequestsBooks.class);

        //checks the current books with reqs
        assertTrue(solo.waitForText("The Trials of Apollo Book One The Hidden Oracle",1,2000));
        solo.clickInList(2);
        solo.assertCurrentActivity("Did not change",IncomingRequestsActivity.class);
        assertTrue(solo.waitForText("Incoming Requests",1,2000));
        //only one requester
        assertTrue(solo.waitForText("Title: The Trials of Apollo Book One The Hidden Oracle",1,2000));
        assertTrue(solo.waitForText("Requester: buddytesterincoming",1,2000));
        assertTrue(solo.waitForText("Pending...",1,2000));
        //select item and accept req
        solo.clickInList(1);
        solo.clickOnButton("Accept");
        solo.waitForActivity(LocationActivity.class);
        solo.assertCurrentActivity("Did not go to location", LocationActivity.class);
        //do not select location
        solo.clickOnButton("Select Location");
        solo.assertCurrentActivity("No location selected",LocationActivity.class);
        solo.goBack();
        assertTrue(solo.waitForText("Title: The Trials of Apollo Book One The Hidden Oracle",1,2000));
        assertTrue(solo.waitForText("Requester: buddytesterincoming",1,2000));
        assertTrue(solo.waitForText("Pending...",1,2000));
    }

    /**Tests that the owner can accept any requests and can choose
     * a location to fulfill request. Sets up book and also sets up
     * the request to appear with the testing buddy
     */

    //book and requests must be manually deleted in database as books with requests cannot be deleted

    @Test
    public void checkAcceptReq(){
        //adds the book to the test owner
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@incomingreqsactivity.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "123456");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class,10000);
        solo.assertCurrentActivity("Wrong Activity", DashboardActivity.class);
        solo.clickOnButton("Scan ISBN");
        solo.clickOnButton("Add book to owned books");
        solo.enterText((EditText)solo.getView(R.id.enterISBNCode), "9781484732748");
        solo.clickOnButton("Enter ISBN Code");
        solo.goBack();
        solo.goBack();
        solo.goBack();

        //requests the book with the test borrower
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@buddyforincoming.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "123456");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class,10000);
        solo.clickOnButton("Search for Books");
        solo.waitForActivity(BookSearchActivity.class);
        solo.enterText((EditText)solo.getView(R.id.book_search_bar),"The Trials of Apollo Book One The Hidden Oracle");
        solo.clickOnButton("search");
        solo.clickInList(1);
        solo.clickOnButton("Request");
        solo.goBack();
        solo.goBack();

        //checks the actual request accepting
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@incomingreqsactivity.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "123456");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class,10000);
        solo.assertCurrentActivity("Wrong Activity (Dashboard)", DashboardActivity.class);
        solo.clickOnButton("My Requests");
        solo.waitForFragmentByTag("REQUESTS");
        solo.clickOnButton("Incoming Requests");
        solo.waitForDialogToClose(200);
        solo.waitForActivity(IncomingRequestsBooks.class);

        //checks the current books with reqs
        assertTrue(solo.waitForText("The Trials of Apollo Book One The Hidden Oracle",1,2000));
        solo.clickInList(2);
        solo.assertCurrentActivity("Did not change",IncomingRequestsActivity.class);
        assertTrue(solo.waitForText("Incoming Requests",1,2000));
        //only one requester
        assertTrue(solo.waitForText("Title: The Trials of Apollo Book One The Hidden Oracle",1,2000));
        assertTrue(solo.waitForText("Requester: buddytesterincoming",1,2000));
        assertTrue(solo.waitForText("Pending...",1,2000));
        //select item and accept req
        solo.clickInList(1);
        solo.clickOnButton("Accept");
        solo.waitForActivity(LocationActivity.class);
        solo.assertCurrentActivity("Did not go to location", LocationActivity.class);
        MapView mapView= (MapView) solo.getView(R.id.locationView);
        solo.clickOnView(mapView);
        solo.clickOnButton("Select Location");
        solo.waitForActivity(IncomingRequestsActivity.class);
        assertTrue(solo.waitForText("Accepted!",1,2000));

        //deletes the book so that it can be redone
        //doesn't work as books with reqs can't be deleted
        solo.goBack();
        solo.goBack();
        solo.goBack();
        solo.clickOnButton("My Books");
        solo.waitForActivity(OwnedBooksActivity.class);
        solo.clickInList(2);
        solo.clickOnButton("delete");
    }

    /**Tests that the owner can delete any request made to them
     *
     */

    @Test
    public void checkDelete(){
        //adds the book to the test owner
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@incomingreqsactivity.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "123456");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class,10000);
        solo.assertCurrentActivity("Wrong Activity", DashboardActivity.class);
        solo.clickOnButton("Scan ISBN");
        solo.clickOnButton("Add book to owned books");
        solo.enterText((EditText)solo.getView(R.id.enterISBNCode), "9781894810647");
        solo.clickOnButton("Enter ISBN Code");
        solo.goBack();
        solo.goBack();
        solo.goBack();

        //requests the book with the test borrower
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@buddyforincoming.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "123456");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class,10000);
        solo.clickOnButton("Search for Books");
        solo.waitForActivity(BookSearchActivity.class);
        solo.enterText((EditText)solo.getView(R.id.book_search_bar),"Complete Englishsmart");
        solo.clickOnButton("search");
        solo.clickInList(1);
        solo.clickOnButton("Request");
        solo.goBack();
        solo.goBack();

        //checks the deletion of a request
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@incomingreqsactivity.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "123456");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class,10000);
        solo.assertCurrentActivity("Wrong Activity (Dashboard)", DashboardActivity.class);
        solo.clickOnButton("My Requests");
        solo.waitForFragmentByTag("REQUESTS");
        solo.clickOnButton("Incoming Requests");
        solo.waitForDialogToClose(200);
        solo.waitForActivity(IncomingRequestsBooks.class);

        //checks the current books with reqs
        assertTrue(solo.waitForText("Complete Englishsmart",1,2000));
        solo.clickInList(3);
        solo.assertCurrentActivity("Did not change",IncomingRequestsActivity.class);
        assertTrue(solo.waitForText("Incoming Requests",1,2000));
        //only one requester
        assertTrue(solo.waitForText("Title: Complete Englishsmart",1,2000));
        assertTrue(solo.waitForText("Requester: buddytesterincoming",1,2000));
        assertTrue(solo.waitForText("Pending...",1,2000));
        solo.clickInList(1);
        solo.clickOnButton("Decline");
        assertFalse(solo.searchText("Title: Complete Englishsmart"));
        assertFalse(solo.searchText("Requester: buddytesterincoming"));
        assertFalse(solo.searchText("Pending..."));

        //deletes the book so that it can be redone
        solo.goBack();
        solo.goBack();
        solo.goBack();
        solo.clickOnButton("My Books");
        solo.waitForActivity(OwnedBooksActivity.class);
        solo.clickInList(3);
        solo.clickOnButton("delete");
    }

    /** Runs after each test to close down active activities
     *
     * @throws Exception
     */

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
