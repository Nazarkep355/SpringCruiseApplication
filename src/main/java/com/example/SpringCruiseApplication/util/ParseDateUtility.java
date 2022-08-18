package com.example.SpringCruiseApplication.util;

import java.util.Date;

public class ParseDateUtility {
    public static Date getDateFromForm(String dateStr){
        Date date= new Date();
        date.setTime(0);
        if(!dateStr.contains("T")){
            String[]segments = dateStr.split("-");
            date.setYear(Integer.parseInt(segments[0])-1900);
            date.setMonth(Integer.parseInt(segments[1])-1);
            date.setDate(Integer.parseInt(segments[2]));
            date.setHours(0);
            date.setMinutes(0);
            date.setSeconds(0);
            return date;
        }else {
            String[] segments= dateStr.split("T");
            String[] hoursMins=segments[1].split(":");
            segments = segments[0].split("-");
            date.setYear(Integer.parseInt(segments[0])-1900);
            date.setMonth(Integer.parseInt(segments[1])-1);
            date.setDate(Integer.parseInt(segments[2]));
            date.setHours(Integer.parseInt(hoursMins[0]));
            date.setMinutes(Integer.parseInt(hoursMins[1]));
            date.setSeconds(0);
            return date;}
    }
}
