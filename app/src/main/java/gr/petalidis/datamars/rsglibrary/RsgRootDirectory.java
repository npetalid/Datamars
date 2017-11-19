/*        Copyright 2017 Nikolaos Petalidis
*
*        Licensed under the Apache License, Version 2.0 (the "License");
*        you may not use this file except in compliance with the License.
*        You may obtain a copy of the License at
*
*        http://www.apache.org/licenses/LICENSE-2.0
*
*        Unless required by applicable law or agreed to in writing, software
*        distributed under the License is distributed on an "AS IS" BASIS,
*        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*        See the License for the specific language governing permissions and
*        limitations under the License.
*/

package gr.petalidis.datamars.rsglibrary;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RsgRootDirectory {

    private CsvRootDirectory csvRootDirectory = new CsvRootDirectory();
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

        File topDirectory = new File(csvRootDirectory.getDirectory());

        if (!topDirectory.exists()) {
            boolean mkdir = topDirectory.mkdir();
            if (mkdir == false) {
                throw new IllegalStateException("Could not create top level directory");
            }
        }
        File datamarsDir = new File(csvRootDirectory.getDirectory() + File.separator + usbName);
        if (!datamarsDir.exists()) {
            boolean mkDir = datamarsDir.mkdir();
            if (mkDir == false) {
                throw new IllegalStateException("Could not create device-differentiating directory");
            }
        }

        return datamarsDir.getAbsolutePath();
    }

}
