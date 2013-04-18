package io.ehdev.android.drivingtime.database.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import io.ehdev.android.drivingtime.backend.model.Task;

import java.sql.SQLException;

public class DrivingTaskDao extends DatabaseHelper<Task> {

    private static final String TAG = DrivingTaskDao.class.getSimpleName();
    public DrivingTaskDao(Context context) {
        super(context, Task.class);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try{
            TableUtils.createTable(connectionSource, Task.class);
        } catch (SQLException e) {
            Log.i(TAG, "Unable to create table");
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addDrivingTask(Task drivingTask) throws SQLException {
        getDao().createIfNotExists(drivingTask);
    }

}
