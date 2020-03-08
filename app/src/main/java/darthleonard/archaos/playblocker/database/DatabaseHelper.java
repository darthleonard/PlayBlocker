package darthleonard.archaos.playblocker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String ID = "Id";
    public static final String TABLE_APPS = "Apps";
    public static final String TABLE_AUTH = "Auth";
    public static final String PACKAGE_NAME = "PackageName";
    public static final String APP_STATUS = "AppStatus";
    static final String DB_NAME = "playblocker.db";
    public static final String AUTH_PSW = "Psw";
    static final int DB_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table "+ TABLE_APPS +"("+
                        ID +" integer primary key, "+
                        PACKAGE_NAME +" text, "+
                        APP_STATUS +" integer)"
        );
        db.execSQL("create table " + TABLE_AUTH + "(" +
                ID + " integer primary key, " +
                AUTH_PSW + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTH);
        onCreate(db);
    }
}
