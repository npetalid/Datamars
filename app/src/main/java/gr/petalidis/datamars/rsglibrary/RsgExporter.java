package gr.petalidis.datamars.rsglibrary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Created by npetalid on 24/10/17.
 */

public class RsgExporter {

    public void export(List<Rsg> rsgList, String filepath) throws IOException {
        if (rsgList.isEmpty()) {
            return;
        }

        //get list's date. Assume it is the first one

        Rsg firstRsg = rsgList.get(0);
         File file = new File(filepath,firstRsg.getName()+".csv");


        if (file.createNewFile()) {
            try(
                FileOutputStream fOut = new FileOutputStream(file);
                OutputStreamWriter writer = new OutputStreamWriter(fOut);
            ) {
                for (Rsg rsg : rsgList) {
                    writer.append(rsg.toString());
                    writer.append("\n");
                }
            } 
        }
    }
}
