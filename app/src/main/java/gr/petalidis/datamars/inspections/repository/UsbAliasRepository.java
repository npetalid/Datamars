
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

import java.util.ArrayList;
import java.util.List;

import gr.petalidis.datamars.inspections.domain.UsbAlias;

public class UsbAliasRepository {

    public static void save(DbHandler dbHandler, UsbAlias usbAlias) {
        try (final SQLiteDatabase db = dbHandler.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put("usb", usbAlias.getUsb());
            values.put("alias", usbAlias.getAlias());
            db.insert(DbHandler.TABLE_USB_ALIAS, null, values);
        }
    }

    public static void saveOrUpdate(DbHandler dbHandler, UsbAlias usbAlias) {
        if (getUsbAliasesFor(dbHandler,usbAlias.getUsb())==null) {
             save(dbHandler,usbAlias);
        } else {
            updateUsbAliasFor(dbHandler,usbAlias);
        }
    }

    private static void updateUsbAliasFor(DbHandler dbHandler, UsbAlias usbAlias)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("alias",usbAlias.getAlias());

        try (final SQLiteDatabase db = dbHandler.getReadableDatabase()) {
            db.update(DbHandler.TABLE_USB_ALIAS,contentValues,"usb=?",new String[]{usbAlias.getUsb()});
        }
    }
    private static UsbAlias getUsbAliasesFor(DbHandler dbHandler, String usb) {

        String findUsb = "SELECT usb, alias  FROM " + DbHandler.TABLE_USB_ALIAS +
                " WHERE usb='" + usb +"'";
        try (final SQLiteDatabase db = dbHandler.getReadableDatabase();
             Cursor cursor = db.rawQuery(findUsb, null)) {
            if (cursor.moveToFirst()) {
                return new UsbAlias(cursor.getString(0), cursor.getString(1));
            }
        }
        return null;
    }

    public static List<UsbAlias> getUsbAliases(DbHandler dbHandler) {

        List<UsbAlias> usbToAlias = new ArrayList<>();
        String selectDateProducerQuery = "SELECT usb, alias  FROM " + DbHandler.TABLE_USB_ALIAS;
        try (final SQLiteDatabase db = dbHandler.getReadableDatabase();
             Cursor cursor = db.rawQuery(selectDateProducerQuery, null)) {

            if (cursor.moveToFirst()) {
                do {
                    usbToAlias.add(new UsbAlias(cursor.getString(0), cursor.getString(1)));
                } while (cursor.moveToNext());
            }
        }
        return usbToAlias;
    }
}
