package io.ehdev.android.drivingtime.database.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import io.ehdev.android.drivingtime.database.model.DrivingRecord;

import java.sql.SQLException;

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
}
