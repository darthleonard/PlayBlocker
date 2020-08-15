package darthleonard.archaos.playblocker.helpers;

import android.content.Context;
import darthleonard.archaos.playblocker.database.DBConfigManager;

public class RandomSortButtonActivator {
    public boolean IsRandomActivated(Context context) {
        return new DBConfigManager(context).IsRandomSortActive();
    }

    public boolean Switch(Context context) {
        DBConfigManager db = new DBConfigManager(context);
        boolean status = db.IsRandomSortActive();
        db.SetRandomSort(status ? 0 : 1);
        return !status;
    }
}
