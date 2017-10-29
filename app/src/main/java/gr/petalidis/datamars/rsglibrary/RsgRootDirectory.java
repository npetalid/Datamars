package gr.petalidis.datamars.rsglibrary;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by npetalid on 29/10/17.
 */

public class RsgRootDirectory {

    private List<String> rsgRoots = new ArrayList<>();

    public RsgRootDirectory() {
        this("/storage");
    }
    public RsgRootDirectory(String startingPath) {
        rsgRoots = recurseIntoFilePath(new ArrayList<String>(),startingPath);

    }
    private List<String> recurseIntoFilePath(List<String> results, String path)
    {

        //TextView textView = (TextView) getView().findViewById(R.id.status_text_view);

        File f = new File(path.toString());
        File [] files = f.listFiles();
        if (files != null) {
            for (File inFile : files) {
                if (inFile.isDirectory() && inFile.canRead() ) {
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

    public List<String> rsgRoots() {
        return rsgRoots;
    }
}
