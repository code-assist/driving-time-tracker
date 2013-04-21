package io.ehdev.android.drivingtime;

import android.content.Context;
import com.j256.ormlite.dao.Dao;
import io.ehdev.android.drivingtime.backend.model.Record;
import io.ehdev.android.drivingtime.backend.model.Task;
import io.ehdev.android.drivingtime.database.dao.DrivingRecordDao;
import io.ehdev.android.drivingtime.database.dao.DrivingTaskDao;

import java.sql.SQLException;

public class StaticInstances {

    private DrivingRecordDao drivingRecordDao;
    private DrivingTaskDao drivingTaskDao;

    static StaticInstances instance = null;

    private StaticInstances(Context context) {
        drivingRecordDao = new DrivingRecordDao(context);
        drivingTaskDao = new DrivingTaskDao(context);
    }

    public static StaticInstances getInstance(Context context){
        if(instance == null)
            instance = new StaticInstances(context);
        return instance;
    }

    public static StaticInstances getInstance(){
        if(instance == null)
            throw new SingletonNotInitializedException();
        return instance;
    }

    public DrivingRecordDao getDrivingRecordHelper() {
        return drivingRecordDao;
    }

    public DrivingTaskDao getDrivingTaskHelper() {
        return drivingTaskDao;
    }

    public Dao<Record, Integer> getDrivingRecordDao() throws SQLException {
        return drivingRecordDao.getDao();
    }

    public Dao<Task, Integer> getDrivingTaskDao() throws SQLException{
        return drivingTaskDao.getDao();
    }

    private static class SingletonNotInitializedException extends RuntimeException {
    }
}
