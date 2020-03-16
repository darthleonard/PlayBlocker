package darthleonard.archaos.playblocker.locker;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import darthleonard.archaos.playblocker.helpers.Notifier;

public class PlayBlockerService extends Service {
    public static final int NOTIFICATION_ID = 31;
    private AppsHandler appsHandler;

    @Override
    public void onCreate() { }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(appsHandler == null) {
            Notifier notifier = new Notifier(this);
            startForeground(NOTIFICATION_ID, notifier.Create());
            appsHandler = new AppsHandler(this);
            appsHandler.execute();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if(appsHandler != null) {
            appsHandler.cancel(true);
            appsHandler = null;
            stopForeground(true);
        }
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }
}
