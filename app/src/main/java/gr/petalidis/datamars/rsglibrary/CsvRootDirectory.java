
/*
 * Copyright 2017-2019 Nikolaos Petalidis
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package gr.petalidis.datamars.rsglibrary;

import android.os.Environment;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import gr.petalidis.datamars.Log4jHelper;
import gr.petalidis.datamars.Moo;

/**
 * Created by npetalid on 19/11/17.
 */

public class CsvRootDirectory {

    private final String directory;
    public CsvRootDirectory() {
        this.directory =  Moo.getAppContext().getExternalFilesDir("")
                .getAbsolutePath() + File.separator + "GES3S";

        if (!isExternalStorageReadable() || !isExternalStorageWritable()) {
            Logger log = Log4jHelper.getLogger(CsvRootDirectory.class.getName());
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
        ArrayList<String> usbList = new ArrayList<>(usbs);
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
