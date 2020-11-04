package com.jensen.demo.a3dayslate;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IncomingRequestsActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    //tests may fail because requests and books for test users must be set up properly before testing in database
    //TODO need to try to find a solution for this
    //testing partner (requests the books) is test@buddyforincoming.act and password is 123456
    //book isbns in order 9780743253338 9781484732748 9781894810647

    //TODO get notifs and locations tested correctly

    @Test
    public void checkARequest(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
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
        assertTrue(solo.waitForText("Williams-Sonoma Collection: Asian",1,2000));
        assertTrue(solo.waitForText("The Trials of Apollo Book One The Hidden Oracle",1,2000));
        assertTrue(solo.waitForText("Complete Englishsmart",1,2000));
        solo.clickInList(1);
        solo.assertCurrentActivity("Did not change",IncomingRequestsActivity.class);
        assertTrue(solo.waitForText("Incoming Requests",1,2000));
        //only one requester
        assertTrue(solo.waitForText("Title: Williams-Sonoma Collection: Asian",1,2000));
        assertTrue(solo.waitForText("Requester: buddytesterincoming",1,2000));
        assertTrue(solo.waitForText("Pending...",1,2000));
    }


    //TODO fix how to test location as it currently fails
    //needs to be manually set up each time

    @Test
    public void checkAcceptReq(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
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
        //should have three books requested
        assertTrue(solo.waitForText("Williams-Sonoma Collection: Asian",1,2000));
        assertTrue(solo.waitForText("The Trials of Apollo Book One The Hidden Oracle",1,2000));
        assertTrue(solo.waitForText("Complete Englishsmart",1,2000));
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
        solo.clickOnScreen(50,50);
        solo.clickOnButton("Select Location");
        solo.waitForActivity(IncomingRequestsActivity.class);
        assertTrue(solo.waitForText("Accepted!",1,2000));
    }

    //needs to be manually set up each time

    @Test
    public void checkDelete(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
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
        assertTrue(solo.waitForText("Williams-Sonoma Collection: Asian",1,2000));
        assertTrue(solo.waitForText("The Trials of Apollo Book One The Hidden Oracle",1,2000));
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

    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
