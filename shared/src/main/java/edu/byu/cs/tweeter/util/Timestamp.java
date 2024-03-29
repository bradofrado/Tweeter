package edu.byu.cs.tweeter.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Timestamp {
    public static final String DATE_FORMAT = "E MMM d k:mm:ss z y";
    public static String getFormattedDate(long timestamp){
        return getDateFormat().format(new Date(timestamp));
    }

    public static SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat(DATE_FORMAT);
    }

    public static Long getMillis(String date) {
        try {
            return getDateFormat().parse(date).getTime();
        } catch (Exception ex) {
            return null;
        }
    }
}