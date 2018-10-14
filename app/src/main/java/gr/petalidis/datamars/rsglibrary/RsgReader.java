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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class RsgReader {


    public static Set<Rsg> readRsgFromScanner(String filepath) throws IOException, ParseException {

        File file = new File(filepath);
        Set<Rsg> rsgs = new HashSet<>();
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

    public static ArrayList<Rsg> readRsgFromTablet(String filepath) throws IOException, ParseException {

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
