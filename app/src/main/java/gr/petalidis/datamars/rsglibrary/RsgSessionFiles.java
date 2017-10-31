package gr.petalidis.datamars.rsglibrary;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by npetalid on 31/10/17.
 */

public class RsgSessionFiles implements Serializable {

    final SimpleDateFormat format = new SimpleDateFormat("YYYY-mm-dd");
    private HashMap<String,String> sessions = new HashMap<>();
    private List<Date> dates = new ArrayList<>();

    public RsgSessionFiles(HashMap<String, String> sessions) throws ParseException {
        this.sessions = sessions;
        for (String date: sessions.keySet()) {
            dates.add(format.parse(date));
        }
        Collections.sort(dates);
    }

    public HashMap<String, String> getSessions() {
        return sessions;
    }

    public List<Date> getDates() {
        return dates;
    }
}
