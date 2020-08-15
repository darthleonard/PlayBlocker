package darthleonard.archaos.playblocker;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Stack;

import darthleonard.archaos.playblocker.database.DBAppManager;
import darthleonard.archaos.playblocker.database.DBAuthManager;

public class AccessActivity extends AppCompatActivity {
    public static final String KEY_PACKAGE_NAME = "PackageName";

    private EditText etPassword;
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
        if(db.IsRandomSortActive()) {
            randomButtons();
        }
        db.Close();
        etPassword = findViewById(R.id.etPassword);
    }

    @Override
    protected void onPause() {
        if(!auth && getIntent().getExtras() != null) {
            killApp(getIntent().getExtras().getString(KEY_PACKAGE_NAME));
        }
        super.onPause();
    }

    public void onNumberClick(View view) {
        TextView tvButton = (TextView)(view);
        CharSequence input = tvButton.getText();
        etPassword.append(input);
    }

    public void onDeleteClick(View view) {
        CharSequence sequence = etPassword.getText();
        if(sequence.length() == 0) {
            return;
        }
        sequence = sequence.subSequence(0, sequence.length() - 1);
        etPassword.setText(sequence);
    }

    public void onDoneClick(View view) {
        validatePassword();
    }

    private void validatePassword() {
        DBAuthManager db = new DBAuthManager(getApplicationContext());
        db.Open();
        if(db.IsPasswordValid(etPassword.getText().toString())) {
            GrantAccess();
        } else {
            etPassword.setText("");
        }
        db.Close();
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

    private void randomButtons() {
        TextView[] tvButtons = loadButtons();
        int number;
        Stack<Integer> stack = new Stack<Integer>();
        for (int i = 0; i < tvButtons.length ; i++) {
            number = (int) Math.floor(Math.random() * tvButtons.length );
            while (stack.contains(number)) {
                number = (int) Math.floor(Math.random() * tvButtons.length );
            }
            stack.push(number);
            tvButtons[i].setText(String.valueOf(number));
        }
    }

    private TextView[] loadButtons() {
        return new TextView[] {
                findViewById(R.id.btn0),
                findViewById(R.id.btn1),
                findViewById(R.id.btn2),
                findViewById(R.id.btn3),
                findViewById(R.id.btn4),
                findViewById(R.id.btn5),
                findViewById(R.id.btn6),
                findViewById(R.id.btn7),
                findViewById(R.id.btn8),
                findViewById(R.id.btn9),
        };
    }
}
