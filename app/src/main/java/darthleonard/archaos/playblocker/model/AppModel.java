package darthleonard.archaos.playblocker.model;

import android.graphics.drawable.Drawable;

public class AppModel {
    private Drawable appIcon;
    private String appName;
    private String appPackage;
    private int appStatus;

    public AppModel(Drawable appIcon, String appName, String appPackage) {
        this.appIcon = appIcon;
        this.appName = appName;
        this.appPackage = appPackage;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public boolean isAppLocked() {
        return appStatus == 1;
    }
}
