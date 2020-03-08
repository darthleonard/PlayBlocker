package darthleonard.archaos.playblocker.model;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.widget.ProgressBar;

import java.util.List;

public class AppLoaderArgs {
    public String PackageName;
    public ProgressBar ProgressBar;
    public PackageManager PackageManager;
    public List<ApplicationInfo> Packages;
    public AppItemAdapter Adapter;
}
