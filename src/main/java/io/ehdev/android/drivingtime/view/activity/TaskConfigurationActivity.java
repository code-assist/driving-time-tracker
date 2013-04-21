package io.ehdev.android.drivingtime.view.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import io.ehdev.android.drivingtime.R;
import io.ehdev.android.drivingtime.view.fragments.TaskEditFragment;

public class TaskConfigurationActivity extends Activity {
    public static final String TAG = TaskConfigurationActivity.class.getName();
    private static final int VIEW_ID = 2;
    private static TaskEditFragment taskEditFragment;

    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Configure Tasks");

        if(savedInstance == null){
            taskEditFragment = new TaskEditFragment();
        }

        FrameLayout fl = new FrameLayout(this);
        fl.setId(VIEW_ID);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(VIEW_ID, taskEditFragment).commit();

        setContentView(fl);
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
