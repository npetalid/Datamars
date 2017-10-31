package gr.petalidis.datamars.rsglibrary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by npetalid on 14/10/17.
 */

public class RsgParser {

    public static List<Rsg> parseRsgStrings(List<String> rsgStrings) throws ParseException {
        List<Rsg> rsgs = new ArrayList<>();
        for (String rsgString: rsgStrings) {
            rsgs.add(parseRsgString(rsgString));
        }
        return rsgs;
    }

    public static Rsg parseRsgString(String rsgString) throws ParseException {

        
        String []parts = rsgString.split("\\|");

        if (parts.length!=3 ) {
            throw new IllegalArgumentException("Parts not divided by | character");
        }

        if (!parts[0].startsWith("[") && !parts[2].endsWith("]")) {
            throw new IllegalArgumentException("Parts not starting/ending with []");
        }

        parts[0] = parts[0].substring(1);
        parts[2] = parts[2].substring(0,parts[2].length()-1);
        return new Rsg(parts[0],parts[1],parts[2]);
    }

    public static Rsg parseCsvString(String rsgString) throws ParseException {


        String []parts = rsgString.split(",");

        if (parts.length!=3 ) {
            throw new IllegalArgumentException("Parts not divided by , character");
        }
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hms");
        Date date = format.parse(parts[2]);
        return new Rsg(parts[0],parts[1],date);
    }
}
