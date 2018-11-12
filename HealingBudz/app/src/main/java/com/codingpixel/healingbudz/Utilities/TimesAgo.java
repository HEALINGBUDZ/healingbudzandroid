package com.codingpixel.healingbudz.Utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by jawadali on 10/26/17.
 */

public class TimesAgo {

    private static final int MINUTE_MILLIS = 60 * 1000;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }
        long now = getCurrentGMTTime();
        if (time > now || time <= 0) {
            return "just now";
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            if ((diff / DAY_MILLIS) > 30) {
                return ((diff / DAY_MILLIS) / 30) + " Month ago";
            } else {
                return diff / DAY_MILLIS + " days ago";
            }
        }
    }

    public static String getTimeAgo(String sqlFormetedTime) {
        return getTimeAgo(getsqlFormetedTimeInMiliseconds(sqlFormetedTime));
    }

    public static long getsqlFormetedTimeInMiliseconds(String sqlFormetedTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date mDate = sdf.parse(sqlFormetedTime);
            long timeInMilliseconds = mDate.getTime();
            return timeInMilliseconds;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    private static long getCurrentGMTTime() {
        Calendar c = Calendar.getInstance();
        System.out.println("current: " + c.getTime());

        TimeZone z = c.getTimeZone();
        int offset = z.getRawOffset();
        if (z.inDaylightTime(new Date())) {
            offset = offset + z.getDSTSavings();
        }
        int offsetHrs = offset / 1000 / 60 / 60;
        int offsetMins = offset / 1000 / 60 % 60;

        System.out.println("offset: " + offsetHrs);
        System.out.println("offset: " + offsetMins);

        c.add(Calendar.HOUR_OF_DAY, (-offsetHrs));
        c.add(Calendar.MINUTE, (-offsetMins));
        return c.getTimeInMillis();
    }
}
