package com.pheonixlabs.srkuruma.fixeddepositor.Common;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by srkuruma on 7/9/2016.
 */
public class DateTimeUtils {
    public static Date GetDateFromString(String dateString, String dateFormat)
    {
        DateFormat df = new SimpleDateFormat(dateFormat);
        Date startDate = null;
        try {
            startDate = df.parse(dateString);
            String newDateString = df.format(startDate);
            System.out.println(newDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return startDate;
    }

    /**
     * TODO: Check for null in calling function
     * @param date
     * @return
     */
    public static String ConvertDateTimeToStringWithFormat(Date date, String format)
    {
        String dateString = date.toString();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static String ConvertDateTimeToSQLFormat(Date date)
    {
        String dateString = date.toString();
        SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd");
        return formatter.format(date);
    }

    public static int GetDaysBetweenDates(Date date1, Date date2)
    {
        LocalDate localDate1 = new LocalDate(date1);

        LocalDate localDate2 = new LocalDate(date2);

        return Days.daysBetween(localDate1, localDate2).getDays();
    }
}
