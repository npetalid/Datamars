
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

package gr.petalidis.datamars.inspections.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import gr.petalidis.datamars.inspections.domain.Inspectee;
import gr.petalidis.datamars.inspections.domain.OtherEntry;
import gr.petalidis.datamars.inspections.domain.OtherEntryType;

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
    }

    public static Set<OtherEntry> getEntriesFor(DbHandler dbHandler, UUID inspectionId) {
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
                " where inspectionId = '" + inspectionId.toString() + "'";

        try (final SQLiteDatabase db = dbHandler.getReadableDatabase();
             Cursor cursor = db.rawQuery(selectDateProducerQuery, null)) {

            if (cursor.moveToFirst()) {
                do {
                    OtherEntry entry = new OtherEntry();
                    entry.setId(UUID.fromString(cursor.getString(0)));
                    entry.setInspectionId(UUID.fromString(cursor.getString(1)));
                    entry.setEntryType(OtherEntryType.valueOf(cursor.getString(2)));                 entry.setInspectee(new Inspectee(cursor.getString(3),cursor.getString(4)));
                    entry.setAnimal(cursor.getString(5));
                    entry.setCount(cursor.getInt(6));
                    entries.add(entry);
                } while (cursor.moveToNext());
            }
        }
        return entries;
    }


}
