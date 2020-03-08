package darthleonard.archaos.playblocker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import darthleonard.archaos.playblocker.database.DBAppManager;
import darthleonard.archaos.playblocker.database.DBAuthManager;

public class AccessActivity extends AppCompatActivity {
    public static final String KEY_PACKAGE_NAME = "PackageName";

    private boolean auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);

        DBAuthManager db = new DBAuthManager(getApplicationContext());
        db.Open();
        if(db.IsEmpty()) {
            OpenSettingsActivity();
            db.Close();
            finish();
        }
        db.Close();

        findViewById(R.id.btnAccess).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditText editText = findViewById(R.id.etPassword);
                DBAuthManager db = new DBAuthManager(getApplicationContext());
                db.Open();
                if(db.IsPasswordValid(editText.getText().toString())) {
                    GrantAccess();
                } else {
                    editText.setText("");
                }
                db.Close();
            }
        });
    }

    @Override
    protected void onPause() {
        if(!auth && getIntent().getExtras() != null) {
            killApp(getIntent().getExtras().getString(KEY_PACKAGE_NAME));
        }
        super.onPause();
    }

    private void OpenSettingsActivity() {
        Intent intent = new Intent(AccessActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void GrantAccess() {
        Bundle args = getIntent().getExtras();
        if(args == null) {
            Intent intent = new Intent(AccessActivity.this, MainActivity.class);
            intent.putExtra(MainActivity.KEY_AUTHENTICATED, true);
            startActivity(intent);
        } else {
            DBAppManager db = new DBAppManager(getApplicationContext());
            db.Open();
            db.Update(args.getString(KEY_PACKAGE_NAME), 1);
            db.Close();
        }
        auth = true;
        finish();
    }

    /**
     * Start home and kill process related to <i>PackageName</i>
     * @param packageName
     */
    private void killApp(String packageName) {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.killBackgroundProcesses(packageName);
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}
