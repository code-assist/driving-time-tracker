package io.ehdev.android.drivingtime.view.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.google.analytics.tracking.android.GoogleAnalytics;
import io.ehdev.android.drivingtime.R;

public class AboutActivity extends Activity {
    public static final String TAG = AboutActivity.class.getName();
    public static final String PREF_FILE_NAME = "opt_out";

    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.about_layout);
        CheckBox checkBox = (CheckBox) findViewById(R.id.opt_out);
        checkBox.setOnCheckedChangeListener(getCheckedListener());
        checkBox.setChecked(getOptOutStatus());

    }

    private boolean getOptOutStatus() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        return prefs.getBoolean("opt-out", false);
    }

    private CompoundButton.OnCheckedChangeListener getCheckedListener() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GoogleAnalytics myInstance = GoogleAnalytics.getInstance(getApplicationContext());
                myInstance.setAppOptOut(isChecked);
                SharedPreferences prefs = getApplicationContext().getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("opt-out", isChecked);
                editor.commit();
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
