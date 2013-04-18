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
import io.ehdev.android.drivingtime.backend.model.Record;
import io.ehdev.android.drivingtime.backend.model.Task;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DrivingRecordDao extends DatabaseHelper<Record> {

    private static final String TAG = DrivingRecordDao.class.getName();
    public DrivingRecordDao(Context context) {
        super(context, Record.class);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try{
            TableUtils.createTable(connectionSource, Record.class);
        } catch (SQLException e) {
            Log.i(TAG, "Unable to create table");
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        Log.i(TAG, "Unable to update table");
    }

    public void addDrivingRecord(Record drivingRecord) throws SQLException {
        getDao().createIfNotExists(drivingRecord);
    }

    public List<Record> getDrivingRecordForTask(int taskId, Dao<Task, Integer> drivingTaskDao) throws SQLException {
        QueryBuilder<Record, Integer> qb = getDao().queryBuilder();
        Where where = qb.where();
        where.eq(Record.DRIVING_TASK_COLUMN_NAME, taskId);
        PreparedQuery<Record> pq = where.prepare();
        List<Record> drivingRecordList = getDao().query(pq);
        for(Record record : drivingRecordList)
            drivingTaskDao.refresh(record.getDrivingTask());
        return new ArrayList<Record>(drivingRecordList);
    }
}
