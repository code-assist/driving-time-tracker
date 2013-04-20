package io.ehdev.android.drivingtime.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import io.ehdev.android.drivingtime.R;
import io.ehdev.android.drivingtime.adapter.DrivingRecordAdapter;
import io.ehdev.android.drivingtime.backend.model.Record;
import io.ehdev.android.drivingtime.database.dao.DrivingRecordDao;
import io.ehdev.android.drivingtime.database.dao.DrivingTaskDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListEntriesForTaskActivity  extends Activity {
    public static final String TAG = ListEntriesForTaskActivity.class.getName();
    private DrivingRecordDao drivingRecordDao;
    private DrivingRecordAdapter drivingRecordAdapter;
    
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        List<Record> drivingRecordList = getListOfEntries();
        setContentView(R.layout.list_view);
        drivingRecordAdapter = new DrivingRecordAdapter(this, drivingRecordList);
        ((ListView)findViewById(R.id.listOfAllRecords)).setAdapter(drivingRecordAdapter);
    }

    private List<Record> getListOfEntries() {
        try{
            int taskId = this.getIntent().getExtras().getInt("taskId");
            drivingRecordDao = new DrivingRecordDao(this);
            DrivingTaskDao drivingTaskDao = new DrivingTaskDao(this);
            return drivingRecordDao.getDrivingRecordForTask(taskId, drivingTaskDao.getDao());
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i(TAG, e.getMessage());
            return new ArrayList<Record>();
        }
    }
}
