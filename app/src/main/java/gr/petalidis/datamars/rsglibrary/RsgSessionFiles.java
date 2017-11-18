package gr.petalidis.datamars.rsglibrary;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by npetalid on 31/10/17.
 */

public class RsgSessionFiles implements Serializable {

    final private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

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
