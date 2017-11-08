package gr.petalidis.datamars.rsglibrary;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by npetalid on 5/11/17.
 */

public class SessionName {
    final static public SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    private String filename;
    private Date date;

    public SessionName(String filename, Date date) {
        this.filename = filename;
        this.date = date;
    }

    public String getFilename() {
        return filename;
    }

    public Date getDate() {
        return date;
    }
}
