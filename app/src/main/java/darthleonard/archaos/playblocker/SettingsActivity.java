package darthleonard.archaos.playblocker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import darthleonard.archaos.playblocker.database.DBAuthManager;

public class SettingsActivity extends AppCompatActivity {
    TextView tvPasswordMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.btnPasswordUpdate).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                tvPasswordMessage = findViewById(R.id.tvPasswordMessage);
                tvPasswordMessage.setVisibility(View.INVISIBLE);
                EditText etPasswordNew = findViewById(R.id.etPasswordNew);
                EditText etPasswordConfirm = findViewById(R.id.etPasswordConfirm);
                String passwordNew = etPasswordNew.getText().toString();
                String passwordConfirm = etPasswordConfirm.getText().toString();
                if(validatePassword(passwordNew, passwordConfirm)) {
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

    public boolean validatePassword(String passwordNew, String passwordConfirm) {
        if(passwordNew.length() < 3) {
            tvPasswordMessage.setText(R.string.passwordShort);
            tvPasswordMessage.setVisibility(View.VISIBLE);
            return false;
        }
        if(passwordNew.length() < 3 || passwordNew.length() > 10) {
            tvPasswordMessage.setText(R.string.passwordLong);
            tvPasswordMessage.setVisibility(View.VISIBLE);
            return false;
        }
        if(!passwordNew.equals(passwordConfirm)) {
            tvPasswordMessage.setText(R.string.passwordMismatch);
            tvPasswordMessage.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }
}
