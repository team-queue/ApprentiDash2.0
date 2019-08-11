package com.example.teamboolean.apprentidash.Models;

import com.example.teamboolean.apprentidash.Models.AppUser;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;

@Entity
public class Day {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @DateTimeFormat(pattern="yyyy-mm-dd HH:mm:ss")
    LocalDateTime clockIn;
    @DateTimeFormat(pattern="yyyy-mm-dd HH:mm:ss")
    LocalDateTime clockOut;
    @DateTimeFormat(pattern="yyyy-mm-dd HH:mm:ss")
    LocalDateTime lunchStart;
    @DateTimeFormat(pattern="yyyy-mm-dd HH:mm:ss")
    LocalDateTime lunchEnd;

    @ManyToOne
    AppUser user;

    public Day(){}

    public Day(LocalDateTime clockIn, LocalDateTime clockOut, LocalDateTime lunchStart, LocalDateTime lunchEnd, AppUser user) {
        this.clockIn = clockIn;
        this.clockOut = clockOut;
        this.lunchStart = lunchStart;
        this.lunchEnd = lunchEnd;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public LocalDateTime getClockIn() {
        return clockIn;
    }

    public void setClockIn(LocalDateTime clockIn) {
        this.clockIn = clockIn;
    }

    public LocalDateTime getClockOut() {
        return clockOut;
    }

    public void setClockOut(LocalDateTime clockOut) {
        this.clockOut = clockOut;
    }

    public LocalDateTime getLunchStart() {
        return lunchStart;
    }

    public void setLunchStart(LocalDateTime lunchStart) {
        this.lunchStart = lunchStart;
    }

    public LocalDateTime getLunchEnd() {
        return lunchEnd;
    }

    public void setLunchEnd(LocalDateTime lunchEnd) {
        this.lunchEnd = lunchEnd;
    }

    /**
     * Method to calculate daily working hours
     * @return number of hours worked/day
     */
    public double calculateDailyHours(){
        double result = 0.0;
        if(clockIn != null && clockOut!= null && lunchStart != null && lunchEnd != null) {
            long dailyWork = Math.abs(Duration.between(clockIn, clockOut).toMinutes());
            long lunch = Math.abs(Duration.between(lunchStart, lunchEnd).toMinutes());
            result = (double) (dailyWork - lunch) / 60;
        }

        return result;
    }


    /**
     * Method to calculate duration of lunch
     * @return lunch duration in minutes
     */
    public double calculateLunch(){

        double result = 0.0;
        if(lunchStart != null && lunchEnd != null) {
            result = (double)Math.abs(Duration.between(lunchStart, lunchEnd).toMinutes())/ 60;
        }

        return result;
        
    }

    /**
     * Method to return string separated by comma
     * @return String
     */
    public String toExcelString(){
        //day string
        DateTimeFormatter dayFormat = DateTimeFormatter.ofPattern("EEEE");
        String day = clockIn.format(dayFormat);
        //date string
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String date = clockIn.format(dateFormat);
        //Time in and time out
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
        String timeIn = clockIn.format(timeFormat);

        String timeOut = "";
        if(clockOut != null){
            timeOut = clockOut.format(timeFormat);
        }

        double lunch = this.calculateLunch();
        double dailyHours = this.calculateDailyHours();

        StringBuilder result = new StringBuilder();
        result.append(day + ",");
        result.append(date + ",");
        result.append(timeIn + ",");
        result.append(timeOut + ",");
        result.append(lunch + ",");
        result.append(dailyHours);

        return result.toString();

    }



}
