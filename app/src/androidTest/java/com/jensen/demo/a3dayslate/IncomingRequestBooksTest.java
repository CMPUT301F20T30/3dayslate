package com.jensen.demo.a3dayslate;

import android.app.Fragment;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class IncomingRequestBooksTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    //interacts with the test user from OutgoingRequestsActivityTest
    //book isbns for ref 9780545079273 9780721406480

    /**Tests that books that have requests actually are displayed
     *
     */

    @Test
    public void checkList(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@incomingreqsbooks.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "123456");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class,10000);
        solo.assertCurrentActivity("Wrong Activity (Dashboard)", DashboardActivity.class);
        solo.clickOnButton("My Requests");
        solo.waitForFragmentByTag("REQUESTS");
        solo.clickOnButton("Incoming Requests");
        solo.waitForDialogToClose(200);
        solo.waitForActivity(IncomingRequestsBooks.class);
        assertTrue(solo.waitForText("Lost in the Storm",1,2000));
        assertTrue(solo.waitForText("Snow White and the Seven Dwarfs",1,2000));
    }

    /**Test to make sure that tapping on a book will go to the
     * next activity
     */

    @Test
    public void checkTap(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@incomingreqsbooks.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "123456");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class,10000);
        solo.assertCurrentActivity("Wrong Activity (Dashboard)", DashboardActivity.class);
        solo.clickOnButton("My Requests");
        solo.waitForFragmentByTag("REQUESTS");
        solo.clickOnButton("Incoming Requests");
        solo.waitForDialogToClose(200);
        solo.waitForActivity(IncomingRequestsBooks.class);
        assertTrue(solo.waitForText("Lost in the Storm",1,2000));
        assertTrue(solo.waitForText("Snow White and the Seven Dwarfs",1,2000));
        solo.clickInList(1);
        solo.assertCurrentActivity("Did not change",IncomingRequestsActivity.class);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
