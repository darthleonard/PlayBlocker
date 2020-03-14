package darthleonard.archaos.playblocker;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import darthleonard.archaos.playblocker.database.DBConfigManager;
import darthleonard.archaos.playblocker.helpers.PermissionVerifier;
import darthleonard.archaos.playblocker.locker.PlayBlockerService;
import darthleonard.archaos.playblocker.model.AppItemAdapter;
import darthleonard.archaos.playblocker.model.AppLoader;
import darthleonard.archaos.playblocker.model.AppLoaderArgs;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_AUTHENTICATED = "auth";
    private ProgressBar pbLoadingApps;
    private AppItemAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ValidateOpen();
        setContentView(R.layout.activity_main);
        initComponents();
        loadApps();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                return true;
            case R.id.menu_password:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadApps() {
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        pbLoadingApps.setMax(packages.size());
        AppLoaderArgs args = new AppLoaderArgs();
        args.PackageName = getPackageName();
        args.Adapter = adapter;
        args.ProgressBar = pbLoadingApps;
        args.PackageManager = pm;
        args.Packages = packages;
        new AppLoader(args).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void ValidateOpen() {
        Bundle args = getIntent().getExtras();
        if(args == null || !args.getBoolean(KEY_AUTHENTICATED, false)) {
            OpenAuthActivity();
            finish();
        }
    }

    private void  initComponents() {
        adapter = new AppItemAdapter(this);
        ListView listView = findViewById(R.id.lvAppList);
        listView.setAdapter(adapter);
        final ToggleButton toggle = findViewById(R.id.btnService);
        toggle.setChecked(IsServiceRunning(PlayBlockerService.class));
        pbLoadingApps = findViewById(R.id.pbLoadingApps);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(!new PermissionVerifier().VerifyPermission(MainActivity.this)) {
                        toggle.setChecked(false);
                        return;
                    }
                    startService(new Intent(MainActivity.this, PlayBlockerService.class));
                    new DBConfigManager(getApplicationContext()).Service(1);
                } else {
                    stopService(new Intent(MainActivity.this, PlayBlockerService.class));
                    new DBConfigManager(getApplicationContext()).Service(0);
                }
            }
        });
    }

    private boolean IsServiceRunning(Class<?> serviceClass) {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void OpenAuthActivity() {
        Intent intent = new Intent(MainActivity.this, AccessActivity.class);
        startActivity(intent);
    }
}
