package darthleonard.archaos.playblocker.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import darthleonard.archaos.playblocker.R;
import darthleonard.archaos.playblocker.database.DBAppManager;

public class AppItemAdapter extends ArrayAdapter<AppModel> {
    public AppItemAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AppModel app = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        ImageView ivAppIcon = convertView.findViewById(R.id.ivAppIcon);
        TextView tvAppName = convertView.findViewById(R.id.tvAppName);
        Switch swAppStatus = convertView.findViewById(R.id.swAppState);
        ivAppIcon.setImageDrawable(app.getAppIcon());
        tvAppName.setText(app.getAppName());
        swAppStatus.setText(app.getAppPackage());
        swAppStatus.setTextSize(0);
        setStatus(swAppStatus, app);
        addListener(swAppStatus);
        return convertView;
    }

    private void setStatus(Switch swAppStatus, AppModel app) {
        DBAppManager db = new DBAppManager(getContext());
        db.Open();
        swAppStatus.setChecked(db.Exist(app.getAppPackage()));
        db.Close();
    }

    private void addListener(Switch swAppStatus) {
        swAppStatus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Switch sw = (Switch)view;
                DBAppManager db = new DBAppManager(getContext());
                db.Open();
                if (sw.isChecked()) {
                    db.Insert(sw.getText().toString());
                } else {
                    db.Delete(sw.getText().toString());
                }
                db.Close();
            }
        });
    }
}
