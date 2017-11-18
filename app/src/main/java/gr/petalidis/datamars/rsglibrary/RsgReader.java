package gr.petalidis.datamars.rsglibrary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by npetalid on 29/10/17.
 */

public class RsgReader {


    public static List<Rsg> readRsgFromScanner(String filepath) throws FileNotFoundException, IOException, ParseException {

        File file = new File(filepath);
        List<Rsg> rsgs = new ArrayList<>();
        if (file != null) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    Rsg rsg = RsgParser.parseRsgString(line);
                    rsgs.add(rsg);
                }
            }
        }
        return rsgs;
    }

    public static ArrayList<Rsg> readRsgFromTablet(String filepath) throws FileNotFoundException, IOException, ParseException {

        File file = new File(filepath);
        ArrayList<Rsg> rsgs = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                Rsg rsg = RsgParser.parseCsvString(line);
                rsgs.add(rsg);
            }
        }
        return rsgs;
    }
}
