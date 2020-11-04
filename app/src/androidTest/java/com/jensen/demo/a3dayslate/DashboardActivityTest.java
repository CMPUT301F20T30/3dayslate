package com.jensen.demo.a3dayslate;

import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/** Test class for the dashboard activity
 *
 */
public class DashboardActivityTest {
    private Solo solo;


    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws  Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /** Tests that the dashboard activity is the first activity gone to after logging in
     *
     */
    @Test
    public void checkLoginToDashboard(){
        solo.assertCurrentActivity("WRONG ACTIVITY", MainActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@dashboard.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "testdashboard");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class);
        solo.assertCurrentActivity("NOT DASHBOARD", DashboardActivity.class);
    }

    /**Tests that the messsage at the top of dashboard activity displays the proper message
     *
     */
    @Test
    public void checkMesasge(){
        solo.assertCurrentActivity("WRONG ACTIVITY", MainActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@dashboard.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "testdashboard");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class);
        solo.assertCurrentActivity("NOT DASHBOARD", DashboardActivity.class);
        //get dashboard activity to access its stuff
        DashboardActivity activity = (DashboardActivity) solo.getCurrentActivity();
        final TextView userDisplay = activity.userDisplay;
        String message = userDisplay.getText().toString();
        assertEquals("Welcome DashboardTest", message);
    }

    /**
     * Tests that clicking search for books goes to proper activity
     */
    @Test
    public void checkBookSearch(){
        solo.assertCurrentActivity("WRONG ACTIVITY", MainActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@dashboard.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "testdashboard");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class);
        solo.assertCurrentActivity("NOT DASHBOARD", DashboardActivity.class);
        solo.clickOnButton("Search for Books");
        solo.waitForActivity(BookSearchActivity.class);
        solo.assertCurrentActivity("NOT BOOK SEARCH", BookSearchActivity.class);
    }

    /**
     *  Tests that clicking search for people goes to proper activity
     */
    @Test
    public void checkUserSearch(){
        solo.assertCurrentActivity("WRONG ACTIVITY", MainActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@dashboard.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "testdashboard");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class);
        solo.assertCurrentActivity("NOT DASHBOARD", DashboardActivity.class);
        solo.clickOnButton("SEARCH FOR PEOPLE");
        solo.waitForActivity(  UserSearchActivity.class);
        solo.assertCurrentActivity("NOT USER SEARCH", UserSearchActivity.class);
    }

    /**
     * Tests that clicking my books goes to proper activity
     */
    @Test
    public void checkOwnedBooks(){
        solo.assertCurrentActivity("WRONG ACTIVITY", MainActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@dashboard.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "testdashboard");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class);
        solo.assertCurrentActivity("NOT DASHBOARD", DashboardActivity.class);
        solo.clickOnButton("My Books");
        solo.waitForActivity(OwnedBooksActivity.class);
        solo.assertCurrentActivity("NOT OWNED BOOKS", OwnedBooksActivity.class);
    }

    /**
     * Tests that clicking my borrowed books goes to proper activity
     */
    @Test
    public void checkBorrowedBooks(){
        solo.assertCurrentActivity("WRONG ACTIVITY", MainActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@dashboard.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "testdashboard");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class);
        solo.assertCurrentActivity("NOT DASHBOARD", DashboardActivity.class);
        solo.clickOnButton("My Borrowed Books");
        solo.waitForActivity(BorrowedBooksActivity.class);
        solo.assertCurrentActivity("NOT BORROWED BOOKS", BorrowedBooksActivity.class);
    }

    /**
     * Tests that clicking my profile goes to proper activity
     */
    @Test
    public void checkMyProfile(){
        solo.assertCurrentActivity("WRONG ACTIVITY", MainActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@dashboard.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "testdashboard");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class);
        solo.assertCurrentActivity("NOT DASHBOARD", DashboardActivity.class);
        solo.clickOnButton("My Profile");
        solo.waitForActivity(ViewProfileActivity.class);
        solo.assertCurrentActivity("NOT MY PROFILE", ViewProfileActivity.class);
    }

    /**
     * Tests that clicking scan ISBN goes to proper activity
     */
    @Test
    public void checkScanISBN(){
        solo.assertCurrentActivity("WRONG ACTIVITY", MainActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@dashboard.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "testdashboard");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class);
        solo.assertCurrentActivity("NOT DASHBOARD", DashboardActivity.class);
        solo.clickOnButton("Scan ISBN");
        solo.waitForActivity(getBookByISBN.class);
        solo.assertCurrentActivity("NOT SCAN ISBN", getBookByISBN.class);
    }

    /**
     * Tests that clicking incoming requests goes to proper activity
     */
    @Test
    public void checkIncomingRequests(){
        solo.assertCurrentActivity("WRONG ACTIVITY", MainActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@dashboard.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "testdashboard");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class);
        solo.assertCurrentActivity("NOT DASHBOARD", DashboardActivity.class);
        solo.clickOnButton("My Requests");
        solo.sleep(1000);
        solo.clickOnButton("Incoming Requests");
        solo.waitForActivity(IncomingRequestsActivity.class);
        solo.assertCurrentActivity("NOT INCOMING REQUESTS", IncomingRequestsActivity.class);
    }

    /**
     * Tests that clicking outgoing requests goes to proper activity
     */
    @Test
    public void checkOutgoingRequests(){
        solo.assertCurrentActivity("WRONG ACTIVITY", MainActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@dashboard.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "testdashboard");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class);
        solo.assertCurrentActivity("NOT DASHBOARD", DashboardActivity.class);
        solo.clickOnButton("My Requests");
        solo.sleep(1000);
        solo.clickOnButton("Outgoing Requests");
        solo.waitForActivity(OutgoingRequestsActivity.class);
        solo.assertCurrentActivity("NOT OUTGOING REQUEST", OutgoingRequestsActivity.class);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
