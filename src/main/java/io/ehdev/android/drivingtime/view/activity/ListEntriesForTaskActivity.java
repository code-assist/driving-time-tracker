package io.ehdev.android.drivingtime.view.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import dagger.ObjectGraph;
import io.ehdev.android.drivingtime.R;
import io.ehdev.android.drivingtime.backend.model.Record;
import io.ehdev.android.drivingtime.backend.model.Task;
import io.ehdev.android.drivingtime.database.dao.DatabaseHelper;
import io.ehdev.android.drivingtime.module.ModuleGetters;
import io.ehdev.android.drivingtime.view.dialog.InsertOrEditRecordDialog;
import io.ehdev.android.drivingtime.view.dialog.InsertRecordDialogNoUpdate;
import io.ehdev.android.drivingtime.view.fragments.TaskDrivingRecordReviewFragment;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.List;

public class ListEntriesForTaskActivity  extends Activity {
    public static final String TAG = ListEntriesForTaskActivity.class.getName();
    public static final int VIEW_ID = 1;
    private Task drivingTask;
    private TaskDrivingRecordReviewFragment taskFragmentView;

    @Inject
    protected DatabaseHelper databaseHelper;

    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        ObjectGraph objectGraph = ObjectGraph.create( ModuleGetters.getInstance());
        objectGraph.inject(this);

        doLoadWork(savedInstance);
    }

    private void doLoadWork(Bundle savedInstance) {
        try{
            String taskName = getTaskName(this.getIntent().getExtras().getInt("taskId"));

            createFragmentIfNotCreated(savedInstance);

            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setTitle(String.format("%s Driving Records", taskName));

            createViews();
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
            finish();
        }
    }

    private void createViews() {
        FrameLayout fl = new FrameLayout(this);
        fl.setId(VIEW_ID);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(VIEW_ID, taskFragmentView).commit();

        setContentView(fl);
    }

    private void createFragmentIfNotCreated(Bundle savedInstance) {
        if(savedInstance == null){
            taskFragmentView = new TaskDrivingRecordReviewFragment(drivingTask);
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
                createAddEntry();
                return true;
            case R.id.tasks:
                launchTaskActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private void createAddEntry() {
        try{
            FragmentManager fm = getFragmentManager();
            InsertOrEditRecordDialog insertRecordDialog = getInsertRecordDialog();
            insertRecordDialog.show(fm, "Insert Record Dialog");
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
    }

    private InsertOrEditRecordDialog getInsertRecordDialog() throws SQLException {
        List<Task> drivingTaskList = databaseHelper.getTaskDao().queryForAll();
        Record drivingRecord = new Record(drivingTaskList.get(0), new DateTime(), Duration.standardHours(1));
        return new InsertRecordDialogNoUpdate(drivingRecord, drivingTaskList, reloadView());
    }

    private void launchTaskActivity() {
        Intent taskIntent = new Intent();
        taskIntent.setClass(this, TaskConfigurationActivity.class);
        startActivity(taskIntent);
    }

    private InsertRecordDialogNoUpdate.ReloadView reloadView() {
        return new InsertRecordDialogNoUpdate.ReloadView(){

            @Override
            public void reload() {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(taskFragmentView);
                ft.attach(taskFragmentView);
                ft.commit();
            }
        };
    }

    public static class TaskNotValidException extends RuntimeException {
    }
}
