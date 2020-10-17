package net.nekomura.minecraft;

import java.util.Calendar;
import java.util.TimeZone;

public class TimezoneUtils {
    /**
     * is day in timezone1 is lower than timezone2
     */
    public static boolean isLowerDay(String timezone1, String timezone2) {
        Calendar c1 = Calendar.getInstance(TimeZone.getTimeZone(timezone1));
        Calendar c2 = Calendar.getInstance(TimeZone.getTimeZone(timezone2));
        int dayInYear1 = c1.get(Calendar.DAY_OF_YEAR);
        int dayInYear2 = c2.get(Calendar.DAY_OF_YEAR);
        return (dayInYear1 < dayInYear2) || (dayInYear1 == 365 && dayInYear2 == 1) || (dayInYear1 == 366 && dayInYear2 == 1);
    }

    /**
     * is day in timezone1 is greater than timezone2
     */
    public static boolean isGreaterDay(String timezone1, String timezone2) {
        Calendar c1 = Calendar.getInstance(TimeZone.getTimeZone(timezone1));
        Calendar c2 = Calendar.getInstance(TimeZone.getTimeZone(timezone2));
        int dayInYear1 = c1.get(Calendar.DAY_OF_YEAR);
        int dayInYear2 = c2.get(Calendar.DAY_OF_YEAR);
        return (dayInYear1 > dayInYear2) || (dayInYear1 == 1 && dayInYear2 == 365) || (dayInYear1 == 1 && dayInYear2 == 366);
    }

    /**
     * day in timezone2 - day in timezone1
     */
    public static int dayBetween(String timezone1, String timezone2) {
        Calendar c1 = Calendar.getInstance(TimeZone.getTimeZone(timezone1));
        Calendar c2 = Calendar.getInstance(TimeZone.getTimeZone(timezone2));
        int dayInYear1 = c1.get(Calendar.DAY_OF_YEAR);
        int dayInYear2 = c2.get(Calendar.DAY_OF_YEAR);
        return dayInYear2 - dayInYear1;
    }

}
