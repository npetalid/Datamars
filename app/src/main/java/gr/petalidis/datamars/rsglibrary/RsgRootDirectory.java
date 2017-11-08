package gr.petalidis.datamars.rsglibrary;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by npetalid on 29/10/17.
 */

public class RsgRootDirectory {

    private String rsgRoot;

    private String usbName = "";

    private String csvPath = "";
    public RsgRootDirectory() {
        this("/storage");
    }

    public RsgRootDirectory(String startingPath) throws IllegalStateException {

        List<String> rsgRoots = recurseIntoFilePath(new ArrayList<String>(), startingPath);
        if (rsgRoots.isEmpty() || rsgRoots.size() > 1) {
            throw new IllegalStateException("Could not find a connected Datamars device");
        }

        rsgRoot = rsgRoots.get(0);

        usbName = calcUsbName(rsgRoot);

        csvPath = calcCsvPath(usbName);
    }

    private List<String> recurseIntoFilePath(List<String> results, String path) {

        //TextView textView = (TextView) getView().findViewById(R.id.status_text_view);

        File f = new File(path.toString());
        File[] files = f.listFiles();
        if (files != null) {
            for (File inFile : files) {
                if (inFile.isDirectory() && inFile.canRead()) {
                    if (inFile.getName().endsWith("Session")) {
                        results.add(inFile.getAbsolutePath());
                        return results;
                    }
                    recurseIntoFilePath(results, inFile.getAbsolutePath());
                }
            }

        }
        return results;
    }

    public String rsgRoot() {
        return rsgRoot;
    }

    public String getUsbName() {
        return usbName;
    }

    public static String getExternalStoragePath()
    {

        String m_str = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + "GS232";

        if (!isExternalStorageReadable() || !isExternalStorageWritable()) {
            throw new IllegalStateException("Could not write to external storage");
        }
        return m_str;
    }
    public static String getCsvPath(String usbName)
    {
        return getExternalStoragePath() + File.separator + usbName;
    }
    public ArrayList<String> getProbableUsbs() {

        Set<String> usbs = new HashSet<>();

        File topDirectory = new File(getExternalStoragePath());

        File[] files = topDirectory.listFiles();
        if (files != null) {
            for (File inFile : files) {
                if (inFile.isDirectory() && inFile.canRead()) {
                    usbs.add(inFile.getName());
                }
            }
        }
        usbs.add(getUsbName());
        ArrayList<String> usbList = new ArrayList<>();
        usbList.addAll(usbs);
        return usbList;

    }
    public String getCsvDirectory() {
        return csvPath;
    }

    private String calcUsbName(String usbPath)
    {
        String name = usbPath.replace("Session", "");

        String[] usbNames = name.split(File.separator);
        if (usbNames.length > 0) {
            name = usbNames[usbNames.length - 1];
        } else {
            name = "";
        }
        return name;
    }

    private String calcCsvPath(String usbName) throws  IllegalStateException{
        String m_str = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + "GS232";

        if (!isExternalStorageReadable() || !isExternalStorageWritable()) {
            throw new IllegalStateException("Could not write to external storage");
        }

        File topDirectory = new File(m_str);

        if (!topDirectory.exists()) {
            topDirectory.mkdir();
        }
        File datamarsDir = new File(m_str + File.separator + usbName);
        if (!datamarsDir.exists()) {
            datamarsDir.mkdir();
        }

        return datamarsDir.getAbsolutePath();
    }
    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
