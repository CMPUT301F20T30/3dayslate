package com.jensen.demo.a3dayslate;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/* ImageTest

   Version 1.0.0

   November 22 2020

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
 *  Test class for images
 */
public class ImageTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);


    /**
     * Sets up the tests
     * @throws Exception
     */
    @Before
    public void setUp() throws  Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     *  Test that image appears on book with image
     */
    @Test
    public void checkImage(){
        // get to edit book
        solo.assertCurrentActivity("WRONG ACTIVITY", LoginActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@images.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "testimages");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class);
        solo.assertCurrentActivity("NOT DASHBOARD", DashboardActivity.class);
        solo.clickOnButton("My Books");
        solo.waitForActivity(OwnedBooksActivity.class);
        solo.assertCurrentActivity("NOT OWNED BOOKS", OwnedBooksActivity.class);

        //wait for books to load in
        // this test account owns a book titled "TESTIMAGE"
        solo.waitForText("TESTIMAGE");
        //click on first book in list
        solo.clickInList(1);
        solo.clickOnButton("edit");
        solo.waitForActivity(EditBookActivity.class);
        solo.assertCurrentActivity("NOT EDIT BOOK ACTIVITY", EditBookActivity.class);

        //test that image is present

        //image takes small time to load from firebase storage
        solo.sleep(1000);

        EditBookActivity activity = (EditBookActivity) solo.getCurrentActivity();


        // test that imagePresent is true, imagePresent is set to false
        // when an image is not fetched from firebase storage
        solo.sleep(1000);
        assertEquals(true, activity.imagePresent);

    }

    /**
     * check that no image is shown for book with no image
     */
    @Test
    public void checkNoImage(){
        // get to edit book
        solo.assertCurrentActivity("WRONG ACTIVITY", LoginActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@images.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "testimages");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class);
        solo.assertCurrentActivity("NOT DASHBOARD", DashboardActivity.class);
        solo.clickOnButton("My Books");
        solo.waitForActivity(OwnedBooksActivity.class);
        solo.assertCurrentActivity("NOT OWNED BOOKS", OwnedBooksActivity.class);

        //wait for books to load in
        // this test account owns a book titled "TESTIMAGE"
        solo.waitForText("TESTIMAGE");
        //click on first book in list
        solo.clickInList(2);
        solo.clickOnButton("edit");
        solo.waitForActivity(EditBookActivity.class);
        solo.assertCurrentActivity("NOT EDIT BOOK ACTIVITY", EditBookActivity.class);

        //test that image is present

        //image takes small time to load from firebase storage
        solo.sleep(1000);

        EditBookActivity activity = (EditBookActivity) solo.getCurrentActivity();

        // check that no image was fetched from storage

        assertEquals(false, activity.imagePresent);
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
