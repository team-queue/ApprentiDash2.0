package com.example.teamboolean.apprentidash.Controllers;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.example.teamboolean.apprentidash.Models.AppUser;
import com.example.teamboolean.apprentidash.Models.Day;
import com.example.teamboolean.apprentidash.Repos.AppUserRepository;
import com.example.teamboolean.apprentidash.Repos.DayRepository;
import javafx.util.converter.LocalDateTimeStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.security.Principal;
import java.text.DecimalFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;

@Controller
public class TimesheetController {

    //Reference on setting local TZ: https://www.logicbig.com/tutorials/spring-framework/spring-web-mvc/client-time-zone.html
    private static ZoneId TIMEZONE;
    //first Day of the week
    private DayOfWeek firstDay;
    //Day list based from date range
    private List<Day> dateRange;
    //total hours worked
    private double totalHours;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    DayRepository dayRepository;

    /********************************* The controller methods to handle our Punch In page **************************************************************/
  //route to handle when a user first comes to the punch in page
    @GetMapping("/recordHour")
    public String recordHour(ZoneId clientZoneId, Model m, Principal p){
        TIMEZONE = clientZoneId;
        String tz = clientZoneId.normalized().getId();
        //Sets the necessary variables for the nav bar
        loggedInStatusHelper(m, p);
        m.addAttribute("currentPage", "clock_in");
        //Sets status for knowing which button to show
        LocalDateTime now = LocalDateTime.now(TIMEZONE);
        AppUser currentUser = appUserRepository.findByUsername(p.getName());
        m.addAttribute("workStatus", buttonRenderHelper(currentUser));
        m.addAttribute("todayDate", now);
        m.addAttribute("tz",tz);
        return "recordHour";
    }

    //Route to handle our clock in button
    @PostMapping(value="/recordHour", params="name=value")
    public String clockInSave(ZoneId clientZoneId, Principal p, Model m) {
//        TIMEZONE = clientZoneId;

        TIMEZONE = ZoneId.of("UTC-07:00");

        AppUser currentUser = appUserRepository.findByUsername(p.getName());
        LocalDateTime now = LocalDateTime.now(TIMEZONE);

        System.out.println(now.toString());

        //check what day instance variable needs to be updated based on the sequence of clockin-lunchin-lunchout-clockout
        if(buttonRenderHelper(currentUser).equals("clockIn")) {
            currentUser.getCurrentday().setClockIn(now);
        }else if(buttonRenderHelper(currentUser).equals(("lunchIn"))) {
            currentUser.getCurrentday().setLunchStart(now);
        }else if(buttonRenderHelper(currentUser).equals("lunchOut")) {
            currentUser.getCurrentday().setLunchEnd(now);
        }else if(buttonRenderHelper(currentUser).equals("clockOut")) {

            if (currentUser.isOptText()){
                sendMessage(currentUser.getPhone(), "You are clockOut. Have a good day!");
            }
            currentUser.getCurrentday().setClockOut(now);
        }

        //set the day instance user to the current user
        currentUser.getCurrentday().setUser(currentUser);
        dayRepository.save(currentUser.getCurrentday());
        m.addAttribute("workStatus", buttonRenderHelper(currentUser));
        return "redirect:/recordHour";
    }


    //route to handle when a user wants to add an additional day to their record
    @GetMapping ("/additionalDayRecord")
    public RedirectView makeDay(Principal p){
        AppUser currentUser = appUserRepository.findByUsername(p.getName());
        currentUser.setCurrentday(null);
        appUserRepository.save(currentUser);
        return new RedirectView("/recordHour");
    }

/******************************** End of the controller for handle Punch In page ********************************************************************/


/******************************** Summary Route ******************************************************************************/
    @GetMapping("/summary")
    public String getSummary(ZoneId clientZoneId, Principal p, Model m, String fromDate, String toDate){
        TIMEZONE = clientZoneId;

        //Sets the necessary variables for the nav bar
        loggedInStatusHelper(m, p);
        m.addAttribute("currentPage", "summary");

        //Retrieve info of logged in user
        AppUser currentUser = appUserRepository.findByUsername(p.getName());

        //Add to the model for display
        m.addAttribute("user", currentUser);

        //Get associated days of the user
        List<Day> userDays = currentUser.getDays();

        //initialize list
        dateRange = new ArrayList<>();

        //Get first day of the current week
        LocalDate from = getFirstDay(clientZoneId);
        //Get last day of current week
        LocalDate to = getLastDay(clientZoneId);

        //Check if input dates are not null, if not convert into local date
        if (fromDate != null){
            from = LocalDate.parse(fromDate);
        }

        if (toDate != null){
            to = LocalDate.parse(toDate);
        }

        //Current work hours so far
        totalHours = 0.00;
        // retrieves the days based from date range and compute the
        // total working hours
        for (Day curDay: userDays){
            LocalDate local = curDay.getClockIn().toLocalDate();

            if (local.compareTo(from) >= 0 && local.compareTo(to)<= 0){
                dateRange.add(curDay);
                totalHours += curDay.calculateDailyHours();
            }
        }

        //Sort the list by clock-in dates
        sortDateList();

        //Add to model for display in summary.html
        m.addAttribute("fromDate", from);
        m.addAttribute("toDate", to);
        m.addAttribute("days", dateRange);
        m.addAttribute("totalHours", totalHours);

        if (totalHours > 40 && currentUser.isOptText()){
            sendMessage(currentUser.getPhone(),"You total hour is over 40. Please talk to your manager!");
        }
        return "summary";
    }


