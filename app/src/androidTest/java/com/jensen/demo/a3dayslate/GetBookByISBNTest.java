package com.jensen.demo.a3dayslate;

import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/* GetBookByISBNTest

   Version 1.0.0

   November 5 2020

   Copyright [2020] [Eric Weber]

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
/**
 *  class that tests GetBookByISBN activity
 */
public class GetBookByISBNTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Sets up the tests
     * @throws Exception
     */
    @Before
    public void setUp() throws  Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     * Tests that the activity can be gotten to from the dashboard
     */
    @Test
    public void checkActivity(){
        // get to owned books
        solo.assertCurrentActivity("WRONG ACTIVITY", MainActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@getbookbyisbn.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "testgetbookbyisbn");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class);
        solo.assertCurrentActivity("NOT DASHBOARD", DashboardActivity.class);
        solo.clickOnButton("Scan ISBN");
        solo.waitForActivity(GetBookByISBN.class);
        solo.assertCurrentActivity("NOT SCAN ISBN", GetBookByISBN.class);
    }

    /**
     * Tests that books can be added by manually entering ISBN
     */
    @Test
    public void checkAddBook(){
        // get to GetBookByISBN
        solo.assertCurrentActivity("WRONG ACTIVITY", MainActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@getbookbyisbn.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "testgetbookbyisbn");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class);
        solo.assertCurrentActivity("NOT DASHBOARD", DashboardActivity.class);
        solo.clickOnButton("Scan ISBN");
        solo.waitForActivity(GetBookByISBN.class);
        solo.assertCurrentActivity("NOT SCAN ISBN", GetBookByISBN.class);
        //add book manually
        solo.enterText((EditText)solo.getView(R.id.enterISBNCode), "9780099584230");
        solo.clickOnButton("Enter ISBN Code");
        solo.sendKey(KeyEvent.KEYCODE_BACK);
        solo.waitForActivity(DashboardActivity.class);
        //test that back button goes to proper activity
        solo.assertCurrentActivity("NOT DASHBOARD", DashboardActivity.class);
        solo.clickOnButton("My Books");
        solo.waitForActivity(OwnedBooksActivity.class);
        //declare activity so its attributes can be accessed
        OwnedBooksActivity activity = (OwnedBooksActivity) solo.getCurrentActivity();
        final ListView ownedBooksList = activity.ownedBooksList;
        solo.waitForText("AVAILABLE",1,1000);
        Book book = (Book) ownedBooksList.getAdapter().getItem(0);
        String title = book.getTitle();
        assertEquals("Mathletics", title);

        //delete book so test can be run again with no set up
        solo.clickInList(1);
        solo.clickOnButton("delete");

    }
    /**
     * handles what happens after tests are done
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
