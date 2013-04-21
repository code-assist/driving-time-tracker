package io.ehdev.android.drivingtime.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import io.ehdev.android.drivingtime.R;

public class TaskConfiguration extends Activity {
    public static final String TAG = TaskConfiguration.class.getName();

    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Configure Tasks");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.task_configuration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.add:
                //createAddEntry();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
