package darthleonard.archaos.playblocker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DBAppManager extends DBManager {
    public DBAppManager(Context c) {
        super(c);
    }

    public void Insert(String packageName) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.PACKAGE_NAME, packageName);
        database.insert(DatabaseHelper.TABLE_APPS, null, contentValue);
    }

    public int Update(String packageName, int appStatus) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.APP_STATUS, appStatus);
        int i = database.update(
                DatabaseHelper.TABLE_APPS,
                contentValues,
                DatabaseHelper.PACKAGE_NAME + " = '" + packageName + "'",
                null);
        return i;
    }

    public void Delete(String packageName) {
        database.delete(
                DatabaseHelper.TABLE_APPS,
                DatabaseHelper.PACKAGE_NAME + " = '" + packageName + "'",
                null);
    }

    public boolean Exist(String packageName) {
        Cursor app = database.rawQuery(
                "select "+ DatabaseHelper.PACKAGE_NAME +" " +
                        "from "+ DatabaseHelper.TABLE_APPS +" " +
                        "where "+ DatabaseHelper.PACKAGE_NAME +"='" + packageName + "'",
                null);
        if(app.moveToFirst()) {
            return true;
        }
        return false;
    }

    public boolean IsAppLocked(String packageName) {
        Cursor cursor = database.rawQuery(
                "select "+ DatabaseHelper.APP_STATUS +" " +
                        "from "+ DatabaseHelper.TABLE_APPS +" " +
                        "where "+ DatabaseHelper.PACKAGE_NAME +"='" + packageName + "'",
                null);
        if(cursor.moveToFirst()) {
            return cursor.getInt(0) == 0;
        }
        return false;
    }
}
