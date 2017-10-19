package gr.petalidis.datamars;

import java.io.File;
import java.io.FilenameFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by npetalid on 14/10/17.
 */

public class DateScanner {

    private List<String> dates = new ArrayList<>();
    //accepts a root directory
    // traverses to find all subdirectories in the form Year/Month/Date
    //@root A root directory under which to search
    //@returns a sorted list of date objects (earlier to later

    private static final String VALID_SUFFIX = ".rsg";

    private boolean containsRsgFiles(File dayDirectory) {


        File[] rsgFiles = dayDirectory.listFiles(new FilenameFilter() {
            public boolean accept(File directory, String fileName) {
                return fileName.endsWith(VALID_SUFFIX);
            }
        });

        return rsgFiles != null && rsgFiles.length > 0;


    }

    List<Date> scanDirectory(String root) {
        File f = new File(root);
        File[] files = f.listFiles();
        List<Date> dates = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if (files != null) {
            for (File inFile : files) {
                if (inFile.isDirectory() && inFile.canRead() && DateUtility.isValidYear(inFile.getName())) {
                    String currentYear = inFile.getName();
                    File yearRoot = new File(root + File.pathSeparator + currentYear);
                    File[] monthFiles = yearRoot.listFiles();
                    if (monthFiles != null) {
                        for (File monthFile : monthFiles) {
                            if (monthFile.isDirectory() && monthFile.canRead() && DateUtility.isValidMonth(monthFile.getName())) {
                                String currentMonth = monthFile.getName();
                                File monthRoot = new File(root + File.pathSeparator + currentYear + File.pathSeparator + currentMonth);
                                File[] dayFiles = monthRoot.listFiles();
                                if (dayFiles != null) {
                                    for (File dayFile : dayFiles) {
                                        if (dayFile.isDirectory() && dayFile.canRead() && DateUtility.isValidDay(dayFile.getName())) {
                                            if (containsRsgFiles(dayFile)) {
                                                try {
                                                    dates.add(simpleDateFormat.parse(currentYear + "-" + currentMonth + "-" + dayFile.getName()));
                                                } catch (ParseException e) {
                                                    //We should never arrive here, but if we do just forget the day
                                                }
                                            }
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
