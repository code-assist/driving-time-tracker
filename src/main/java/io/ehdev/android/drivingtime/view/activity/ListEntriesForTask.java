package io.ehdev.android.drivingtime.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import io.ehdev.android.drivingtime.R;
import io.ehdev.android.drivingtime.adapter.DrivingRecordAdapter;
import io.ehdev.android.drivingtime.database.dao.DrivingRecordDao;
import io.ehdev.android.drivingtime.database.model.DrivingRecord;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListEntriesForTask extends SherlockFragmentActivity {
    public static final String TAG = ListEntriesForTask.class.getName();
    private DrivingRecordDao drivingRecordDao;
    private DrivingRecordAdapter drivingRecordAdapter;
    
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        List<DrivingRecord> drivingRecordList = getListOfEntries();
        setContentView(R.layout.list_view);
        drivingRecordAdapter = new DrivingRecordAdapter(this, drivingRecordList);
        ((ListView)findViewById(R.id.listOfAllRecords)).setAdapter(drivingRecordAdapter);
    }

    private List<DrivingRecord> getListOfEntries() {
        try{
            int taskId = this.getIntent().getExtras().getInt("taskId");
            drivingRecordDao = new DrivingRecordDao(this);
            return drivingRecordDao.getDrivingRecordForTask(taskId);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i(TAG, e.getMessage());
            return new ArrayList<DrivingRecord>();
        }
    }
}
