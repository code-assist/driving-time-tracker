package io.ehdev.android.drivingtime.database.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public abstract class DatabaseHelper<T> extends OrmLiteSqliteOpenHelper {
    public static final int VERSION_ONE = 1;
    private static final String DATABASE_NAME = "driving.log.db";

    private Dao<T, Integer> dao;
    private Class<T> clazz;

    public DatabaseHelper(Context context, Class<T> clazz) {
        super(context, DATABASE_NAME, null, VERSION_ONE);
        this.clazz = clazz;
    }

    public Dao<T, Integer> getDao() throws SQLException {
        if(null == dao)
            dao = getDao(clazz);
        return dao;
    }

    @Override
    public void close(){
        super.close();
        dao = null;
    }

    abstract public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource);

    abstract public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion);
}
