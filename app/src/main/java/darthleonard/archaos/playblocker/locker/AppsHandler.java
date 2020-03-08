package darthleonard.archaos.playblocker.locker;

import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import darthleonard.archaos.playblocker.AccessActivity;
import darthleonard.archaos.playblocker.database.DBAppManager;

public class AppsHandler  extends AsyncTask<Void, Void, Void> {
    private Service service;
    private String currentPackage = "";

    public AppsHandler(Service service) {
        this.service = service;
    }

    @Override
    protected Void doInBackground(Void... params) {
        while (!this.isCancelled()) {
            try {
                Thread.sleep(800);
                AppScanner();
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
        }
        return null;
    }

    /**
     * Scans the used <i>packages</i> in the last 5 seconds and sorts it.
     * If the last used is the PACKAGE_NAME, calls kill function.
     */
    private void AppScanner() {
        UsageStatsManager usageStatsManager = (UsageStatsManager) service.getSystemService(Context.USAGE_STATS_SERVICE);
        long time = System.currentTimeMillis();
        List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 5, time);

        if (stats == null) {
            return;
        }
        // Sort the stats by the last time used
        SortedMap<Long, UsageStats> sortedMap = new TreeMap<>();
        for (UsageStats usageStats : stats) {
            sortedMap.put(usageStats.getLastTimeUsed(), usageStats);
        }

        if (!sortedMap.isEmpty()) {
            String packageName = sortedMap.get(sortedMap.lastKey()).getPackageName();
            DBAppManager db = new DBAppManager(service.getApplicationContext());
            db.Open();
            if(!packageName.equals(currentPackage)) {
                db.Update(currentPackage, 0);
            }
            if(db.Exist(packageName) && db.IsAppLocked(packageName)) {
                //killApp(packageName);
                currentPackage = packageName;
                Log.e("applock", "requesting auth to " + currentPackage);
                LaunchAccessActivity(packageName);
            }
            db.Close();
        }
    }

    private void LaunchAccessActivity(String packageName) {
        Intent intent = new Intent();
        intent.setClass(service.getApplicationContext(), AccessActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(AccessActivity.KEY_PACKAGE_NAME, packageName);
        service.startActivity(intent);
    }
}
