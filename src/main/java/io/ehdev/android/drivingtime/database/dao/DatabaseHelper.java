package io.ehdev.android.drivingtime.database.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import io.ehdev.android.drivingtime.backend.AggregatedRecord;
import io.ehdev.android.drivingtime.backend.model.Record;
import io.ehdev.android.drivingtime.backend.model.Task;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    public static final int VERSION_ONE = 2;
    private static final String DATABASE_NAME = "driving.log.db";

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private Dao<Record, Integer> recordDao;
    private Dao<Task, Integer> taskDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_ONE);
    }

    public Dao<Record, Integer> getRecordDao() throws SQLException {
        if(null == recordDao)
            recordDao = getDao(Record.class);
        return recordDao;
    }

    public Dao<Task, Integer> getTaskDao() throws SQLException {
        if(null == taskDao)
            taskDao = getDao(Task.class);
        return taskDao;
    }

    @Override
    public void close(){
        super.close();
        taskDao = null;
        recordDao = null;
    }

    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource){
        try{
            TableUtils.createTable(connectionSource, Record.class);
            TableUtils.createTable(connectionSource, Task.class);
        } catch (SQLException e) {
            Log.i(TAG, "Unable to create table");
            Log.d(TAG, e.getMessage());
        }
    }

    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion){

    }

    public List<Record> getDrivingRecordForTask(int taskId, Dao<Task, Integer> drivingTaskDao) throws SQLException {
        QueryBuilder<Record, Integer> qb = getRecordDao().queryBuilder();
        Where where = qb.where();
        where.eq(Record.DRIVING_TASK_COLUMN_NAME, taskId);
        PreparedQuery<Record> pq = where.prepare();
        List<Record> drivingRecordList = getRecordDao().query(pq);
        for(Record record : drivingRecordList)
            drivingTaskDao.refresh(record.getDrivingTask());
        return new ArrayList<Record>(drivingRecordList);
    }

    public Task getTaskFromId(int id) throws SQLException {
        return getTaskDao().queryForId(id);
    }

    public List<AggregatedRecord> createDrivingRecordList(){
        try{
            return createAggregatedList();
        } catch (Exception e){
            Log.i(TAG, e.getMessage());
            return new ArrayList<AggregatedRecord>();
        }
    }

    private ArrayList<AggregatedRecord> createAggregatedList() throws SQLException {
        ArrayList<AggregatedRecord> aggregatedDrivingRecordList = new ArrayList<AggregatedRecord>();
        for(Task drivingTask : getTaskDao().queryForAll()){
            aggregatedDrivingRecordList.add(new AggregatedRecord(drivingTask, getRecordDao().queryForEq(Record.DRIVING_TASK_COLUMN_NAME, drivingTask.getId())));
        }
        return aggregatedDrivingRecordList;
    }
}
