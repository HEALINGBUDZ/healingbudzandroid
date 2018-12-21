package com.codingpixel.healingbudz.Utilities;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class DateConverter {
    private static PrettyTime prettyTime = new PrettyTime();

    public static String convertDateForServer(String date_string) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        Date myDate = null;
        try {
            myDate = dateFormat.parse(date_string);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return timeFormat.format(myDate);
    }

    public static String convertSpecial(String date_string) {
        DateFormat dateFormatsp;
        dateFormatsp = DateFormat
                .getDateInstance(DateFormat.LONG);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date mCurrentDate = calendar.getTime();
        Date myDate = null;
        try {
            myDate = dateFormat.parse(date_string);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("MM.dd.yyyy");

        return dateFormatsp.format(myDate);
    }

    public static String convertDateShootOut(String date_string) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date mCurrentDate = calendar.getTime();
        Date myDate = null;
        try {
            myDate = dateFormat.parse(date_string);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("MM.dd.yyyy");

        return timeFormat.format(myDate);
    }

    public static String convertDateForShooutOut(String date_string) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date mCurrentDate = calendar.getTime();
        Date myDate = calendar.getTime();
        try {
            myDate = dateFormat.parse(date_string);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("MM.dd.yyyy");
        if (myDate.getYear() == mCurrentDate.getYear()) {
            if (myDate.getMonth() == mCurrentDate.getMonth()) {
                if (myDate.getDate() == mCurrentDate.getDate()) {
                    return "Expire Soon!";
                }
            }
        }
        if (myDate.before(mCurrentDate)) {
            return "Expired";
        } else if (myDate.after(mCurrentDate)) {
            if (myDate.getYear() == mCurrentDate.getYear()) {
                if (myDate.getMonth() == mCurrentDate.getMonth()) {
                    if (myDate.getDate() > mCurrentDate.getDate()) {
                        int dayCount = myDate.getDate() - mCurrentDate.getDate();
                        if (dayCount >= 0 && dayCount <= 2) {
                            return "Expire Soon!";
                        } else {
                            return timeFormat.format(myDate);
                        }
                    } else {
                        return timeFormat.format(myDate);
                    }
                } else {
                    return timeFormat.format(myDate);
                }
            } else {
                return timeFormat.format(myDate);
            }

        } else {
            return timeFormat.format(myDate);
        }
    }

    public static String checkDatePay(String date_string) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yy");
        Date myDate = null;
        try {
            myDate = dateFormat.parse(date_string);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Date Format Error";
        }
        if (date_string.contains("/")) {
            String tv = date_string.split("/")[0];
            int vc = Integer.parseInt(tv);
            if (vc > 12) {
                return "Date Format Error";
            }
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("MM/yyyy");
        Date dt = Calendar.getInstance().getTime();
        if (dt.before(myDate)) {
            return timeFormat.format(myDate);
        } else {
            return "Date Format Error";
        }

    }

    public static String convertDate(String date_string) {
        date_string = UtcToLocal(date_string);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date myDate = null;
        try {
            myDate = dateFormat.parse(date_string);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("MM.dd.yyyy hh:mm aa");
        return timeFormat.format(myDate);
    }

    public static String convertDateTrial(String date_string) {
        date_string = UtcToLocal(date_string);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date myDate = null;
        try {
            myDate = dateFormat.parse(date_string);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("MM.dd.yyyy");
        return "End at: " + timeFormat.format(myDate);
    }

    public static String convertDateReward(String date_string) {
        date_string = UtcToLocal(date_string);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date myDate = null;
        try {
            myDate = dateFormat.parse(date_string);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("MM-dd-yyyy");
        return timeFormat.format(myDate);
    }


    public static String convertDate_with_month_name(String date_string) {
        date_string = UtcToLocal(date_string);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date myDate = null;
        try {
            myDate = dateFormat.parse(date_string);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("MMM. dd, yyyy, hh:mm aa");
        return timeFormat.format(myDate);
    }

    public static String convertDate_with_complete_details(String date_string) {
        date_string = UtcToLocal(date_string);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date myDate = null;
        try {
            myDate = dateFormat.parse(date_string);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("EEEE,dd,MMMM,yyyy, hh:mm aa");
        return timeFormat.format(myDate);
    }

    public static Date getDateTime(String format, String date_string) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date myDate = Calendar.getInstance().getTime();
        try {
            myDate = dateFormat.parse(date_string);
            return myDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return myDate = Calendar.getInstance().getTime();
        }
    }

    public static String getDateTime(String inputformat, String outPutformat, String date_string) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(inputformat);
        SimpleDateFormat dateFormatOut = new SimpleDateFormat(outPutformat);
        Date myDate = Calendar.getInstance().getTime();
        try {
            myDate = dateFormat.parse(date_string);
            return dateFormatOut.format(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getDateAsApiFormate(String date_string) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd-MMMM, yyyy HH:mm:ss");
        Date myDate = null;
        try {
            myDate = dateFormat.parse(date_string);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        return LocalToUtc(timeFormat.format(myDate));
    }

    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        SimpleDateFormat timeFormat = new SimpleDateFormat("MM/dd/yyyy");
        return timeFormat.format(date);

    }

    public static String getCustomDateString(String date_string) {
        date_string = UtcToLocal(date_string);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(date_string);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat tmp = new SimpleDateFormat("MMMM d");

        String str = tmp.format(date);
        str = str.substring(0, 1).toUpperCase() + str.substring(1);

        if (date.getDate() > 10 && date.getDate() < 14)
            str = str + "th, ";
        else {
            if (str.endsWith("1")) str = str + "st, ";
            else if (str.endsWith("2")) str = str + "nd, ";
            else if (str.endsWith("3")) str = str + "rd, ";
            else str = str + "th, ";
        }

        tmp = new SimpleDateFormat("yyyy");
        str = str + tmp.format(date);
        return str;
    }


    public static String getPrettyTime(String s) {
        try {
            DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = null;
            date = utcFormat.parse(s);
            SimpleDateFormat localTimeFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            localTimeFormate.setTimeZone(TimeZone.getDefault());
            Date dt = localTimeFormate.parse(localTimeFormate.format(date));
//            prettyTime.
            if (prettyTime.format(dt).equalsIgnoreCase("7 days ago")) {
                return "1 week ago";
            } else {
                if (prettyTime.format(dt).equalsIgnoreCase("moments ago")) {
                    return "just now";
                }
                return prettyTime.format(dt);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            DateFormat utcFormatNew = new SimpleDateFormat("yyyy-MM-dd HH:mm a", Locale.ENGLISH);
            utcFormatNew.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = null;
            try {
                date = utcFormatNew.parse(s);
                SimpleDateFormat localTimeFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                localTimeFormate.setTimeZone(TimeZone.getDefault());
                Date dt = localTimeFormate.parse(localTimeFormate.format(date));
//            prettyTime.
                return prettyTime.format(dt);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }

        }
        return null;
    }

    private static String LocalToUtc(String s) {
        try {
            DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            utcFormat.setTimeZone(TimeZone.getDefault());
            Date date = null;
            date = utcFormat.parse(s);
            SimpleDateFormat localTimeFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            localTimeFormate.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date dt = localTimeFormate.parse(localTimeFormate.format(date));
//            prettyTime.
            return localTimeFormate.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String UtcToLocal(String s) {
        try {
            DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = null;
            date = utcFormat.parse(s);
            SimpleDateFormat localTimeFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            localTimeFormate.setTimeZone(TimeZone.getDefault());
            Date dt = localTimeFormate.parse(localTimeFormate.format(date));
//            prettyTime.
            return localTimeFormate.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String validateFormat(String trim) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(trim);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat tmp = new SimpleDateFormat("yyyy-MM-dd");
        return tmp.format(date);

    }

    public static String setEventEditDate(String trim) {
        SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = parseFormat.parse(trim);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("MM/dd/yyyy");
        return timeFormat.format(date);
    }

    public static String convertSpecialWallTop(String date_string) {
        date_string = UtcToLocal(date_string);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat dateFormatHalf = new SimpleDateFormat("hh:mm aa");
        Date myDate = null;
        try {
            myDate = dateFormat.parse(date_string);

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd 'at' hh:mm aa");
        Calendar calendaras = Calendar.getInstance();
        calendaras.setTime(myDate);
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);


        if (calendaras.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendaras.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            return "Today at " + dateFormatHalf.format(myDate);
        } else if (calendaras.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendaras.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            return "Yesterday at " + dateFormatHalf.format(myDate);
        } else {
            return timeFormat.format(myDate);
        }

    }
}
