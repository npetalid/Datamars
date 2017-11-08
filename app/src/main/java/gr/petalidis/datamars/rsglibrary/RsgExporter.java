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

    public static String export(List<Rsg> rsgList, String filepath, String name) throws IOException {
        if (rsgList.isEmpty()) {
            return "";
        }

         File file = new File(filepath,name);


        if (file.exists()) {
            file.delete();
        }
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
            return file.getAbsolutePath();

        }

        throw new IOException("Could not write file " + file.getAbsolutePath());
    }
}
