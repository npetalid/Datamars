/*        Copyright 2017 Nikolaos Petalidis
*
*        Licensed under the Apache License, Version 2.0 (the "License");
*        you may not use this file except in compliance with the License.
*        You may obtain a copy of the License at
*
*        http://www.apache.org/licenses/LICENSE-2.0
*
*        Unless required by applicable law or agreed to in writing, software
*        distributed under the License is distributed on an "AS IS" BASIS,
*        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*        See the License for the specific language governing permissions and
*        limitations under the License.
*/

package gr.petalidis.datamars.rsglibrary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Set;

public class RsgExporter {

    public static String export(Set<Rsg> rsgList, String filepath, String name) throws IOException {
        if (rsgList.isEmpty()) {
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
