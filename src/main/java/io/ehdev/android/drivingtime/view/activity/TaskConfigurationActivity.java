package io.ehdev.android.drivingtime.view.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import io.ehdev.android.drivingtime.R;
import io.ehdev.android.drivingtime.backend.model.Task;
import io.ehdev.android.drivingtime.view.dialog.EditTaskDialog;
import io.ehdev.android.drivingtime.view.dialog.InsertOrEditTaskDialog;
import io.ehdev.android.drivingtime.view.fragments.AbstractListDrivingFragment;
import io.ehdev.android.drivingtime.view.fragments.TaskEditFragment;
import org.joda.time.Duration;

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
                createAddEntry();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createAddEntry() {
        try{
            FragmentManager fm = getFragmentManager();
            InsertOrEditTaskDialog insertRecordDialog = new EditTaskDialog(new Task("", Duration.standardHours(1)), reloadView() );
            insertRecordDialog.show(fm, "Insert Record Dialog");
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
    }

    protected AbstractListDrivingFragment.PostEditExecution reloadView(){
        return new AbstractListDrivingFragment.PostEditExecution() {
            public void execute(){
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(taskEditFragment);
                ft.attach(taskEditFragment);
                ft.commit();
            }
        };
    }
}
