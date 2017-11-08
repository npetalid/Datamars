package gr.petalidis.datamars.rsglibrary;

import java.io.File;
import java.io.FilenameFilter;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by npetalid on 14/10/17.
 */
public class RsgSessionScanner {

    private static final String VALID_SUFFIX = ".rsg";
    private static final String VALID_PREFIX = "session_";

    private static boolean containsRsgFiles(File monthDirectory) {

        File[] rsgFiles = monthDirectory.listFiles(new FilenameFilter() {
            public boolean accept(File directory, String fileName) {
                return fileName.endsWith(VALID_SUFFIX) && fileName.startsWith(VALID_PREFIX);
            }
        });

        return rsgFiles != null && rsgFiles.length > 0;

    }


    //accepts a root directory
    //traverses to find all subdirectories in the form Year/Month/Date
    //@param root A root directory under which to search
    //@returns a map of dates to files objects 

    public static RsgSessionFiles scanDirectory(String root) throws ParseException {
        File f = new File(root);
        File[] files = f.listFiles();

        RsgSessionFiles sessionFiles = new RsgSessionFiles();
        if (files != null) {
            for (File inFile : files) {
                if (inFile.isDirectory() && inFile.canRead() && RsgFileUtility.isValidYear(inFile.getName().trim())) {
                    String currentYear = inFile.getName();
                    File yearRoot = new File(root + File.separator + currentYear);
                    File[] monthFiles = yearRoot.listFiles();
                    if (monthFiles != null) {
                        for (File monthFile : monthFiles) {
                            if (monthFile.isDirectory() && monthFile.canRead() && RsgFileUtility.isValidMonth(monthFile.getName().trim()) && containsRsgFiles(monthFile)) {
                                String currentMonth = monthFile.getName();
                                File monthRoot = new File(root + File.separator + currentYear + File.separator + currentMonth);
                                File[] dayFiles = monthRoot.listFiles();
                                if (dayFiles != null) {
                                    for (File dayFile : dayFiles) {
                                        String day = RsgFileUtility.getDate(dayFile.getName().trim());
                                        if (dayFile.canRead() && RsgFileUtility.isValidDay(day) && RsgFileUtility.isValidFilename(dayFile.getName())) {
                                            sessionFiles.getSessions().add(new RsgSession(currentYear + "-" + currentMonth + "-" + day, dayFile.getAbsolutePath()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
        return sessionFiles;
    }

    public static RsgSessionFiles scanUsbDirectory(String root) throws ParseException {
        RsgSessionFiles sessionFiles = new RsgSessionFiles();
        File f = new File(root);
        File[] files = f.listFiles();

        if (files != null) {
            for (File inFile : files) {
                if (inFile.canRead() && RsgSession.isValidRsgCsvSessionFile(inFile.getName())) {
                    sessionFiles.getSessions().add(new RsgSession(inFile.getName(), inFile.getAbsolutePath()));
                }
            }
        }
        return sessionFiles;
    }

}
