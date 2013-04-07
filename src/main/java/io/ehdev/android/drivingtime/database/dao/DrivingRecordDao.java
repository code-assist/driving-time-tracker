package io.ehdev.android.drivingtime.database.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import io.ehdev.android.drivingtime.database.model.DrivingRecord;
import io.ehdev.android.drivingtime.database.model.DrivingTask;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DrivingRecordDao extends DatabaseHelper<DrivingRecord> {

    private static final String TAG = DrivingRecordDao.class.getName();
    public DrivingRecordDao(Context context) {
        super(context, DrivingRecord.class);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try{
            TableUtils.createTable(connectionSource, DrivingRecord.class);
        } catch (SQLException e) {
            Log.i(TAG, "Unable to create table");
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        Log.i(TAG, "Unable to update table");
    }

    public void addDrivingRecord(DrivingRecord drivingRecord) throws SQLException {
        getDao().createIfNotExists(drivingRecord);
    }

    public List<DrivingRecord> getDrivingRecordForTask(int taskId, Dao<DrivingTask, Integer> drivingTaskDao) throws SQLException {
        QueryBuilder<DrivingRecord, Integer> qb = getDao().queryBuilder();
        Where where = qb.where();
        where.eq(DrivingRecord.DRIVING_TASK_COLUMN_NAME, taskId);
        PreparedQuery<DrivingRecord> pq = where.prepare();
        List<DrivingRecord> drivingRecordList = getDao().query(pq);
        for(DrivingRecord record : drivingRecordList)
            drivingTaskDao.refresh(record.getDrivingTask());
        return new ArrayList<DrivingRecord>(drivingRecordList);
    }
}
