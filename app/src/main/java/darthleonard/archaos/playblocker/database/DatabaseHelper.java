package darthleonard.archaos.playblocker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String ID = "Id";
    public static final String TABLE_APPS = "Apps";
    public static final String TABLE_AUTH = "Auth";
    public static final String TABLE_CONFIG = "AppConfig";
    public static final String PACKAGE_NAME = "PackageName";
    public static final String APP_STATUS = "AppStatus";
    static final String DB_NAME = "playblocker.db";
    public static final String AUTH_PSW = "Psw";
    public static final String CONFIG_SERVICE_STATUS = "ServiceStatus";
    public static final String CONFIG_RANDOM_SORT_BUTTONS = "RandomSortButtons";
    static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createAppsTable(db);
        createAuthTable(db);
        createConfigTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFIG);
        onCreate(db);
    }

    private void createAppsTable(SQLiteDatabase db) {
        db.execSQL(
                "create table "+ TABLE_APPS +"("+
                        ID +" integer primary key, "+
                        PACKAGE_NAME +" text, "+
                        APP_STATUS +" integer)"
        );
    }

    private void createAuthTable(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_AUTH + "(" +
                ID + " integer primary key, " +
                AUTH_PSW + " text)");
    }

    private void createConfigTable(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CONFIG + "(" +
                ID + " integer primary key, " +
                CONFIG_SERVICE_STATUS + " integer," +
                CONFIG_RANDOM_SORT_BUTTONS + " integer)");
        InitConfig(db);
    }

    private void InitConfig(SQLiteDatabase db) {
        if(db.rawQuery(
                "select * from "+ DatabaseHelper.TABLE_CONFIG,
                null)
                .moveToFirst()) {
           return;
        }
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.CONFIG_SERVICE_STATUS, 0);
        db.insert(DatabaseHelper.TABLE_CONFIG, null, contentValue);
    }
}
