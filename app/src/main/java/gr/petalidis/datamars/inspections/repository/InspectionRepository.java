package gr.petalidis.datamars.inspections.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import gr.petalidis.datamars.inspections.domain.Inspection;
import gr.petalidis.datamars.inspections.dto.InspectionDateProducer;
import gr.petalidis.datamars.inspections.exceptions.PersistenceException;

public class InspectionRepository {


    public static Inspection save(DbHandler dbHandler, Inspection inspection) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

        try (SQLiteDatabase db = dbHandler.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put("id", inspection.getId().toString());
            values.put("producer1Name", inspection.getProducer1Name());
            values.put("producer1Tin", inspection.getProducer1Tin());
            values.put("producer2Name", inspection.getProducer2Name());
            values.put("producer2Tin", inspection.getProducer2Tin());
            values.put("producer3Name", inspection.getProducer3Name());
            values.put("producer3Tin", inspection.getProducer3Tin());
            values.put("producer4Name", inspection.getProducer4Name());
            values.put("producer4Tin", inspection.getProducer4Tin());
            values.put("locationX", inspection.getLatitude());
            values.put("locationY", inspection.getLongitude());
            values.put("date", dateFormat.format(inspection.getDate()));

            db.insert(DbHandler.TABLE_INSPECTIONS, null, values);
        }
        return inspection;
    }

    public static List<InspectionDateProducer> getAllInspections(DbHandler dbHandler) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

        List<InspectionDateProducer> inspectionDateProducerArrayList = new ArrayList<>();
        final String selectDateProducerQuery = "SELECT id,date,producer1Name FROM " + DbHandler.TABLE_INSPECTIONS + " ORDER BY date DESC";
        try (SQLiteDatabase db = dbHandler.getReadableDatabase();
             Cursor cursor = db.rawQuery(selectDateProducerQuery, null)) {

            if (cursor.moveToFirst()) {
                do {
                    InspectionDateProducer inspectionDateProducer = new InspectionDateProducer(
                            cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2));
                    inspectionDateProducerArrayList.add(inspectionDateProducer);
                } while (cursor.moveToNext());
            }
        }
        return inspectionDateProducerArrayList;
    }

    public static Inspection getInspectionFor(DbHandler dbHandler, String uuidString) throws ParseException, PersistenceException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String selectDateProducerQuery = "SELECT id, date, " +
                "producer1Tin, producer1Name," +
                "producer2Tin, producer2Name," +
                "producer3Tin, producer3Name," +
                "producer4Tin, producer4Name, " +
                "locationX, locationY " +
                "FROM " + DbHandler.TABLE_INSPECTIONS + " WHERE id = '" + uuidString + "'";

        UUID uuid = UUID.fromString(uuidString);
        try (SQLiteDatabase db = dbHandler.getReadableDatabase();
             Cursor cursor = db.rawQuery(selectDateProducerQuery, null)) {

            if (cursor.getCount() != 1) {
                throw new PersistenceException("Expected one entry, found none or too many");
            }
            cursor.moveToFirst();

            Inspection inspection = new Inspection(
                    dateFormat.parse(cursor.getString(1)),
                    cursor.getString(2),
                    cursor.getString(3));
            inspection.setId(uuid);
            inspection.setProducer2Tin(cursor.getString(4));
            inspection.setProducer2Name(cursor.getString(5));
            inspection.setProducer3Tin(cursor.getString(6));
            inspection.setProducer3Name(cursor.getString(7));
            inspection.setProducer4Tin(cursor.getString(8));
            inspection.setProducer4Name(cursor.getString(9));
            inspection.setLatitude(cursor.getFloat(10));
            inspection.setLongitude(cursor.getFloat(11));
            inspection.setEntries(EntryRepository.getEntriesFor(dbHandler,inspection.getId()));
            inspection.setScannedDocuments(ScannedDocumentRepository.getScannedDocumentFor(dbHandler,inspection.getId()));
            return inspection;
        }
    }
}
