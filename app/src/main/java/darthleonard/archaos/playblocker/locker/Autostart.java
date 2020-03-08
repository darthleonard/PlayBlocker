package darthleonard.archaos.playblocker.locker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import darthleonard.archaos.playblocker.database.DBConfigManager;

public class Autostart extends BroadcastReceiver {
    public void onReceive(Context context, Intent arg1)
    {
        if(new DBConfigManager(context).IsServiceEnable()) {
            Intent intent = new Intent(context, PlayBlockerService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent);
            } else {
                context.startService(intent);
            }
        }
    }
}
