package gr.petalidis.datamars.inspections.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import gr.petalidis.datamars.inspections.domain.CommentType;
import gr.petalidis.datamars.inspections.domain.Entry;
import gr.petalidis.datamars.rsglibrary.Rsg;

public class EntryRepository {

    public static void save(DbHandler dbHandler, List<Entry> entries)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

        try (final SQLiteDatabase db = dbHandler.getWritableDatabase()) {
            entries.forEach(e -> {
                ContentValues values = new ContentValues();
                values.put("id", e.getId().toString());
                values.put("inspectionId", e.getInspectionId().toString());
                values.put("tag", e.getTag());
                values.put("country", e.getCountry());
                values.put("tagDate", dateFormat.format(e.getTagDate()));
                values.put("animalType", e.getAnimalType());
                values.put("animalGenre", e.getAnimalGenre());
                values.put("producer", e.getProducer());
                values.put("producerTin", e.getProducerTin());
                values.put("isInRegister", e.isInRegister() == true ? 1 : 0);
                values.put("comment", e.getComment().getTitle());

                db.insert(DbHandler.TABLE_INSPECTION_ENTRIES, null, values);
            });

        }
        return;
    }

    public static List<Rsg> getAlreadyCheckedRsgsFor(DbHandler dbHandler, Date inspectionDate) throws ParseException
    {
        List<Rsg> inspectedRsgs = new ArrayList<>();
        SimpleDateFormat onlyDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

        String realInspectionDate = onlyDateFormat.format(inspectionDate)+"%";

        String selectDateProducerQuery = "SELECT " +
                "tag," +
                "country," +
                "tagDate" +
                " FROM " + DbHandler.TABLE_INSPECTION_ENTRIES + " where tagDate like '" +realInspectionDate+"' AND producerTin!='000000000'";
        try (final SQLiteDatabase db = dbHandler.getReadableDatabase();
             Cursor cursor = db.rawQuery(selectDateProducerQuery, null)) {

            if (cursor.moveToFirst()) {
                do {
                    Rsg entry = new Rsg(cursor.getString(1),
                            cursor.getString(0),
                            fullDateFormat.parse(cursor.getString(2)));
                    inspectedRsgs.add(entry);
                } while (cursor.moveToNext());
            }
        }
        return inspectedRsgs;
    }
    public static List<Entry> getEntriesFor(DbHandler dbHandler, UUID inspectionId) throws ParseException
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

        List<Entry> entries = new ArrayList<>();

        String selectDateProducerQuery = "SELECT " +
                "id," +
                "inspectionId," +
                "tag," +
                "country," +
                "tagDate," +
                "animalType," +
                "animalGenre," +
                "producer," +
                "producerTin,"+
                "isInRegister, " +
                "comment " +
                " FROM " + DbHandler.TABLE_INSPECTION_ENTRIES + " where inspectionId = '" + inspectionId.toString() + "'";

        try (final SQLiteDatabase db = dbHandler.getReadableDatabase();
             Cursor cursor = db.rawQuery(selectDateProducerQuery, null)) {

            if (cursor.moveToFirst()) {
                do {
                    Entry entry = new Entry();
                    entry.setId(UUID.fromString(cursor.getString(0)));
                    entry.setInspectionId(UUID.fromString(cursor.getString(1)));
                    entry.setTag(cursor.getString(2));
                    entry.setCountry(cursor.getString(3));
                    entry.setTagDate(dateFormat.parse(cursor.getString(4)));
                    entry.setAnimalType(cursor.getString(5));
                    entry.setAnimalGenre(cursor.getString(6));
                    entry.setProducer(cursor.getString(7));
                    entry.setProducerTin(cursor.getString(8));
                    entry.setInRegister(cursor.getInt(9)==1?true:false);
                    entry.setComment(CommentType.fromString(cursor.getString(10)));
                    entries.add(entry);
                } while (cursor.moveToNext());
            }
        }
        return entries;
    }


}
