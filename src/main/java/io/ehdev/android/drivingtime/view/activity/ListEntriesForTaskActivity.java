package io.ehdev.android.drivingtime.view.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import io.ehdev.android.drivingtime.R;
import io.ehdev.android.drivingtime.backend.model.Task;
import io.ehdev.android.drivingtime.database.dao.DatabaseHelper;
import io.ehdev.android.drivingtime.view.fragments.TaskDrivingRecordReviewFragment;

import java.sql.SQLException;

public class ListEntriesForTaskActivity  extends Activity {
    public static final String TAG = ListEntriesForTaskActivity.class.getName();
    public static final int VIEW_ID = 1;
    private Task drivingTask;
    private TaskDrivingRecordReviewFragment taskFragmentView;

    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        try{
            int taskId = this.getIntent().getExtras().getInt("taskId");
            String taskName = getTaskName(taskId);

            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setTitle(String.format("%s Driving Records", taskName));

            FrameLayout fl = new FrameLayout(this);
            fl.setId(VIEW_ID);
            taskFragmentView = new TaskDrivingRecordReviewFragment(drivingTask);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(VIEW_ID, taskFragmentView).commit();

            setContentView(fl);
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.task_window, menu);
        return true;
    }

    private String getTaskName(int taskId) {
        try{
            drivingTask = new DatabaseHelper(this).getTaskFromId(taskId);
            if(drivingTask != null)
                return drivingTask.getTaskName();
        } catch (SQLException e) {
            finish();
        }
        throw new TaskNotValidException();
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.add:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public static class TaskNotValidException extends RuntimeException {
    }
}
