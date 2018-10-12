package gr.petalidis.datamars.inspections.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Set;

import gr.petalidis.datamars.rsglibrary.Rsg;

public class FileService {
    public static String export(List<String> strings, String filepath, String name) throws IOException {
        if (strings.isEmpty()) {
            return "";
        }

        File file = new File(filepath,name);


        if (file.exists()) {
            boolean delete = file.delete();
            if (!delete) {
                throw new IOException("File with same name already present that I could not overwrite");
            }
        }
        if (file.createNewFile()) {
            try(
                    FileOutputStream fOut = new FileOutputStream(file);
                    OutputStreamWriter writer = new OutputStreamWriter(fOut)
            ) {
                for (String line : strings) {
                    writer.append(line);
                    writer.append("\n");
                }
            }
            return file.getAbsolutePath();

        }

        throw new IOException("Could not write file " + file.getAbsolutePath());
    }
}
