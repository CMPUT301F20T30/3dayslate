package com.jensen.demo.a3dayslate;

import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
/* SignupLoginTest

   Version 1.0.0

   November 5 2020

   Copyright [2020] [Houston Le]

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
 * Test class for the sign up / login
 */
public class SignupLoginTest {
    private Solo solo;

    /**
     * Sets up the tests
     * @throws Exception
     */
    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws  Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     * Tests that the dashboard activity is the first activity gone to after logging in
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

    /**
     * Tests that wrong password doesn't log you in
     */
    @Test
    public void checkWrongPassword(){
        solo.assertCurrentActivity("WRONG ACTIVITY", MainActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@dashboard.act");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "thisshouldfail");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class);
        solo.assertCurrentActivity("LOGIN SHOULD FAIL. PASSWORD CHECK IS WRONG", MainActivity.class);
    }

    /**
     * Tests that wrong email doesn't log you in somehow
     */
    @Test
    public void checknonexistentemail(){
        solo.assertCurrentActivity("WRONG ACTIVITY", MainActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "thisdoesnotexist");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "thisshouldfail");
        solo.clickOnButton("Login");
        solo.waitForActivity(DashboardActivity.class);
        solo.assertCurrentActivity("NONEXISTANT EMAIL. SHOULD NOT LOG IN", MainActivity.class);
    }

    /**
     * Tests signup if email is already in database which should fail
     */
    @Test
    public void checkemailindatabase(){
        solo.assertCurrentActivity("WRONG ACTIVITY", MainActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "test@dashboard.act");
        solo.enterText((EditText)solo.getView(R.id.enter_username), "thisdoesnotexist");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "thisshouldfail");
        solo.clickOnButton("Signup");
        solo.waitForActivity(DashboardActivity.class);
        solo.assertCurrentActivity("EMAIL ALREADY TAKEN. SHOULD NOT SIGNUP", MainActivity.class);
    }

    /**
     * Tests signup if username is already in database which should fail
     */
    @Test
    public void checkusernametaken(){
        solo.assertCurrentActivity("WRONG ACTIVITY", MainActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "testbrandnew@dashboard.act");
        solo.enterText((EditText)solo.getView(R.id.enter_username), "DashboardTest");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "thisshouldfail");
        solo.clickOnButton("Signup");
        solo.waitForActivity(DashboardActivity.class);
        solo.assertCurrentActivity("USERNAME TAKEN. SHOULD NOT SIGNUP", MainActivity.class);
    }

    /**
     * Tests signup if password is too short
     */
    @Test
    public void checkshortpassword(){
        solo.assertCurrentActivity("WRONG ACTIVITY", MainActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "new@test.com");
        solo.enterText((EditText)solo.getView(R.id.enter_username), "newacc");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "short");
        solo.clickOnButton("Signup");
        solo.waitForActivity(DashboardActivity.class);
        solo.assertCurrentActivity("INVALID PASSWORD. SHOULD NOT SIGNUP", MainActivity.class);
    }

    /**
     * Tests clean signup
     */
    @Test
    public void checkcleansignup(){
        solo.assertCurrentActivity("WRONG ACTIVITY", MainActivity.class);
        solo.enterText((EditText)solo.getView(R.id.enter_email), "new@test.com");
        solo.enterText((EditText)solo.getView(R.id.enter_username), "newacc");
        solo.enterText((EditText)solo.getView(R.id.enter_password), "hihihi");
        solo.clickOnButton("Signup");
        solo.waitForActivity(DashboardActivity.class);
        solo.assertCurrentActivity("FAIL TO SIGNUP", DashboardActivity.class);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth uAuth = FirebaseAuth.getInstance();
        FirebaseUser user = uAuth.getCurrentUser();
        db.collection("users").document(user.getDisplayName()).delete();
        user.delete();
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