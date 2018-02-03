package com.yesmeal.yesmealnotes.ymutils;

import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by asnimansari on 29/10/17.
 */

public class CusUtils {
    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);

//
        }
        return mDatabase;
    }

    public static String getDisplayDateString(Date past, Date now) {


        try {
            long milliseconds = TimeUnit.MILLISECONDS.toMillis(now.getTime() - past.getTime());
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());

            if(days>0){
                return days + " day ago";
            }else if(hours > 0){
                return hours + " hours";
            }
            else if(minutes>0){
                return minutes + " mins";
            }
            else{
                return seconds + "sec";

            }
        }
        catch (Exception j){
            j.printStackTrace();
        }
        return "Test";
    }
}