package com.example.teamboolean.apprentidash;

import com.example.teamboolean.apprentidash.Models.AppUser;
import org.junit.Test;

import static org.junit.Assert.*;

public class AppUserTest {


    // This is testing the empty constructor
    @Test
    public void appUserNull(){
        AppUser test = new AppUser();
        assertNull("this should be null", test.getUsername());
        assertNull("this should be null", test.getPassword());
        assertNull("this should be null", test.getFirstName());
        assertNull("this should be null", test.getLastName());
        assertNull("this should be null", test.getManagerName());
    }


    // This is testing the constructor when creating a new user
    @Test
    public void appUserCreated(){

        String userName = "itsName";
        String password = "test";
        String firstName = "FirstName";
        String lastName = "LastName";
        String managerName = "ManagerName";
        String email = "Email";

        AppUser test = new AppUser(userName, password, firstName, lastName, managerName, email);


        assertEquals("should return instance of a username", "itsName", test.getUsername());
        assertEquals("should return instance of a password", "test", test.getPassword());
        assertEquals("should return instance of a first name", "FirstName", test.getFirstName());
        assertEquals("should return instance of a last name", "LastName", test.getLastName());
        assertEquals("should return instance of a manager name", "ManagerName", test.getManagerName());
        assertEquals("should return instance of an email", "Email", test.getEmail());

    }


    // This is testing the constructor with a missing manager field.
    // Manager is the only non-required field upon submit with Thyme leaf
    @Test
    public void appUserMissingFields(){

        String userName = "itsName";
        String password = "test";
        String firstName = "FirstName";
        String lastName = "LastName";

        AppUser test = new AppUser(userName, password, firstName, lastName, "", "");


        assertEquals("should return instance of a username", "itsName", test.getUsername());
        assertEquals("should return instance of a password", "test", test.getPassword());
        assertEquals("should return instance of a first name", "FirstName", test.getFirstName());
        assertEquals("should return instance of a last name", "LastName", test.getLastName());
        assertEquals("should return instance of a manager name", "", test.getManagerName());
    }

    @Test
    public void appUserSetters(){

        AppUser test = new AppUser();

        String userName = "itsName";
        String password = "test";
        String firstName = "FirstName";
        String lastName = "LastName";
        String managerName = "ManagerName";

        test.setUsername(userName);
        test.setPassword(password);
        test.setFirstName(firstName);
        test.setLastName(lastName);
        test.setManagerName(managerName);


        assertEquals("should return instance of a username", "itsName", test.getUsername());
        assertEquals("should return instance of a password", "test", test.getPassword());
        assertEquals("should return instance of a first name", "FirstName", test.getFirstName());
        assertEquals("should return instance of a last name", "LastName", test.getLastName());
        assertEquals("should return instance of a manager name", "ManagerName" , test.getManagerName());
    }
}