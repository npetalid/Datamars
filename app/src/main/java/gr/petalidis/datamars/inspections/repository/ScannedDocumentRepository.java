package gr.petalidis.datamars.inspections.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import gr.petalidis.datamars.inspections.domain.ScannedDocument;

public class ScannedDocumentRepository {

    public static List<ScannedDocument> getScannedDocumentFor(DbHandler dbHandler, UUID inspectionId) throws ParseException
    {
        List<ScannedDocument> scannedDocuments = new ArrayList<>();

        String selectDateProducerQuery = "SELECT " +
                "id," +
                "inspectionId," +
                "imagePath " +

                " FROM " + DbHandler.TABLE_INSPECTION_SCANNED_DOCUMENT + " where inspectionId = '" + inspectionId.toString() + "'";

        try (final SQLiteDatabase db = dbHandler.getReadableDatabase();
             Cursor cursor = db.rawQuery(selectDateProducerQuery, null)) {

            if (cursor.moveToFirst()) {
                do {
                    ScannedDocument scannedDocument = new ScannedDocument(UUID.fromString(cursor.getString(1)),cursor.getString(2));
                    scannedDocument.setId(UUID.fromString(cursor.getString(0)));
                    scannedDocuments.add(scannedDocument);
                } while (cursor.moveToNext());
            }
        }
        return scannedDocuments;
    }

    public static void save(DbHandler dbHandler, List<ScannedDocument> scannedDocuments)
    {
        try (final SQLiteDatabase db = dbHandler.getWritableDatabase()) {
            scannedDocuments.forEach(e -> {
                ContentValues values = new ContentValues();
                values.put("id", e.getId().toString());
                values.put("inspectionId", e.getInspectionId().toString());
                values.put("imagePath", e.getImagePath());

                db.insert(DbHandler.TABLE_INSPECTION_SCANNED_DOCUMENT, null, values);
            });

        }
        return;
    }
}
