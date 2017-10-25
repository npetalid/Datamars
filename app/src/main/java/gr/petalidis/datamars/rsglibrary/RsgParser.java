package gr.petalidis.datamars.rsglibrary;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by npetalid on 14/10/17.
 */

public class RsgParser {

    public List<Rsg> parseRsgStrings(List<String> rsgStrings) throws ParseException {
        List<Rsg> rsgs = new ArrayList<>();
        for (String rsgString: rsgStrings) {
            rsgs.add(parseRsgString(rsgString));
        }
        return rsgs;
    }

    private Rsg parseRsgString(String rsgString) throws ParseException {

        
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
}
