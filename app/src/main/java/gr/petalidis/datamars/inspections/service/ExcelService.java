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

import gr.petalidis.datamars.Log4jHelper;
import gr.petalidis.datamars.inspections.domain.Animals;
import gr.petalidis.datamars.inspections.domain.Entry;
import gr.petalidis.datamars.inspections.domain.Inspectee;
import gr.petalidis.datamars.inspections.domain.Inspection;
import gr.petalidis.datamars.inspections.domain.OtherEntry;

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
                cell.setCellValue(entry.getTagDate());
                cell = entryRow.createCell(3);
                cell.setCellValue(entry.isInRegister() ? "ΝΑΙ" : "ΟΧΙ");
                cell = entryRow.createCell(4);
                cell.setCellValue(entry.getAnimalType());
                cell = entryRow.createCell(5);
                cell.setCellValue(entry.getAnimalGenre());
                cell = entryRow.createCell(6);
                cell.setCellValue(entry.getComment());
            }
            j++;
            Row entryRow = sheet.createRow(j++);
            cell = entryRow.createCell(0);
            cell.setCellValue("Καταμετρημένα ζώα χωρίς ηλ. σήμανση");
            for (OtherEntry otherEntry: inspection.getConventionalTotalFor(inspectee)) {
                entryRow = sheet.createRow(j++);
                cell = entryRow.createCell(0);
                cell.setCellValue(otherEntry.getAnimal());
                cell = entryRow.createCell(1);
                cell.setCellValue(otherEntry.getCount());
            }
            j++;
            entryRow = sheet.createRow(j++);
            cell = entryRow.createCell(0);
            cell.setCellValue("Καταμετρημένα ζώα εν ζωή χωρίς ηλ. σήμανση που βρέθηκαν στο μητρώο");
            for (OtherEntry otherEntry: inspection.getConventionalInRegisterFor(inspectee)) {
                entryRow = sheet.createRow(j++);
                cell = entryRow.createCell(0);
                cell.setCellValue(otherEntry.getAnimal());
                cell = entryRow.createCell(1);
                cell.setCellValue(otherEntry.getCount());
            }
            j++;
            entryRow = sheet.createRow(j++);
            cell = entryRow.createCell(0);
            cell.setCellValue("Συνολικά αποτελέσματα");
            headerRow = sheet.createRow(j++);
            cell = headerRow.createCell(0);
            cell.setCellValue("Είδος");
            cell = headerRow.createCell(1);
            cell.setCellValue("Καταμετρηθέντα");
            cell = headerRow.createCell(2);
            cell.setCellValue("Αναγραφόμενα στο μητρώο");
            headerRow = sheet.createRow(j++);
            cell = headerRow.createCell(0);
            cell.setCellValue(Animals.SHEEP_ANIMAL);
            cell = headerRow.createCell(1);
            cell.setCellValue(inspection.getCount(inspectee.getTin(),Animals.SHEEP_ANIMAL));
            cell = headerRow.createCell(2);
            cell.setCellValue(inspection.getInRegisterCount(inspectee.getTin(),Animals.SHEEP_ANIMAL));
            headerRow = sheet.createRow(j++);
            cell = headerRow.createCell(0);
            cell.setCellValue(Animals.LAMB_ANIMAL);
            cell = headerRow.createCell(1);
            cell.setCellValue(inspection.getCount(inspectee.getTin(),Animals.LAMB_ANIMAL));
            cell = headerRow.createCell(2);
            cell.setCellValue(inspection.getInRegisterCount(inspectee.getTin(),Animals.LAMB_ANIMAL));
            headerRow = sheet.createRow(j++);
            cell = headerRow.createCell(0);
            cell.setCellValue(Animals.RAM_ANIMAL);
            cell = headerRow.createCell(1);
            cell.setCellValue(inspection.getCount(inspectee.getTin(),Animals.RAM_ANIMAL));
            cell = headerRow.createCell(2);
            cell.setCellValue(inspection.getInRegisterCount(inspectee.getTin(),Animals.RAM_ANIMAL));
            headerRow = sheet.createRow(j++);
            cell = headerRow.createCell(0);
            cell.setCellValue(Animals.GOAT_ANIMAL);
            cell = headerRow.createCell(1);
            cell.setCellValue(inspection.getCount(inspectee.getTin(),Animals.GOAT_ANIMAL));
            cell = headerRow.createCell(2);
            cell.setCellValue(inspection.getInRegisterCount(inspectee.getTin(),Animals.GOAT_ANIMAL));
            headerRow = sheet.createRow(j++);
            cell = headerRow.createCell(0);
            cell.setCellValue(Animals.KID_ANIMAL);
            cell = headerRow.createCell(1);
            cell.setCellValue(inspection.getCount(inspectee.getTin(),Animals.KID_ANIMAL));
            cell = headerRow.createCell(2);
            cell.setCellValue(inspection.getInRegisterCount(inspectee.getTin(),Animals.KID_ANIMAL));
            headerRow = sheet.createRow(j++);
            cell = headerRow.createCell(0);
            cell.setCellValue(Animals.HEGOAT_ANIMAL);
            cell = headerRow.createCell(1);
            cell.setCellValue(inspection.getCount(inspectee.getTin(),Animals.HEGOAT_ANIMAL));
            cell = headerRow.createCell(2);
            cell.setCellValue(inspection.getInRegisterCount(inspectee.getTin(),Animals.HEGOAT_ANIMAL));
            headerRow = sheet.createRow(j++);
            cell = headerRow.createCell(0);
            cell.setCellValue("Σύνολο");
            cell = headerRow.createCell(1);
            cell.setCellValue(inspection.getCount(inspectee.getTin()));
            cell = headerRow.createCell(2);
            cell.setCellValue(inspection.getInRegisterCount(inspectee.getTin()));
            headerRow = sheet.createRow(j++);
            cell = headerRow.createCell(0);
            cell.setCellValue(Animals.HORSE_ANIMAL);
            cell = headerRow.createCell(1);
            cell.setCellValue(inspection.getCount(inspectee.getTin(),Animals.HORSE_ANIMAL));
            cell = headerRow.createCell(2);
            cell.setCellValue(inspection.getInRegisterCount(inspectee.getTin(),Animals.HORSE_ANIMAL));

            entryRow = sheet.createRow(j++);
            cell = entryRow.createCell(0);
            cell.setCellValue("Ζώα με μονό ενώτιο:");
            cell = entryRow.createCell(1);
            cell.setCellValue(inspection.getUniqueTag(inspectee.getTin()));
//            sheet.autoSizeColumn(0);
//            sheet.autoSizeColumn(1);
//            sheet.autoSizeColumn(2);
//            sheet.autoSizeColumn(3);
//            sheet.autoSizeColumn(4);
//            sheet.autoSizeColumn(5);
//            sheet.autoSizeColumn(6);

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
                    FileOutputStream fOut = new FileOutputStream(file);
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
