package com.jensen.demo.a3dayslate;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class OutgoingRequestsActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**Tests that the activity shows the current requests it has made
     * to other books owned by other users
     */

    //interacts with the test user from IncomingRequestBooksTest.java

    @Test
    public void checkList(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@outgoingrequestsactivity.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "123456");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class,10000);
        solo.assertCurrentActivity("Wrong Activity (Dashboard)", DashboardActivity.class);
        solo.clickOnButton("My Requests");
        solo.waitForFragmentByTag("REQUESTS");
        solo.clickOnButton("Outgoing Requests");
        solo.waitForDialogToClose(200);
        solo.waitForActivity(OutgoingRequestsActivity.class);
        //book that has been requested and not accepted/denied
        assertTrue(solo.waitForText("Lost in the Storm",1,2000));
        assertTrue(solo.waitForText("Requested...",1,2000));
        //book that has been accepted
        assertTrue(solo.waitForText("Snow White and the Seven Dwarfs",1,2000));
        assertTrue(solo.waitForText("Accepted! -> Long tap to see location!",1,2000));
    }

    /**Tests that an accepted request can be clicked and view the location
     * that the owner has sent the borrower
     */

    @Test
    public void checkAcceptedRequest(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@outgoingrequestsactivity.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "123456");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class,10000);
        solo.assertCurrentActivity("Wrong Activity (Dashboard)", DashboardActivity.class);
        solo.clickOnButton("My Requests");
        solo.waitForFragmentByTag("REQUESTS");
        solo.clickOnButton("Outgoing Requests");
        solo.waitForDialogToClose(200);
        solo.waitForActivity(OutgoingRequestsActivity.class);
        //book that has been requested and not accepted/denied
        assertTrue(solo.waitForText("Lost in the Storm",1,2000));
        assertTrue(solo.waitForText("Requested...",1,2000));
        //book that has been accepted
        assertTrue(solo.waitForText("Snow White and the Seven Dwarfs",1,2000));
        assertTrue(solo.waitForText("Accepted! -> Long tap to see location!",1,2000));
        solo.clickLongInList(2);
        solo.waitForActivity(ReviewLocationActivity.class,1000);
        solo.assertCurrentActivity("Did not get location from request",ReviewLocationActivity.class);
    }

    /**Ensures that unaccepted requests are only there to display
     * the request.
     */

    @Test
    public void checkRegularRequest(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@outgoingrequestsactivity.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "123456");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class,10000);
        solo.assertCurrentActivity("Wrong Activity (Dashboard)", DashboardActivity.class);
        solo.clickOnButton("My Requests");
        solo.waitForFragmentByTag("REQUESTS");
        solo.clickOnButton("Outgoing Requests");
        solo.waitForDialogToClose(200);
        solo.waitForActivity(OutgoingRequestsActivity.class);
        //book that has been requested and not accepted/denied
        assertTrue(solo.waitForText("Lost in the Storm",1,2000));
        assertTrue(solo.waitForText("Requested...",1,2000));
        //book that has been accepted
        assertTrue(solo.waitForText("Snow White and the Seven Dwarfs",1,2000));
        assertTrue(solo.waitForText("Accepted! -> Long tap to see location!",1,2000));
        solo.clickLongInList(1);
        solo.assertCurrentActivity("Should not have location",OutgoingRequestsActivity.class);
    }


    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
