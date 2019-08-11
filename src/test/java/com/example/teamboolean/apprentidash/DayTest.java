package com.example.teamboolean.apprentidash;

import com.example.teamboolean.apprentidash.Models.AppUser;
import com.example.teamboolean.apprentidash.Models.Day;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class DayTest {

    // test for the calculation of the total hours
    @Test
    public void calculateDailyHours() {
        LocalDateTime startHour = LocalDate.now().atTime(9, 0);
        LocalDateTime endHour = LocalDate.now().atTime(18, 0);
        LocalDateTime lunchStart = LocalDate.now().atTime(12, 0);
        LocalDateTime lunchEnd = LocalDate.now().atTime(13, 0);

        AppUser userTest = new AppUser("myusername", "mypassword", "joe", "sands",
                "mngr","123@email.test");

        Day test = new Day(startHour, endHour, lunchStart, lunchEnd, userTest);

        assertEquals("Duration should be the same", 8, test.calculateDailyHours(), 0.01 );
    }

    // testing total hour calculation when the minutes is not equal to quarters of a hour.
    @Test
    public void calculateDailyHoursNotExactMinutes() {
        LocalDateTime startHour = LocalDate.now().atTime(9, 0);
        LocalDateTime endHour = LocalDate.now().atTime(18, 30);
        LocalDateTime lunchStart = LocalDate.now().atTime(12, 10);
        LocalDateTime lunchEnd = LocalDate.now().atTime(13, 10);

        AppUser userTest = new AppUser("myusername", "mypassword", "joe", "sands",
                "mngr","123@email.test");

        Day test = new Day(startHour, endHour, lunchStart, lunchEnd, userTest);

        assertEquals("Duration should be the same", 8.5, test.calculateDailyHours(), 0.01 );
    }

    // Testing empty Day constructor
    @Test
    public void testDayNull(){
        Day test = new Day();
        assertNull("this should be null", test.getClockIn());
        assertNull("this should be null", test.getClockOut());
        assertNull("this should be null", test.getLunchStart());
        assertNull("this should be null", test.getLunchEnd());
    }

    // Testing instance of a new day
    @Test
    public void testDayParameters(){

        LocalDateTime startHour = LocalDate.now().atTime(9, 0);
        LocalDateTime endHour = LocalDate.now().atTime(18, 30);
        LocalDateTime lunchStart = LocalDate.now().atTime(12, 10);
        LocalDateTime lunchEnd = LocalDate.now().atTime(13, 10);

        AppUser userTest = new AppUser("myusername", "mypassword", "joe", "sands",
                "mngr","123@email.test");

        Day test = new Day(startHour, endHour, lunchStart, lunchEnd, userTest);
        assertEquals("this should give back start time", startHour, test.getClockIn());
        assertEquals("this should give back end time", endHour, test.getClockOut());
        assertEquals("this should give back lunch end time", lunchEnd, test.getLunchEnd());
        assertEquals("this should give back lunch start time", lunchStart, test.getLunchStart());
    }

    // Test for the lunch calculation method
    @Test
    public void testDayLunchCalc(){

        LocalDateTime startHour = LocalDate.now().atTime(9, 0);
        LocalDateTime endHour = LocalDate.now().atTime(18, 30);
        LocalDateTime lunchStart = LocalDate.now().atTime(12, 10);
        LocalDateTime lunchEnd = LocalDate.now().atTime(13, 10);

        AppUser userTest = new AppUser("myusername", "mypassword", "joe", "sands",
                "mngr","123@email.test");

        Day test = new Day(startHour, endHour, lunchStart, lunchEnd, userTest);
        assertEquals("this should give back lunch start time", 1.0, test.calculateLunch(),0.01);
    }

    // Testing the lunch calculation method when not a flat hour
    @Test
    public void testDayLunchCalcNotWholeNum(){

        LocalDateTime startHour = LocalDate.now().atTime(9, 0);
        LocalDateTime endHour = LocalDate.now().atTime(18, 30);
        LocalDateTime lunchStart = LocalDate.now().atTime(12, 10);
        LocalDateTime lunchEnd = LocalDate.now().atTime(13, 42);

        AppUser userTest = new AppUser("myusername", "mypassword", "joe", "sands",
                "mngr","123@email.test");

        Day test = new Day(startHour, endHour, lunchStart, lunchEnd, userTest);
        assertEquals("this should give back lunch start time", 1.53, test.calculateLunch(),0.01);
    }

    //Test for getters
    @Test
    public void testDayGetters(){

        LocalDateTime startHour = LocalDate.now().atTime(9, 0);
        LocalDateTime endHour = LocalDate.now().atTime(18, 30);
        LocalDateTime lunchStart = LocalDate.now().atTime(12, 10);
        LocalDateTime lunchEnd = LocalDate.now().atTime(13, 10);

        AppUser userTest = new AppUser("myusername", "mypassword", "joe", "sands",
                "mngr","123@email.test");

        Day test = new Day(startHour, endHour, lunchStart, lunchEnd, userTest);
        assertEquals("this should give back start time", startHour, test.getClockIn());
        assertEquals("this should give back end time", endHour, test.getClockOut());
        assertEquals("this should give back lunch end time", lunchEnd, test.getLunchEnd());
        assertEquals("this should give back lunch start time", lunchStart, test.getLunchStart());
    }

    //Test for setters
    @Test
    public void testDaySetters(){

        Day test = new Day();

        LocalDateTime startHour = LocalDate.now().atTime(9, 0);
        LocalDateTime endHour = LocalDate.now().atTime(18, 30);
        LocalDateTime lunchStart = LocalDate.now().atTime(12, 10);
        LocalDateTime lunchEnd = LocalDate.now().atTime(13, 10);

        AppUser userTest = new AppUser("myusername", "mypassword", "joe", "sands",
                "mngr","123@email.test");

        test.setClockOut(endHour);
        test.setClockIn(startHour);
        test.setLunchEnd(lunchEnd);
        test.setLunchStart(lunchStart);

        assertEquals("this should give back start time", startHour, test.getClockIn());
        assertEquals("this should give back end time", endHour, test.getClockOut());
        assertEquals("this should give back lunch end time", lunchEnd, test.getLunchEnd());
        assertEquals("this should give back lunch start time", lunchStart, test.getLunchStart());
    }

    //test toExcelString
    @Test
    public void testDayToString(){
        LocalDateTime startHour = LocalDate.now().atTime(9, 0);
        LocalDateTime endHour = LocalDate.now().atTime(18, 30);
        LocalDateTime lunchStart = LocalDate.now().atTime(12, 10);
        LocalDateTime lunchEnd = LocalDate.now().atTime(13, 10);

        AppUser userTest = new AppUser("myusername", "mypassword", "joe", "sands",
                "mngr","123@email.test");

        Day test = new Day(startHour, endHour, lunchStart, lunchEnd, userTest);

        String result = "Thursday,06-27-2019,09:00,18:30,1.0,8.5";
        assertEquals("this should give string with day format", result, test.toExcelString());
    }

    //If there is more time, we would like to test edge cases such as null values, one value-missing, etc

}