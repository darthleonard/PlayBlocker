package darthleonard.archaos.playblocker.helpers;

import android.app.usage.UsageStatsManager;
import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import java.util.Calendar;

import darthleonard.archaos.playblocker.R;

public class PermissionVerifier {
    public boolean VerifyPermission(Context context) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        if(usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                cal.getTimeInMillis(),
                System.currentTimeMillis())
        .size() != 0)  {
            return true;
        }

        new AlertDialog.Builder(context)
                .setTitle(R.string.permissionNeeded)
                .setMessage(R.string.permissionMessage)
                .setNeutralButton(R.string.accept, null)
                .create()
                .show();

        return false;
    }
}
