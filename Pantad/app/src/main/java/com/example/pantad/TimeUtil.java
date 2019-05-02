package com.example.pantad;
import com.google.firebase.*;
import java.util.Date;


public final class TimeUtil {


    private TimeUtil() {

    }



    public static String getDifference(Timestamp t1) {

        Date d1 = t1.toDate();
        Date d2 = new Date();
        /** in milliseconds */
        long diff = d2.getTime() - d1.getTime();

        /** remove the milliseconds part */
        diff = diff / 1000;

        long days = diff / (24 * 60 * 60);
        long hours = diff / (60 * 60) % 24;
        long minutes = diff / 60 % 60;
        String result = "";
        if(days != 0) {
            result = days + " days, ";
        }if(days != 0 || hours != 0) {
            result += hours + " hours, ";
        }
        result += minutes + " minutes. ";

        return result;
    }


}
