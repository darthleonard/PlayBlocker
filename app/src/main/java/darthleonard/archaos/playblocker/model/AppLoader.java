package darthleonard.archaos.playblocker.model;

import android.content.pm.ApplicationInfo;
import android.os.AsyncTask;
import android.view.ViewManager;

import java.util.Comparator;

public class AppLoader extends AsyncTask<Void, AppModel, Void> {
    private AppLoaderArgs args;

    public AppLoader(AppLoaderArgs args) {
        this.args = args;
    }

    @Override
    protected Void doInBackground(Void... unused) {
        for (ApplicationInfo info : args.Packages) {
            if(isAppExcluded(info.packageName)) {
                publishProgress(null);
                continue;
            }
            AppModel app = new AppModel(
                    info.loadIcon(args.PackageManager),
                    info.loadLabel(args.PackageManager).toString(),
                    info.packageName);
            publishProgress(app);
        }
        return(null);
    }

    @Override
    protected void onProgressUpdate(AppModel... item) {
        args.ProgressBar.setProgress(args.ProgressBar.getProgress() + 1);
        if(item != null) {
            args.Adapter.add(item[0]);
            args.Adapter.sort(new Comparator<AppModel>() {
                @Override
                public int compare(AppModel o1, AppModel o2) {
                    return o1.getAppName().compareTo(o2.getAppName());
                }
            });
        }
    }

    @Override
    protected void onPostExecute(Void unused) {
        ((ViewManager)args.ProgressBar.getParent()).removeView(args.ProgressBar);
    }

    private boolean isAppExcluded(String packageName) {
        return packageName.equals(args.PackageName) || args.PackageManager.getLaunchIntentForPackage(packageName) == null;
    }
}
