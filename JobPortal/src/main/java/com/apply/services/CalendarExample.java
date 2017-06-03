package com.apply.services;

/**
 * Created by kunal on 5/25/17.
 */


import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;
import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class CalendarExample {

    @Autowired
    private SendMailCalendar m;


public void createCalendar(int application_id,String location1,String startDate,String endDate,String email) {


    //Initilize values
    String calFile = "interviewSchedule.ics";

    //start time
    java.util.Calendar startCal = java.util.Calendar.getInstance();


    DateTime dt;
    DateTime de;


    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        String s1 = startDate.replace("T"," ");
        s1= s1.replace("Z","");
        s1=s1.substring(0,s1.length()-4);
        System.out.println("S1:"+s1);

        org.joda.time.format.DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
         dt = dateFormatter.parseDateTime(s1);

        System.out.println("date after adding 000"+dt.getYear());
        System.out.println("year in:"+dt.getMonthOfYear());
        System.out.println("year in:"+dt.getDayOfMonth());
        System.out.println("year in:"+dt.getHourOfDay());
        System.out.println("Mibute in:"+dt.getMinuteOfHour());
        startCal.set(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth(), dt.getHourOfDay(), dt.getMinuteOfHour());


    //For end date
    String s2 = endDate.replace("T"," ");
    s2= s2.replace("Z","");
    s2=s2.substring(0,s2.length()-4);
    System.out.println("S2:"+s2);

    de = dateFormatter.parseDateTime(s2);





    //end time
    java.util.Calendar endCal = java.util.Calendar.getInstance();
    endCal.set(de.getYear(), de.getMonthOfYear(), de.getDayOfMonth(), de.getHourOfDay(), de.getMinuteOfHour());

    String subject = "Interview Schedule";
    String location = location1;
    String description = "Interview Scheduled.";

    String hostEmail = email;

    //Creating a new calendar
    net.fortuna.ical4j.model.Calendar calendar = new net.fortuna.ical4j.model.Calendar();
    calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
    calendar.getProperties().add(Version.VERSION_2_0);
    calendar.getProperties().add(CalScale.GREGORIAN);

    SimpleDateFormat sdFormat =  new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
    String strDate = sdFormat.format(startCal.getTime());
    //String strDate = sdFormat.format(start);

    net.fortuna.ical4j.model.Date startDt = null;
    try {
        startDt = new net.fortuna.ical4j.model.Date(strDate,"yyyyMMdd'T'HHmmss'Z'");
      //  startDt = new net.fortuna.ical4j.model.Date(strDate,);

    } catch (ParseException e) {
        e.printStackTrace();
    }

    long diff = endCal.getTimeInMillis() - startCal.getTimeInMillis();
    int min = (int)(diff / (1000 * 60));

    Dur dur = new Dur(0,0,min,0);

    //Creating a meeting event
    VEvent meeting = new VEvent(startDt,dur,subject);

    meeting.getProperties().add(new Location(location));
    meeting.getProperties().add(new Description());

    try {
        meeting.getProperties().getProperty(Property.DESCRIPTION).setValue(description);
    } catch (IOException e) {
        e.printStackTrace();
    } catch (URISyntaxException e) {
        e.printStackTrace();
    } catch (ParseException e) {
        e.printStackTrace();
    }

    try {
        meeting.getProperties().add(new Organizer("MAILTO:"+hostEmail));
    } catch (URISyntaxException e) {
        e.printStackTrace();
    }

    calendar.getComponents().add(meeting);

    FileOutputStream fout = null;

    try {
        fout = new FileOutputStream(calFile);
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }

    CalendarOutputter outputter = new CalendarOutputter();
    outputter.setValidating(false);

    try {
        outputter.output(calendar, fout);
    } catch (IOException e) {
        e.printStackTrace();
    } catch (ValidationException e) {
        e.printStackTrace();
    }

    System.out.println(meeting);

    m.sendMail(calFile,email,location);

}

}
