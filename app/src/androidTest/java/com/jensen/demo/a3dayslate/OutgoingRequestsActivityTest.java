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

/* OutgoingRequestsActivityTest Class

   Version 1.0.0

   November 5 2020

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
 *  OutgoingRequestsActivityTest using robotium
 *
 */

public class OutgoingRequestsActivityTest {
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

    /**Tests that the activity shows the current requests it has made
     * to other books owned by other users
     */

    //interacts with the test user from IncomingRequestBooksTest.java

    @Test
    public void checkList(){
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
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
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
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
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
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

    /** Runs after each test to close down active activities
     *
     * @throws Exception
     */

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
