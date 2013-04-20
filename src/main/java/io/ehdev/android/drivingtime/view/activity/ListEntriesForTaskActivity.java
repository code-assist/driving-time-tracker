package io.ehdev.android.drivingtime.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import io.ehdev.android.drivingtime.R;
import io.ehdev.android.drivingtime.adapter.DrivingRecordAdapter;
import io.ehdev.android.drivingtime.backend.model.Record;
import io.ehdev.android.drivingtime.backend.model.Task;
import io.ehdev.android.drivingtime.database.dao.DrivingRecordDao;
import io.ehdev.android.drivingtime.database.dao.DrivingTaskDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListEntriesForTaskActivity  extends Activity {
    public static final String TAG = ListEntriesForTaskActivity.class.getName();
    private DrivingRecordAdapter drivingRecordAdapter;

    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        int taskId = this.getIntent().getExtras().getInt("taskId");
        String taskName = getTaskName(taskId);

        List <Record> drivingRecordList = getListOfEntries(taskId);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(String.format("%s Driving Records", taskName));

        setViewLogic(drivingRecordList);
    }

    private String getTaskName(int taskId) {
        try{
            Task thisTask = new DrivingTaskDao(this).getTaskFromId(taskId);
            if(thisTask != null)
                return thisTask.getTaskName();
            else
                return "";
        } catch (SQLException e) {
            return "";
        }
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

    private void setViewLogic(List <Record> drivingRecordList) {
        setContentView(R.layout.aggregated_list_view);
        drivingRecordAdapter = new DrivingRecordAdapter(this, drivingRecordList);
        ((ListView)findViewById(R.id.listOfAllRecords)).setAdapter(drivingRecordAdapter);
    }

    private List<Record> getListOfEntries(int taskId) {
        try{
            DrivingTaskDao drivingTaskDao = new DrivingTaskDao(this);
            DrivingRecordDao drivingRecordDao = new DrivingRecordDao(this);
            return drivingRecordDao.getDrivingRecordForTask(taskId, drivingTaskDao.getDao());
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i(TAG, e.getMessage());
            return new ArrayList<Record>();
        }
    }
}
