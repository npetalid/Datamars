package gr.petalidis.datamars.rsglibrary;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by npetalid on 7/11/17.
 */

public class RsgSession implements Comparable<RsgSession>, Serializable {
    private Date date;
    private String filepath;
    final static private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public RsgSession(String filename, String filepath) throws ParseException
    {
        String dateString = filename.replace(".csv","");
        this.date = formatter.parse(dateString);
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
        return formatter.format(date)+".csv";
    }

    public static boolean isValidRsgCsvSessionFile(String name)
    {
       boolean hasProperSuffix = name.endsWith(".csv");
       String nameWithoutSuffix = name.replace(".csv","");
        try {
            Date date = formatter.parse(nameWithoutSuffix);
            return hasProperSuffix;
        } catch (ParseException e) {
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
        int result = getDate().hashCode();
        return result;
    }

    @Override
    public int compareTo(@NonNull RsgSession o) {
        return this.getDate().compareTo(o.getDate());
    }
}
