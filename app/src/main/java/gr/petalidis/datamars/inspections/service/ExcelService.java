
/*
 * Copyright 2017-2019 Nikolaos Petalidis
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package gr.petalidis.datamars.inspections.service;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import gr.petalidis.datamars.Log4jHelper;
import gr.petalidis.datamars.Moo;
import gr.petalidis.datamars.inspections.domain.AnimalType;
import gr.petalidis.datamars.inspections.domain.Entry;
import gr.petalidis.datamars.inspections.domain.Inspectee;
import gr.petalidis.datamars.inspections.domain.Inspection;
import gr.petalidis.datamars.inspections.domain.Report;

public class ExcelService {
    private static final Logger log = Log4jHelper.getLogger(ExcelService.class.getName());

    public static String export(Inspection inspection, String filepath, String name) throws IOException {

        Workbook workbook = new HSSFWorkbook();

        for (Inspectee inspectee: inspection.getProducersWithNoDummy()) {
            Sheet sheet = workbook.createSheet(inspectee.getTin() + "_" + inspectee.getName());
            Row headerRow = sheet.createRow(1);
            Cell cell = headerRow.createCell(0);
            cell.setCellValue("Παραγωγός");
            cell = headerRow.createCell(1);
            cell.setCellValue(inspectee.getName());
            cell = headerRow.createCell(2);
            cell.setCellValue("ΑΦΜ");
            cell.setCellValue(inspectee.getTin());
            Row titleRow = sheet.createRow(2);
            cell = titleRow.createCell(0);
            cell.setCellValue("Χώρα");
            cell = titleRow.createCell(1);
            cell.setCellValue("Ενώτιο");
            cell = titleRow.createCell(2);
            cell.setCellValue("Ημερομηνία");
            cell = titleRow.createCell(3);
            cell.setCellValue("Στο μητρώο;");
            cell = titleRow.createCell(4);
            cell.setCellValue("Είδος");
            cell = titleRow.createCell(5);
            cell.setCellValue("Φυλή");
            cell = titleRow.createCell(6);
            cell.setCellValue("Σχόλια");

            int j = 3;
            for (Entry entry : inspection.getEntriesFor(inspectee.getTin())) {
                Row entryRow = sheet.createRow(j++);
                cell = entryRow.createCell(0);
                cell.setCellValue(entry.getCountry());
                cell = entryRow.createCell(1);
                cell.setCellValue(entry.getTag());
                cell = entryRow.createCell(2);
                cell.setCellValue(Moo.getFormatter().format(entry.getTagDate()));
                cell = entryRow.createCell(3);
                cell.setCellValue(entry.isInRegister() ? "ΝΑΙ" : "ΟΧΙ");
                cell = entryRow.createCell(4);
                cell.setCellValue(entry.getAnimalType());
                cell = entryRow.createCell(5);
                cell.setCellValue(entry.getAnimalGenre());
                cell = entryRow.createCell(6);
                cell.setCellValue(entry.getComment().getTitle());
            }
            j++;
            Report report = inspection.generateReportFor(inspectee);
            Row entryRow = sheet.createRow(j++);
            cell = entryRow.createCell(0);
            cell.setCellValue("Καταμετρημένα ζώα");
            cell = entryRow.createCell(1);
            cell.setCellValue(report.getTotal());

            entryRow = sheet.createRow(j++);
            cell = entryRow.createCell(0);
            cell.setCellValue("Zώα χωρίς σήμανση");
            cell = entryRow.createCell(1);
            cell.setCellValue(report.getNoTag());

            entryRow = sheet.createRow(j++);
            cell = entryRow.createCell(0);
            cell.setCellValue("Zώα χωρίς σήμανση κάτω των 6 μηνών");
            cell = entryRow.createCell(1);
            cell.setCellValue(report.getNoTagUnder6());

            entryRow = sheet.createRow(j++);
            cell = entryRow.createCell(0);
            cell.setCellValue("Zώα χωρίς ηλ. σήμανση");
            cell = entryRow.createCell(1);
            cell.setCellValue(report.getNoElectronicTag());

            entryRow = sheet.createRow(j++);
            cell = entryRow.createCell(0);
            cell.setCellValue("Zώα με ένα ενώτιο");
            cell = entryRow.createCell(1);
            cell.setCellValue(report.getSingleTag());

            entryRow = sheet.createRow(j++);
            cell = entryRow.createCell(0);
            cell.setCellValue("Καταμετρηθέντα ζώα με σήμανση που δεν αναγράφονται στο μητρώο");
            cell = entryRow.createCell(1);
            cell.setCellValue(report.getCountedButNotInRegistry());

            entryRow = sheet.createRow(j++);
            cell = entryRow.createCell(0);
            cell.setCellValue("Επιλέξιμα ζώα:");
            Map<AnimalType, Long> selectables = report.getSelectable();
            for (AnimalType animalType: selectables.keySet())
            {
                entryRow = sheet.createRow(j++);
                cell = entryRow.createCell(0);
                cell.setCellValue(animalType.getTitle());
                cell = entryRow.createCell(1);
                cell.setCellValue(selectables.get(animalType));
            }

            //          `

        }

        File file = new File(filepath, name);


        if (file.exists()) {
            boolean delete = file.delete();
            if (!delete) {
                log.error("File with same name already present that I could not overwrite:" + file.getAbsolutePath());
                throw new IOException("File with same name already present that I could not overwrite");
            }
        }
        if (file.createNewFile()) {
            try (
                    FileOutputStream fOut = new FileOutputStream(file)
            ) {
                workbook.write(fOut);
            }
            return file.getAbsolutePath();

        }
        log.error("Could not write file " + file.getAbsolutePath());
        throw new IOException("Could not write file " + file.getAbsolutePath());
    }

    public static void copyFilesToDirectory(List<String> filepaths, String destinationDirectory) throws IOException {
        File directory = new File(destinationDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
            filepaths.forEach(x -> {
                File source = new File(x);

                String destinationPath = destinationDirectory + "/" + source.getName();
                File destination = new File(destinationPath);

                try {
                    FileUtils.copyFile(source, destination);
                } catch (IOException e) {
                    log.error("Unable to copy image " + x + " to " + destinationDirectory);
                    throw new RuntimeException("Unable to copy image " + x + " to " + destinationDirectory);
                }

            });
        } catch (RuntimeException e) {
            log.error("copyFilesToDirectory() " + e.getLocalizedMessage());
            throw new IOException(e.getMessage());
        }

    }
}
