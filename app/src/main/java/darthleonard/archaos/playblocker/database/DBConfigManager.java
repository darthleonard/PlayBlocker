package darthleonard.archaos.playblocker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DBConfigManager extends DBManager {
    public DBConfigManager(Context c) {
        super(c);
    }

    public int Service(int status) {
        Open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.CONFIG_SERVICE_STATUS, status);
        int i = database.update(
                DatabaseHelper.TABLE_CONFIG,
                contentValues,
                null,
                null);
        Close();
        return i;
    }

    public boolean IsServiceEnable() {
        Open();
        Cursor cursor = database.rawQuery(
                "select "+ DatabaseHelper.CONFIG_SERVICE_STATUS +" " +
                        "from "+ DatabaseHelper.TABLE_CONFIG,
                null);
        if(cursor.moveToFirst()) {
            Close();
            return cursor.getInt(0) == 1;
        }
        Close();
        return false;
    }

    public boolean IsRandomSortActive() {
        Open();
        Cursor cursor = database.rawQuery(
                "select " + DatabaseHelper.CONFIG_RANDOM_SORT_BUTTONS +
                        " from "+ DatabaseHelper.TABLE_CONFIG,
                null);
        if(cursor.moveToFirst()) {
            Close();
            return cursor.getInt(0) == 1;
        }
        Close();
        return false;
    }

    public void SetRandomSort(int state) {
        Open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.CONFIG_RANDOM_SORT_BUTTONS, state);
        database.update(
                DatabaseHelper.TABLE_CONFIG,
                contentValues,
                null,
                null);
        Close();
    }
}
