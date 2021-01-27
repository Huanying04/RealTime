package net.nekomura.minecraft.realtime;

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
        int between = dayInYear2 - dayInYear1;

        //可能相隔一年
        if (between == 1 - 365 || between == 1 - 366) {  //timezone1為第二年的一日，timezone2為第一年最後一日，後減前為-1
            between = -1;
        }else if (between == 365 - 1 || between == 366 - 1) {  //timezone1為第一年最後一日，timezone2為第二年的一日，後減前為1
            between = 1;
        }

        return between;
    }

}