package com.jensen.demo.a3dayslate;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserClassTest {

    private User MockUser() {
        User user = new User("TestUser", "test@gmail.com");
        return user;
    }

    @Test
    public void TestGetUsername() {
        User user = MockUser();
        String username = user.getUsername();
        assertEquals("TestUser", username);
    }

    @Test
    public void TestGetEmail() {
        User user = MockUser();
        String email = user.getEmail();
        assertEquals("test@gmail.com", email);
    }

    @Test
    public void TestSetEmail() {
        User user = MockUser();
        user.setEmail("test2@gmail.com");
        String newEmail = user.getEmail();
        assertEquals("test2@gmail.com", newEmail);
    }

}