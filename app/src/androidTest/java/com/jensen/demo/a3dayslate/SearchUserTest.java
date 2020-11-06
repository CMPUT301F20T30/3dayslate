package com.jensen.demo.a3dayslate;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import static org.junit.Assert.assertEquals;
import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
/* SearchUser Test

   Version 1.0.0

   November 3 2020

   Copyright [2020] [Anita Ferenc]
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
 * Test class for SearchUserActivity and DisplayUserSearchActivity. All the UI tests are written here. Robotium test framework is
 used
 */
public class SearchUserTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<UserSearchActivity> rule =
            new ActivityTestRule<>(UserSearchActivity.class,true,true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    /**
     * Checks that the usernames display contain the search word
     */
    @Test
    public void checkUsernameDisplay(){
        solo.assertCurrentActivity("Wrong Activity", UserSearchActivity.class);

        UserSearchActivity activity = (UserSearchActivity) solo.getCurrentActivity();
        EditText searchUser = (EditText) solo.getView(R.id.user_search_bar);

        // check that list is empty
        assertEquals(0,activity.matchedUsers.size());

        // enter the search word and search for it
        solo.enterText(searchUser,"test");
        solo.clickOnButton("search");

        String word;
        Boolean isIn;

        // go through every item in the list and check that the search word is in every object in the list
        for(int i =0; i < activity.matchedUsers.size(); i++) {
            word = activity.matchedUsers.get(i).getUsername();
            isIn = word.toLowerCase().contains("test");
            assertTrue(isIn);
        }
    }

    /**
     * Test that nothing is in list if a non-existent user is searched
     */
    @Test
    public void checkNoneExistentUser(){
        solo.assertCurrentActivity("Wrong Activity", UserSearchActivity.class);

        UserSearchActivity activity = (UserSearchActivity) solo.getCurrentActivity();
        EditText searchUser = (EditText) solo.getView(R.id.user_search_bar);

        // check that list is empty
        assertEquals(0,activity.matchedUsers.size());

        // search for user that does not exist
        solo.enterText(searchUser,"USER DOES NOT EXIST");
        solo.clickOnButton("search");

        // check that nothing is displayed in the list
        assertEquals(0,activity.matchedUsers.size());
    }

    /**
     * Checks that after searching multiple names, only relevant usernames to search word are displayed
     * @throws InterruptedException
     */
    @Test
    public void clearTest() throws InterruptedException {
        solo.assertCurrentActivity("Wrong Activity", UserSearchActivity.class);

        UserSearchActivity activity = (UserSearchActivity) solo.getCurrentActivity();
        EditText searchUser = (EditText) solo.getView(R.id.user_search_bar);

        // check that list is empty
        assertEquals(0,activity.matchedUsers.size());

        // search for all users with test in username
        solo.enterText(searchUser,"test");
        solo.clickOnButton("search");

        String word;
        Boolean isIn;

        // check that all words displayed in list have the search word in it
        for(int i =0; i < activity.matchedUsers.size(); i++) {
            word = activity.matchedUsers.get(i).getUsername();
            isIn = word.toLowerCase().contains("test");
            assertTrue(isIn);
        }

        // check that it clears an only shows relevant information
        solo.enterText(searchUser,"");

        // search for all usernames with OwnedBooks
        solo.enterText(searchUser,"OwnedBooks");
        solo.clickOnButton("search");
        TimeUnit.SECONDS.sleep(3);

        // get the word displayed in listview
        word = activity.matchedUserStrings.get(0);
        isIn = word.contains("OwnedBooks");
        // assert that the listview contains the search word
        assertTrue(isIn);
        // assert that there is only one thing displayed related to the search word
        assertEquals(1,activity.matchedUserStrings.size());
    }

    /**
     * Checks that selecting a username shows the correct information
     */
    @Test
    public void clickInfo(){
        solo.assertCurrentActivity("Wrong Activity", UserSearchActivity.class);

        UserSearchActivity activity = (UserSearchActivity) solo.getCurrentActivity();
        EditText searchUser = (EditText) solo.getView(R.id.user_search_bar);

        // check that list is empty
        assertEquals(0,activity.matchedUsers.size());
        solo.enterText(searchUser,"OwnedBooks");
        solo.clickOnButton("search");

        // click the first item in the listview
        solo.clickInList(0);

        // check new activity opened
        solo.assertCurrentActivity("Wrong Activity",DisplayUserSearchActivity.class);
        DisplayUserSearchActivity newActivity = (DisplayUserSearchActivity)solo.getCurrentActivity();

        // check information
        String username = newActivity.user.getUsername();
        String email = newActivity.user.getEmail();
        assertEquals(username,"OwnedBooks");
        assertEquals(email,"test@ownedbooks.act");
    }

    /**
     * Close activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
