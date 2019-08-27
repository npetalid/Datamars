package gr.petalidis.datamars.inspections.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import gr.petalidis.datamars.inspections.domain.Entry;
import gr.petalidis.datamars.inspections.domain.Inspectee;
import gr.petalidis.datamars.inspections.domain.OtherEntry;
import gr.petalidis.datamars.inspections.domain.OtherEntryType;
import gr.petalidis.datamars.rsglibrary.Rsg;

public class OtherEntryRepository {

    public static void save(DbHandler dbHandler, Set<OtherEntry> entries)
    {
        try (final SQLiteDatabase db = dbHandler.getWritableDatabase()) {
            entries.forEach(e -> {
                ContentValues values = new ContentValues();
                values.put("id", e.getId().toString());
                values.put("type", e.getEntryType().getName());
                values.put("inspectionId", e.getInspectionId().toString());
                values.put("producerTin", e.getInspectee().getTin());
                values.put("producerName", e.getInspectee().getName());
                values.put("animal", e.getAnimal());
                values.put("value", e.getCount());

                db.insert(DbHandler.TABLE_INSPECTION_OTHER_ENTRIES, null, values);
            });

        }
        return;
    }

    public static Set<OtherEntry> getEntriesFor(DbHandler dbHandler, UUID inspectionId, OtherEntryType type) throws ParseException
    {
        Set<OtherEntry> entries = new HashSet<>();

        String selectDateProducerQuery = "SELECT " +
                "id," +
                "inspectionId," +
                "type," +
                "producerTin," +
                "producerName," +
                "animal," +
                "value " +
                " FROM " + DbHandler.TABLE_INSPECTION_OTHER_ENTRIES +
                " where inspectionId = '" + inspectionId.toString() + "' AND type='" + type.getName() + "'";

        try (final SQLiteDatabase db = dbHandler.getReadableDatabase();
             Cursor cursor = db.rawQuery(selectDateProducerQuery, null)) {

            if (cursor.moveToFirst()) {
                do {
                    OtherEntry entry = new OtherEntry();
                    entry.setId(UUID.fromString(cursor.getString(0)));
                    entry.setInspectionId(UUID.fromString(cursor.getString(1)));
                    entry.setEntryType(cursor.getString(2).equals(OtherEntryType.CONVENTIONAL.getName())?
                            OtherEntryType.CONVENTIONAL:OtherEntryType.NO_EARRING);
                    entry.setInspectee(new Inspectee(cursor.getString(3),cursor.getString(4)));
                    entry.setAnimal(cursor.getString(5));
                    entry.setCount(cursor.getInt(6));
                    entries.add(entry);
                } while (cursor.moveToNext());
            }
        }
        return entries;
    }


}
