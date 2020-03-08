package darthleonard.archaos.playblocker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import darthleonard.archaos.playblocker.database.DBAuthManager;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.btnPasswordUpdate).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditText etPasswordNew = findViewById(R.id.etPasswordNew);
                EditText etPasswordConfirm = findViewById(R.id.etPasswordConfirm);
                String passwordNew = etPasswordNew.getText().toString();
                String passwordConfirm = etPasswordConfirm.getText().toString();
                if(passwordNew.equals(passwordConfirm)) {
                    DBAuthManager db = new DBAuthManager(getApplicationContext());
                    db.Open();
                    db.SavePassword(passwordNew);
                    db.Close();
                    Intent intent = new Intent(SettingsActivity.this, AccessActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    etPasswordNew.setText("");
                    etPasswordConfirm.setText("");
                    findViewById(R.id.tvPasswordMessage).setVisibility(View.VISIBLE);
                }

            }
        });
    }
}
