package gr.petalidis.datamars.rsglibrary;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * Created by npetalid on 14/10/17.
 */
public class DateScanner {

    private static final String VALID_SUFFIX = ".rsg";
    private static final String VALID_PREFIX = "session_";

    private boolean containsRsgFiles(File monthDirectory) {

        File[] rsgFiles = monthDirectory.listFiles(new FilenameFilter() {
            public boolean accept(File directory, String fileName) {
                return fileName.endsWith(VALID_SUFFIX) && fileName.startsWith(VALID_PREFIX);
            }
        });

        return rsgFiles != null && rsgFiles.length > 0;

    }

    private boolean isValidFilename(String rsgFilename)
    {
        return rsgFilename.endsWith(VALID_SUFFIX);
    }
    private String getDate(String rsgFilename) {
        String dateSuffix = "MMYYYY";

        String date = rsgFilename.replaceFirst(VALID_PREFIX, "").replaceFirst(VALID_SUFFIX, "");

        return date.substring(0, date.length() - dateSuffix.length());

    }
    //accepts a root directory
    //traverses to find all subdirectories in the form Year/Month/Date
    //@param root A root directory under which to search
    //@returns a map of dates to files objects 

    public HashMap<String, String> scanDirectory(String root) {
        File f = new File(root);
        File[] files = f.listFiles();
        HashMap<String, String> dates = new HashMap<>();

        if (files != null) {
            for (File inFile : files) {
                if (inFile.isDirectory() && inFile.canRead() && DateUtility.isValidYear(inFile.getName())) {
                    String currentYear = inFile.getName();
                    File yearRoot = new File(root + File.separator + currentYear);
                    File[] monthFiles = yearRoot.listFiles();
                    if (monthFiles != null) {
                        for (File monthFile : monthFiles) {
                            if (monthFile.isDirectory() && monthFile.canRead() && DateUtility.isValidMonth(monthFile.getName()) && containsRsgFiles(monthFile)) {
                                String currentMonth = monthFile.getName();
                                File monthRoot = new File(root + File.separator + currentYear + File.separator + currentMonth);
                                File[] dayFiles = monthRoot.listFiles();
                                if (dayFiles != null) {
                                    for (File dayFile : dayFiles) {
                                        String day = getDate(dayFile.getName());
                                        if (dayFile.canRead() && DateUtility.isValidDay(day) && isValidFilename(dayFile.getName())) {
                                            dates.put(currentYear + "-" + currentMonth + "-" + day, dayFile.getAbsolutePath());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
        return dates;
    }

}
