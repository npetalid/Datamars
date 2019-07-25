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

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class RsgSessionFiles implements Serializable {

    private List<RsgSession> sessions = new ArrayList<>();


    public RsgSessionFiles() {
            //do nothing

    }
    public RsgSessionFiles(List<RsgSession> sessions) throws ParseException {
        this.sessions = sessions;

        Collections.sort(sessions);
    }

    public List<RsgSession> getSessions() {
        return sessions;
    }

    public List<Date> getDates() {

        List<Date> dates = new ArrayList<>();
        for (RsgSession session: sessions) {
            dates.add(session.getDate());
        }
        Collections.sort(dates,Comparator.naturalOrder());
        return dates;
    }

    public String getFilename(Date date) throws ParseException
    {
        for (RsgSession session: sessions) {
            if (session.getDate().equals(date)) {
                return session.getFilepath();
            }
        }
        return "";
    }


}
