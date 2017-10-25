package gr.petalidis.datamars.rsglibrary;

import java.util.Calendar;

/**
 * Created by npetalid on 14/10/17.
 */

public class DateUtility {

    private final static int MINIMUM_YEAR = 2000;

    public static boolean isValidYear(String year)
    {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        try {
            int yearInt = Integer.parseInt(year);
            return MINIMUM_YEAR < yearInt && yearInt<=currentYear;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidMonth(String month)
    {
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        try {
            int monthInt = Integer.parseInt(month);
            return 0 <= monthInt && monthInt<=currentMonth;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidDay(String day) {
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        try {
            int dayInt = Integer.parseInt(day);
            return 1 <= dayInt && dayInt <= currentDay;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
