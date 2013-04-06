package io.ehdev.android.drivingtime.database.dao;

import android.util.Log;
import io.ehdev.android.drivingtime.adapter.pojo.AggregatedDrivingRecord;
import io.ehdev.android.drivingtime.database.model.DrivingRecord;
import io.ehdev.android.drivingtime.database.model.DrivingTask;
import org.joda.time.Duration;

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

    public List<AggregatedDrivingRecord> createDrivingRecordList(){
        try{
            ArrayList<AggregatedDrivingRecord> aggregatedDrivingRecordList = new ArrayList<AggregatedDrivingRecord>();
            createAggregatedList(aggregatedDrivingRecordList);
            return aggregatedDrivingRecordList;
        } catch (Exception e){
            Log.i(TAG, e.getMessage());
            return new ArrayList<AggregatedDrivingRecord>();
        }
    }

    private void createAggregatedList(ArrayList<AggregatedDrivingRecord> aggregatedDrivingRecordList) throws SQLException {

        for(DrivingTask drivingTask : drivingTaskDao.getDao().queryForAll()){
            Duration totalDuration = new Duration(0);
            for(DrivingRecord drivingRecord : drivingRecordDao.getDao().queryForEq(DrivingRecord.DRIVING_TASK_COLUMN_NAME, drivingTask.getId())){
                totalDuration = totalDuration.plus(drivingRecord.getDurationOfDriving());
            }
            aggregatedDrivingRecordList.add(
                    new AggregatedDrivingRecord(
                            drivingTask.getTaskName(),
                            drivingTask.getRequiredHours(),
                            totalDuration));
        }
    }

    public DrivingRecordDao getDrivingRecordDao() {
        return drivingRecordDao;
    }

    public DrivingTaskDao getDrivingTaskDao() {
        return drivingTaskDao;
    }
}
