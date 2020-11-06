package com.jensen.demo.a3dayslate;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/* IncomingRequestsBooksTest Class

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
 *  IncomingRequestsBooksTest using robotium
 *
 */

public class IncomingRequestBooksTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**Method that runs before all tests and gets solo instance
     *
     * @throws Exception
     */

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

    /** Runs after each test to close down active activities
     *
     * @throws Exception
     */

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
