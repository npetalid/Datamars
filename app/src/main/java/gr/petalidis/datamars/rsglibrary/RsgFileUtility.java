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

import android.util.Log;

import org.apache.log4j.Logger;

import java.util.Calendar;

import gr.petalidis.datamars.Log4jHelper;
import gr.petalidis.datamars.SessionViewer;

class RsgFileUtility {

    private static final Logger log = Log4jHelper.getLogger(RsgFileUtility.class.getName());
    private final static int MINIMUM_YEAR = 2000;

    private static final String VALID_SUFFIX = ".rsg";
    private static final String VALID_PREFIX = "session_";


    static boolean isValidYear(String year)
    {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        try {
            int yearInt = Integer.parseInt(year);
            return MINIMUM_YEAR < yearInt && yearInt<=currentYear;
        } catch (NumberFormatException e) {
            log.error("Received not Valid year: "  + year +", " + e.getLocalizedMessage());
            return false;
        }
    }

    static boolean isValidMonth(String month)
    {
        try {
            int monthInt = Integer.parseInt(month);
            return 0 <= monthInt && monthInt<=12;
        } catch (NumberFormatException e) {
            log.error("Received not Valid month: "  + month +", " + e.getLocalizedMessage());
            return false;
        }
    }

     static boolean isValidDay(String day) {
        try {
            int dayInt = Integer.parseInt(day);
            return 1 <= dayInt && dayInt <= 31;
        } catch (NumberFormatException e) {
            log.error("Received not Valid day: "  + day +", " + e.getLocalizedMessage());
            return false;
        }
    }

     static boolean isValidFilename(String rsgFilename)
    {
        return rsgFilename.endsWith(VALID_SUFFIX);
    }

     static String getDate(String rsgFilename) {
        String dateSuffix = "MMYYYY";

        String date = rsgFilename.replaceFirst(VALID_PREFIX, "").replaceFirst(VALID_SUFFIX, "").trim();

        return date.substring(0, date.length() - dateSuffix.length());

    }
}
