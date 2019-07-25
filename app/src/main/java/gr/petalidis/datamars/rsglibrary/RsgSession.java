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

import android.support.annotation.NonNull;
import android.util.Log;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import gr.petalidis.datamars.Log4jHelper;
import gr.petalidis.datamars.Moo;
import gr.petalidis.datamars.SessionViewer;

public class RsgSession implements Comparable<RsgSession>, Serializable {
    private Date date;
    private String filepath;
    private static final Logger log = Log4jHelper.getLogger(RsgSession.class.getName());

    RsgSession(String filename, String filepath) throws ParseException
    {

        String dateString = filename.replace(".csv","");
        this.date = Moo.getFormatter().parse(dateString);
        this.filepath = filepath;

    }
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public boolean isValidRsgSessionFile(String name)
    {
        return true;
    }

    public String getCsvName() {
        return Moo.getFormatter().format(date)+".csv";
    }

    static boolean isValidRsgCsvSessionFile(String name)
    {
       boolean hasProperSuffix = name.endsWith(".csv");
       String nameWithoutSuffix = name.replace(".csv","");
        try {
            Date date = Moo.getFormatter().parse(nameWithoutSuffix);
            return hasProperSuffix;
        } catch (ParseException e) {
            log.error("Received not valid session file name: "  + name +", " + e.getLocalizedMessage());
            return false;
        }

    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RsgSession that = (RsgSession) o;

        return (getDate().equals(that.getDate()));
    }

    @Override
    public int hashCode() {
        return getDate().hashCode();
    }

    @Override
    public int compareTo(@NonNull RsgSession o) {
        return this.getDate().compareTo(o.getDate());
    }
}
