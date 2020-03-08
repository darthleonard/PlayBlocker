package darthleonard.archaos.playblocker.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
    private DatabaseHelper dbHelper;
    private Context context;
    protected SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager Open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void Close() {
        dbHelper.close();
    }
}
