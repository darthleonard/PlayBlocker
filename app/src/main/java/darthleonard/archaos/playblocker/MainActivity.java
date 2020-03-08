package darthleonard.archaos.playblocker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ToggleButton;

import darthleonard.archaos.playblocker.locker.PlayBlockerService;
import darthleonard.archaos.playblocker.model.AppItemAdapter;
import darthleonard.archaos.playblocker.model.AppLoader;

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
        new AppLoader(this, adapter, pbLoadingApps).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

    private void ValidateOpen() {
        Bundle args = getIntent().getExtras();
        if(args == null || !args.getBoolean(KEY_AUTHENTICATED, false)) {
            OpenAuthActivity();
            finish();
        }
    }

    private void initComponents() {
        adapter = new AppItemAdapter(this);
        ListView listView = findViewById(R.id.lvAppList);
        listView.setAdapter(adapter);
        ToggleButton toggle = findViewById(R.id.btnService);
        toggle.setChecked(IsServiceRunning(PlayBlockerService.class));
        pbLoadingApps = findViewById(R.id.pbLoadingApps);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startService(new Intent(MainActivity.this, PlayBlockerService.class));
                } else {
                    stopService(new Intent(MainActivity.this, PlayBlockerService.class));
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