    /************************************ Controller to handle the Edit page ***************************************************************************/
    @GetMapping("/edit/{dayId}")
    public String getEdit(@PathVariable long dayId, Model m, Principal p) {
        //Sets the necessary variables for the nav bar
        loggedInStatusHelper(m, p);
        m.addAttribute("currentPage", "clock_in");

        Day currentDay = dayRepository.findById(dayId).get();
        AppUser currentUser = appUserRepository.findByUsername(p.getName());
        //check if the day the user is trying to modify belongs to the user
        if(!currentUser.getDays().contains(currentDay))
            return "error";
        else{
            m.addAttribute("currentDay", currentDay);
            return "edit";
        }
    }


    @PostMapping("/edit")
    public String postEdit(long dayId,String clockIn, String clockOut, String lunchStart, String lunchEnd,
                           String newDate){
        Day currentDay = dayRepository.findById(dayId).get();

        //Since we get the time and date from two separate input fields, we are saving them separately for now.
        LocalTime clockInTime = LocalTime.parse(clockIn);
        LocalDate clockInDate = LocalDate.parse(newDate);

        //Editing LocalDateTime:  https://www.javabrahman.com/java-8/java-8-working-with-localdate-localtime-localdatetime-tutorial-with-examples/

        //update the clockIn to column to the newly edited time and date
        currentDay.setClockIn(
                currentDay.getClockIn()
                        .withHour(clockInTime.getHour())
                        .withMinute(clockInTime.getMinute())
                        .withDayOfYear(clockInDate.getDayOfYear())
                );

        //Set all the Dates to the date provided in the edit, and update new values
        //(if the user didn't update date, it already defaults to the date's original date)

        //TODO: Create a helper function for these
        //Checks if the user made any modifications to the lunch start date field
        if(!(lunchStart.equals(""))){
            LocalTime lunchStartLocalTime = LocalTime.parse(lunchStart);

            //If the LunchStart was null, initialize it using the ClockIn date
            if(currentDay.getLunchStart() == null) {
                currentDay.setLunchStart(currentDay.getClockIn());
            }

            //overwrite the hours and minutes of the lunch start to match with the modifications the user made
            //update the date to make sure it is still on the same date as clock in
            currentDay.setLunchStart(
                    currentDay.getLunchStart()
                            .withHour(lunchStartLocalTime.getHour())
                            .withMinute(lunchStartLocalTime.getMinute())
                            .withDayOfYear(clockInDate.getDayOfYear()));

        }

        //Checks if the user made any modifications to the lunch end date field
        if(!(lunchEnd.equals(""))){
            LocalTime lunchEndLocalTime = LocalTime.parse(lunchEnd);

            //If the LunchEnd was null, initialize it using the ClockIn date
            if(currentDay.getLunchEnd() == null) {
                currentDay.setLunchEnd(currentDay.getClockIn());
            }

            //overwrite the hours and minutes of the lunch end to match with the modifications the user made
            //update the date to make sure it is still on the same date as clock in
            currentDay.setLunchEnd(
                    currentDay.getLunchEnd()
                            .withHour(lunchEndLocalTime.getHour())
                            .withMinute(lunchEndLocalTime.getMinute())
                            .withDayOfYear(clockInDate.getDayOfYear()));
        }

        //Checks if the user make any modifications to the clock out date field
        if(!(clockOut.equals(""))) {
            LocalTime clockOutLocalTime = LocalTime.parse(clockOut);

            //If the Clockout was null, initialize it using the ClockIn date
            if (currentDay.getClockOut() == null) {
                currentDay.setClockOut(currentDay.getClockIn());
            }

            //overwrite the hours and minutes of the clockout to match with the modifications the user made
            //update the date to make sure it is still on the same date as clock in
            currentDay.setClockOut(
                    currentDay.getClockOut()
                            .withHour(clockOutLocalTime.getHour())
                            .withMinute(clockOutLocalTime.getMinute())
                            .withDayOfYear(clockInDate.getDayOfYear()));
        }

        dayRepository.save(currentDay);
        return "redirect:/summary";
    }


