package darthleonard.archaos.playblocker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DBAuthManager extends DBManager {

    public DBAuthManager(Context context) {
        super(context);
    }

    public void SavePassword(String password) {
        if(IsEmpty()) {
            InsertPassword(password);
        } else {
            UpdatePassword(password);
        }
    }

    public boolean IsEmpty() {
        Cursor cursor = database.rawQuery(
                "select * from "+ DatabaseHelper.TABLE_AUTH,
                null);
        return !cursor.moveToFirst();
    }

    public boolean IsPasswordValid(String password) {
        Cursor app = database.rawQuery(
                "select "+ DatabaseHelper.AUTH_PSW +" " +
                        "from "+ DatabaseHelper.TABLE_AUTH +" " +
                        "where "+ DatabaseHelper.AUTH_PSW +"='" + password + "'",
                null);
        return app.moveToFirst();
    }

    public boolean IsRandomSortActive() {
        Cursor cursor = database.rawQuery(
                "select " + DatabaseHelper.CONFIG_RANDOM_SORT_BUTTONS +
                        " from "+ DatabaseHelper.TABLE_CONFIG,
                null);
        if(cursor.moveToFirst()) {
            return cursor.getInt(0) == 1;
        }
        return false;
    }

    private void InsertPassword(String password) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.AUTH_PSW, password);
        database.insert(DatabaseHelper.TABLE_AUTH, null, contentValue);
    }

    private int UpdatePassword(String password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.AUTH_PSW, password);
        int i = database.update(
                DatabaseHelper.TABLE_AUTH,
                contentValues,
                null,
                null);
        return i;
    }
}
