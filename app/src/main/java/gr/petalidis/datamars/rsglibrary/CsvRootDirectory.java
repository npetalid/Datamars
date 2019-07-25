package gr.petalidis.datamars.rsglibrary;

import android.os.Environment;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import gr.petalidis.datamars.Log4jHelper;
import gr.petalidis.datamars.Moo;
import gr.petalidis.datamars.inspections.ui.InspectionStepTwoActivity;

/**
 * Created by npetalid on 19/11/17.
 */

public class CsvRootDirectory {
    private final Logger log = Log4jHelper.getLogger(CsvRootDirectory.class.getName());

    private String directory;
    public CsvRootDirectory() {
        this.directory =  Moo.getAppContext().getExternalFilesDir("")
                .getAbsolutePath() + File.separator + "GES3S";

        if (!isExternalStorageReadable() || !isExternalStorageWritable()) {
            log.error("Could not write to external storage: " + this.directory);
            throw new IllegalStateException("Could not write to external storage");
        }
    }

    public ArrayList<String> getProbableUsbs(String currentUsbName) {

        Set<String> usbs = new HashSet<>();

        File topDirectory = new File(directory);

        File[] files = topDirectory.listFiles();
        if (files != null) {
            for (File inFile : files) {
                if (inFile.isDirectory() && inFile.canRead()) {
                    usbs.add(inFile.getName());
                }
            }
        }
        if (currentUsbName!=null && !currentUsbName.isEmpty()) {
            usbs.add(currentUsbName);
        }
        ArrayList<String> usbList = new ArrayList<>();
        usbList.addAll(usbs);
        return usbList;

    }

    public String getDirectory() {
        return directory;
    }

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }
}
