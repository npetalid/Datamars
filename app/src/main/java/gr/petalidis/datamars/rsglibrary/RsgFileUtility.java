package gr.petalidis.datamars.rsglibrary;

import java.util.Calendar;

/**
 * Created by npetalid on 14/10/17.
 */

public class RsgFileUtility {

    private final static int MINIMUM_YEAR = 2000;

    private static final String VALID_SUFFIX = ".rsg";
    private static final String VALID_PREFIX = "session_";


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
        try {
            int monthInt = Integer.parseInt(month);
            return 0 <= monthInt && monthInt<=12;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidDay(String day) {
        try {
            int dayInt = Integer.parseInt(day);
            return 1 <= dayInt && dayInt <= 31;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidFilename(String rsgFilename)
    {
        return rsgFilename.endsWith(VALID_SUFFIX);
    }

    public static String getDate(String rsgFilename) {
        String dateSuffix = "MMYYYY";

        String date = rsgFilename.replaceFirst(VALID_PREFIX, "").replaceFirst(VALID_SUFFIX, "");

        return date.substring(0, date.length() - dateSuffix.length());

    }
}
