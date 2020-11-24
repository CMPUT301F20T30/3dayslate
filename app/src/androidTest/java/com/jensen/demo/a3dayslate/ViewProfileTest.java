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
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
/* ViewProfile Test

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
 * Test class for ViewProfileActivity. All the UI tests are written here. Robotium test framework is
 used
 */
public class ViewProfileTest {
    private Solo solo;
    private String setUsername;
    private String originalEmail;
    private String setEmail;
    private FirebaseFirestore db;
    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class,true,true);
    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{

        // sign into profile test account
        originalEmail = "profiletest@gmail.com";
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        solo.assertCurrentActivity("WRONG ACTIVITY", MainActivity.class);

        // enter email and password to sign in
        solo.enterText((EditText)solo.getView(R.id.enter_email), originalEmail);
        solo.enterText((EditText)solo.getView(R.id.enter_password), "123456");
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
     * Check if the current user signed in is displayed correctly with username
     */
    @Test
    public void checkUsernameDisplay(){

        // go from Main Activity to View Profile Activity
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class);
        solo.clickOnButton("My Profile");

        //Asserts that the current activity is the MainActivity. Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", ViewProfileActivity.class);

        ViewProfileActivity activity = (ViewProfileActivity) solo.getCurrentActivity();
        String usernameDisplay = activity.currentUser.getDisplayName();

        final FirebaseAuth uAuth = FirebaseAuth.getInstance();
        setUsername = uAuth.getCurrentUser().getDisplayName();

        // assert that username displayed is the same as the current user signed in
        assertEquals(setUsername,usernameDisplay);
    }
    /**
     * Checks if email of current user signed in is displayed correctly
     */
    @Test
    public void checkEmail(){
        // go from Main Activity to View Profile Activity
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class);
        solo.clickOnButton("My Profile");
        solo.assertCurrentActivity("Wrong Activity", ViewProfileActivity.class);

        ViewProfileActivity activity = (ViewProfileActivity) solo.getCurrentActivity();
        String emailDisplay = activity.currentUser.getEmail();

        final FirebaseAuth uAuth = FirebaseAuth.getInstance();
        setEmail = uAuth.getCurrentUser().getEmail();

        // assert that email displayed is the same as the current user signed in
        assertEquals(setEmail,emailDisplay);
    }

    /**
     * check if dialog opens if want to edit
     */
    @Test
    public void checkOpenEdit(){
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class);
        solo.clickOnButton("My Profile");
        solo.assertCurrentActivity("Wrong Activity", ViewProfileActivity.class);
        solo.clickOnButton("Edit Profile");
        assertTrue(solo.waitForDialogToOpen());
    }

    /**
     * Edits email and checks if the change is recorded properly
     * @throws InterruptedException
     */
    @Test
    public void checkEditEmail() throws InterruptedException {
        // go from Main Activity to View Profile Activity
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class);
        solo.clickOnButton("My Profile");

        // change the email
        String changeEmail = "test3@gmail.com";
        solo.clickOnButton("Edit Profile");
        solo.enterText((EditText)solo.getView(R.id.view_profile_edit_email),"");
        solo.enterText((EditText)solo.getView(R.id.view_profile_edit_email),changeEmail);

        ViewProfileActivity activity = (ViewProfileActivity) solo.getCurrentActivity();
        solo.clickOnButton("Confirm");

        // wait for change to be recorded in database
        TimeUnit.SECONDS.sleep(3);

        // get email from display
        String emailDisplay = activity.currentUser.getEmail();

        // get current user email
        final FirebaseAuth uAuth = FirebaseAuth.getInstance();
        setEmail = uAuth.getCurrentUser().getEmail();

        // assert that database email and displayed email is the changed email
        assertEquals(setEmail,emailDisplay);
        assertEquals(setEmail,changeEmail);

        // change back to original email
        changeEmail = "profiletest@gmail.com";

        solo.clickOnButton("Edit Profile");
        solo.enterText((EditText)solo.getView(R.id.view_profile_edit_email),"");
        solo.enterText((EditText)solo.getView(R.id.view_profile_edit_email),changeEmail);
        solo.clickOnButton("Confirm");
        TimeUnit.SECONDS.sleep(3);
        emailDisplay = activity.currentUser.getEmail();

        setEmail = uAuth.getCurrentUser().getEmail();

        // assert that email was changed back to original one
        assertEquals(setEmail,changeEmail);
        assertEquals(emailDisplay,changeEmail);
    }

    /**
     * Checks that user cannot use an email that is currently in use
     * @throws InterruptedException
     */
    @Test
    public void checkUsedEmail() throws InterruptedException {
        // go from Main Activity to View Profile Activity
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class);
        solo.clickOnButton("My Profile");

        // set change email to a taken email
        String changeEmail = "test@buddyforincoming.act";
        solo.clickOnButton("Edit Profile");
        solo.enterText((EditText)solo.getView(R.id.view_profile_edit_email),"");
        solo.enterText((EditText)solo.getView(R.id.view_profile_edit_email),changeEmail);

        ViewProfileActivity activity = (ViewProfileActivity) solo.getCurrentActivity();

        solo.clickOnButton("Confirm");
        TimeUnit.SECONDS.sleep(3);
        String emailDisplay = activity.currentUser.getEmail();

        // get the current user email
        final FirebaseAuth uAuth = FirebaseAuth.getInstance();
        setEmail = uAuth.getCurrentUser().getEmail();

        // assert that the current email was the original email even after tried changing email
        assertEquals(originalEmail,emailDisplay);
        assertEquals(originalEmail,setEmail);
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