    @GetMapping("/delete/{dayId}")
    public String deleteDay(@PathVariable long dayId, Principal p){
        Day currentDay = dayRepository.findById(dayId).get();
        AppUser currentUser = appUserRepository.findByUsername(p.getName());

        //check if the day the user wants to delete belongs to the user
        if(!currentUser.getDays().contains(currentDay))
            return "error";
        else{
            //check if the day the user wants to delete is a day the user has clocked in but not clocked out for
            if(currentUser.getCurrentday() == currentDay){
                //reallocate the users currentday instance reference to null, before deleting the day
                currentUser.setCurrentday(null);
                appUserRepository.save(currentUser);
                dayRepository.delete(currentDay);
            }else{
                dayRepository.delete(currentDay);
            }
            return "redirect:/summary";
        }
    }

    /************************************ End of Controller to handle the Edit page ***************************************************************************/


    /***************************** CSV CONTROLLER ***************************/

    // Generates and downloads
    @GetMapping("/timesheet")
    public void exportCSV(HttpServletResponse response, Principal p) throws Exception {

        //set file name and content type
        String filename = "timesheet.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        //Write to file and download
        PrintWriter csvWriter = response.getWriter();

        String header = "Day,Date,Time In,Time Out,Lunch,Daily Hours";
        csvWriter.println(header);

        for(Day curDay: dateRange){

            csvWriter.println(curDay.toExcelString());
        }

        csvWriter.println(",,,,Total Hours:," + totalHours);
        csvWriter.close();

    }


    /******************************** All the helper functions ************************************/



    //helper function to handle the punch in page. It checks which day instance variable hasnt been clicked yet, and returns that to the view to
    // display a button for it
    public String buttonRenderHelper(AppUser currentUser ){
        if(currentUser.getCurrentday() == null) {
            Day day = new Day();
            currentUser.setCurrentday(day);
        }
        if(currentUser.getCurrentday().getClockIn() == null)
            return "clockIn";
        else if(currentUser.getCurrentday().getLunchStart() == null)
            return "lunchIn";
        else if(currentUser.getCurrentday().getLunchEnd() == null)
            return "lunchOut";
        else if(currentUser.getCurrentday().getClockOut() == null)
            return "clockOut";
        else
            return "notNewDay";
    }


    //Checks if the user is logged in and sets the model attributes accordingly per the navbar requirements
    public void loggedInStatusHelper(Model m, Principal p){

        //Navbar required variables for knowing if user is logged in and their name for display
        boolean isLoggedIn;
        String currentUserFirstName;

        //Check if the user is logged in and sets the variables
        if(p == null){
            isLoggedIn = false;
            currentUserFirstName = "Visitor";
        }else {
            isLoggedIn = true;
            currentUserFirstName = appUserRepository.findByUsername(p.getName()).getFirstName();
        }

        //add the attributes to the passed in model
        m.addAttribute("isLoggedIn", isLoggedIn);
        m.addAttribute("userFirstName", currentUserFirstName);
    }


    //Helper function to get the first day
    //Reference: https://stackoverflow.com/questions/22890644/get-current-week-start-and-end-date-in-java-monday-to-sunday
    private LocalDate getFirstDay(ZoneId clientZoneId){
        TIMEZONE = clientZoneId;

        firstDay = WeekFields.of(Locale.US).getFirstDayOfWeek();
        return LocalDate.now(TIMEZONE).with(TemporalAdjusters.previousOrSame(firstDay));
    }


    //Helper function to get the last day
    //Reference: https://stackoverflow.com/questions/22890644/get-current-week-start-and-end-date-in-java-monday-to-sunday
    private LocalDate getLastDay(ZoneId clientZoneId){
        TIMEZONE = clientZoneId;

        DayOfWeek lastDay = DayOfWeek.of(((firstDay.getValue() + 5) % DayOfWeek.values().length) + 1);
        return LocalDate.now(TIMEZONE).with(TemporalAdjusters.nextOrSame(lastDay));

    }

    
    //Sort dates from earliest to latest
    //Ref: http://java-buddy.blogspot.com/2013/01/sort-list-of-date.html
    private void sortDateList(){
        Collections.sort(dateRange, new Comparator<Day>(){

            @Override
            public int compare(Day o1, Day o2) {
                return o1.getClockIn().compareTo(o2.getClockIn());
            }
        });
    }

    /******************************** AWS SNS send message ************************************/
    public void sendMessage(String phoneNumber,String message) {
        AmazonSNSClient snsClient = new AmazonSNSClient();
        Map<String, MessageAttributeValue> smsAttributes =
            new HashMap<String, MessageAttributeValue>();
    //<set SMS attributes>
    sendSMSMessage(snsClient, message, phoneNumber, smsAttributes);
}

    public static void sendSMSMessage(AmazonSNSClient snsClient, String message,
                                      String phoneNumber, Map<String, MessageAttributeValue> smsAttributes) {
        PublishResult result = snsClient.publish(new PublishRequest()
                .withMessage(message)
                .withPhoneNumber(phoneNumber)
                .withMessageAttributes(smsAttributes));
        System.out.println(result); // Prints the message ID.
    }

}



