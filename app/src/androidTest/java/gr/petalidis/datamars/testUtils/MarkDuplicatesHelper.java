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

package gr.petalidis.datamars.testUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import gr.petalidis.datamars.inspections.domain.Inspectee;
import gr.petalidis.datamars.rsglibrary.CsvRootDirectory;
import gr.petalidis.datamars.rsglibrary.Rsg;
import gr.petalidis.datamars.rsglibrary.RsgExporter;

public class MarkDuplicatesHelper {
    public static class Result {
        private String tag;
        private boolean inRegister;
        private String comment;
        private Date date;

        public Result(String tag, Date date, boolean inRegister, String comment) {
            this.tag = tag;
            this.date = date;
            this.inRegister = inRegister;
            this.comment = comment;
        }

        public String getTag() {
            return tag;
        }

        public Date getDate() {
            return date;
        }

        public boolean isInRegister() {
            return inRegister;
        }

        public String getComment() {
            return comment;
        }
    }
    private static List<Rsg> rsgsArray;

    public final static String USB_NAME = "testDevice";
    public final static String TEST_FILE_NAME = "2018-02-22.csv";
    public final static Inspectee inspectee = new Inspectee("127137474","Pappas");
    static {
        try {
            rsgsArray = Arrays.asList(
                    new Rsg("01234560300062037570054", "22022018", "091614"),
                    new Rsg("01234560300062037570071", "22022018", "091715"),
                    new Rsg("01234560300062037570054", "22022018", "091615"),
                    new Rsg("01234560300062037570012", "22022018", "091917"),
                    new Rsg("01234560300062037570021", "22022018", "092018"),
                    new Rsg("01234560300062037570012", "22022018", "091921"),
                    new Rsg("01234560300062037570012", "22022018", "091922"));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static List<Result> results() {
        Result one = new Result(rsgsArray.get(0).getIdentificationCode(),rsgsArray.get(0).getDate(),true,"");
        Result two = new Result(rsgsArray.get(1).getIdentificationCode(),rsgsArray.get(1).getDate(),true,"");
        Result three = new Result(rsgsArray.get(2).getIdentificationCode(),rsgsArray.get(2).getDate(),false,"ΛΘΣ");
        Result four = new Result(rsgsArray.get(3).getIdentificationCode(),rsgsArray.get(3).getDate(),true,"");
        Result five = new Result(rsgsArray.get(4).getIdentificationCode(),rsgsArray.get(4).getDate(),true,"");
        Result six = new Result(rsgsArray.get(5).getIdentificationCode(),rsgsArray.get(5).getDate(),false,"ΔΠΛ");
        Result seven = new Result(rsgsArray.get(6).getIdentificationCode(),rsgsArray.get(6).getDate(),false,"ΛΘΣ");

        return Arrays.asList(one,two,three,four,five,six,seven);
    }
    public static String saveRsg() throws IOException {
        CsvRootDirectory csvRootDirectory = new CsvRootDirectory();
        File datamarsDir = new File(csvRootDirectory.getDirectory() + File.separator + USB_NAME);
        if (!datamarsDir.exists()) {
            boolean mkDir = datamarsDir.mkdir();
            if (!mkDir) {
                throw new IllegalStateException("Could not create device-differentiating directory");
            }
        }
        return RsgExporter.export(  new HashSet<>(rsgsArray), datamarsDir.getAbsolutePath(), TEST_FILE_NAME);
    }

}
