package darthleonard.archaos.playblocker.locker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import darthleonard.archaos.playblocker.AccessActivity;
import darthleonard.archaos.playblocker.R;

public class Notifier {
    public static final String NOTIFICATION_CHANNEL_ID_SERVICE = "darthleonard.archaos.playblocker.service";
    public static final String NOTIFICATION_CHANNEL_ID_INFO = "darthleonard.archaos.playblocker.info";

    private Service service;

    public Notifier(Service service) {
        this.service = service;
    }

    public Notification Create() {
        initChannel();
        PendingIntent pendingIntent = CreatePendingIntent();
        NotificationCompat.Builder builder = CreateBuilder(pendingIntent);
        return builder.build();
    }

    private void initChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Context context = service.getApplicationContext();
            NotificationManager nm = (NotificationManager) service.getSystemService(context.NOTIFICATION_SERVICE);
            nm.createNotificationChannel(new NotificationChannel(NOTIFICATION_CHANNEL_ID_SERVICE, "App Service", NotificationManager.IMPORTANCE_DEFAULT));
            nm.createNotificationChannel(new NotificationChannel(NOTIFICATION_CHANNEL_ID_INFO, "Blocking Info", NotificationManager.IMPORTANCE_DEFAULT));
        }
    }

    private PendingIntent CreatePendingIntent() {
        Intent intent = new Intent(service, AccessActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(service, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private NotificationCompat.Builder CreateBuilder(PendingIntent pendingIntent) {
        return new NotificationCompat.Builder(service, NOTIFICATION_CHANNEL_ID_INFO)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("PlayBlocker")
                .setContentText("Blocking apps")
                .setSmallIcon(R.mipmap.ic_launcher_foreground);
    }
}
