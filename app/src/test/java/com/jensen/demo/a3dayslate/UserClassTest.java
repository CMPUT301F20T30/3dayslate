package com.jensen.demo.a3dayslate;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
/* User Class Test
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
 * Test the User Class
 */
public class UserClassTest {

    /**
     * Creates a user for tests
     * @return
     */
    private User MockUser() {
        User user = new User("TestUser", "test@gmail.com");
        return user;
    }

    /**
     * Checks that user class returns proper name
     */
    @Test
    public void TestGetUsername() {
        User user = MockUser();
        String username = user.getUsername();
        assertEquals("TestUser", username);
    }

    /**
     * Tests that the set email is what is returned from user class
     */
    @Test
    public void TestGetEmail() {
        User user = MockUser();
        String email = user.getEmail();
        assertEquals("test@gmail.com", email);
    }

    /**
     * Tests that you can set the email
     */
    @Test
    public void TestSetEmail() {
        User user = MockUser();
        user.setEmail("test2@gmail.com");
        String newEmail = user.getEmail();
        assertEquals("test2@gmail.com", newEmail);
    }

}