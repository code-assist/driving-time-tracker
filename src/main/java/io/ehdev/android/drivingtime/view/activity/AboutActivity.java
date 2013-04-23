package io.ehdev.android.drivingtime.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

public class AboutActivity extends Activity {
    public static final String TAG = AboutActivity.class.getName();

    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        getActionBar().setDisplayHomeAsUpEnabled(true);

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
