/*
     Copyright 2017 Nikolaos Petalidis
     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.
 */


package gr.petalidis.datamars.rsglibrary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class RsgParser {

    static List<Rsg> parseRsgStrings(List<String> rsgStrings) throws ParseException {
        List<Rsg> rsgs = new ArrayList<>();
        for (String rsgString: rsgStrings) {
            rsgs.add(parseRsgString(rsgString));
        }
        return rsgs;
    }

    static Rsg parseRsgString(String rsgString) throws ParseException {

        
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

    static Rsg parseCsvString(String rsgString) throws ParseException {


        String []parts = rsgString.split(",");

        if (parts.length!=3 ) {
            throw new IllegalArgumentException("Parts not divided by , character");
        }
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hms");
        Date date = format.parse(parts[2]);
        return new Rsg(parts[0],parts[1],date);
    }
}
