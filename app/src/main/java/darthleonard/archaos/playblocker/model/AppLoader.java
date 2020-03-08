package darthleonard.archaos.playblocker.model;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.view.ViewManager;
import android.widget.ProgressBar;

import java.util.List;

public class AppLoader extends AsyncTask<Void, AppModel, Void> {
    private Activity activity;
    private ProgressBar pbLoadingApps;
    private AppItemAdapter adapter;

    public AppLoader(Activity activity, AppItemAdapter adapter, ProgressBar pbLoadingApps) {
        this.activity = activity;
        this.pbLoadingApps = pbLoadingApps;
        this.adapter = adapter;
    }

    @Override
    protected Void doInBackground(Void... unused) {
        final PackageManager pm = activity.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        pbLoadingApps.setMax(packages.size());
        for (ApplicationInfo info : packages) {
            if(info.packageName.equals(activity.getPackageName()) || pm.getLaunchIntentForPackage(info.packageName) == null) {
                publishProgress(null);
                continue;
            }
            AppModel app = new AppModel(
                    info.loadIcon(pm),
                    info.loadLabel(activity.getPackageManager()).toString(),
                    info.packageName);
            publishProgress(app);
        }
        return(null);
    }

    @Override
    protected void onProgressUpdate(AppModel... item) {
        pbLoadingApps.setProgress(pbLoadingApps.getProgress() + 1);
        if(item != null)
            adapter.add(item[0]);
    }

    @Override
    protected void onPostExecute(Void unused) {
        ((ViewManager)pbLoadingApps.getParent()).removeView(pbLoadingApps);
    }
}
