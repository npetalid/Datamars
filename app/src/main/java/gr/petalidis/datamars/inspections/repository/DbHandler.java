package gr.petalidis.datamars.inspections.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 3;
    // Database Name
    private static final String DATABASE_NAME = "bovscanner";

    public static final String TABLE_INSPECTIONS = "INSPECTIONS";

    public static final String TABLE_INSPECTION_ENTRIES = "ENTRIES";

    public static final String TABLE_INSPECTION_OTHER_ENTRIES = "OTHER_ENTRIES";

    public static final String TABLE_INSPECTION_SCANNED_DOCUMENT = "SCANNED_DOCUMENT";

    public static final String TABLE_USB_ALIAS = "USB_ALIAS";

    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_INSPECTIONS = "CREATE TABLE IF NOT EXISTS " + TABLE_INSPECTIONS +
        " ( " +
                "id TEXT PRIMARY KEY, " +
                "producer1Tin TEXT, " +
                "producer1Name TEXT, " +
                "producer2Tin TEXT, " +
                "producer2Name TEXT, " +
                "producer3Tin TEXT, " +
                "producer3Name TEXT, " +
                "producer4Tin TEXT, " +
                "producer4Name TEXT, " +
                "locationX REAL," +
                "locationY REAL," +
                "date TEXT " +
                ") ";
        String CREATE_TABLE_INSPECTION_ENTRIES = "CREATE TABLE IF NOT EXISTS " + TABLE_INSPECTION_ENTRIES +
                " ( " +
                "id TEXT PRIMARY KEY, " +
                "inspectionId TEXT, " +
                "country TEXT, " +
                "tag TEXT, " +
                "tagDate TEXT, " +
                "isInRegister INTEGER, " +
                "producer TEXT, " +
                "producerTin TEXT, " +
                "animalType TEXT, " +
                "animalGenre TEXT, " +
                "comment TEXT" +
                ") ";

        String CREATE_TABLE_INSPECTION_OTHER_ENTRIES = "CREATE TABLE IF NOT EXISTS " + TABLE_INSPECTION_OTHER_ENTRIES +
                " ( " +
                "id TEXT PRIMARY KEY, " +
                "type TEXT, " +
                "inspectionId TEXT, " +
                "producerTin TEXT, " +
                "producerName TEXT, " +
                "animal TEXT, " +
                "value INTEGER" +
                ")";

        String CREATE_TABLE_INSPECTION_IMAGES = "CREATE TABLE IF NOT EXISTS " + TABLE_INSPECTION_SCANNED_DOCUMENT +
                " ( " +
                "id TEXT PRIMARY KEY, " +
                "inspectionId TEXT, " +
                "imagePath TEXT " +
                ") ";

        String CREATE_TABLE_USB_ALIAS = "CREATE TABLE IF NOT EXISTS " + TABLE_USB_ALIAS +
                " ( " +
                "usb TEXT PRIMARY KEY, " +
                "alias TEXT " +
                ") ";
        sqLiteDatabase.execSQL(CREATE_TABLE_INSPECTIONS);
        sqLiteDatabase.execSQL(CREATE_TABLE_INSPECTION_ENTRIES);
        sqLiteDatabase.execSQL(CREATE_TABLE_INSPECTION_OTHER_ENTRIES);
        sqLiteDatabase.execSQL(CREATE_TABLE_INSPECTION_IMAGES);
        sqLiteDatabase.execSQL(CREATE_TABLE_USB_ALIAS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }

}
