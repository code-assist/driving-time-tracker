package io.ehdev.android.drivingtime.database.dao;

import android.util.Log;
import io.ehdev.android.drivingtime.backend.AggregatedRecord;
import io.ehdev.android.drivingtime.backend.model.Record;
import io.ehdev.android.drivingtime.backend.model.Task;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AggregatedDrivingRecordDAO {
    private final DrivingRecordDao drivingRecordDao;
    private final DrivingTaskDao drivingTaskDao;

    private final static String TAG = AggregatedDrivingRecordDAO.class.getSimpleName();

    public AggregatedDrivingRecordDAO(DrivingRecordDao drivingRecordDao, DrivingTaskDao drivingTaskDao){

        this.drivingRecordDao = drivingRecordDao;
        this.drivingTaskDao = drivingTaskDao;
    }

    public List<AggregatedRecord> createDrivingRecordList(){
        try{
            ArrayList<AggregatedRecord> aggregatedDrivingRecordList = createAggregatedList();

            return aggregatedDrivingRecordList;
        } catch (Exception e){
            Log.i(TAG, e.getMessage());
            return new ArrayList<AggregatedRecord>();
        }
    }

    private ArrayList<AggregatedRecord> createAggregatedList() throws SQLException {
        ArrayList<AggregatedRecord> aggregatedDrivingRecordList = new ArrayList<AggregatedRecord>();
        for(Task drivingTask : drivingTaskDao.getDao().queryForAll()){
            aggregatedDrivingRecordList.add(new AggregatedRecord(drivingTask, drivingRecordDao.getDao().queryForEq(Record.DRIVING_TASK_COLUMN_NAME, drivingTask.getId())));
        }
        return aggregatedDrivingRecordList;
    }

    public DrivingRecordDao getDrivingRecordDao() {
        return drivingRecordDao;
    }

    public DrivingTaskDao getDrivingTaskDao() {
        return drivingTaskDao;
    }
}
