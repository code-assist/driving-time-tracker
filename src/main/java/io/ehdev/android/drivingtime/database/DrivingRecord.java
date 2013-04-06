package io.ehdev.android.drivingtime.database;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.joda.time.DateTime;

@DatabaseTable(tableName = DrivingRecord.TableName)
public class DrivingRecord {
    public final static String TableName = "drivingRecord";

    @DatabaseField(foreign = true)
    private DrivingTask drivingTask;

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(dataType = DataType.DATE_LONG)
    private DateTime startTime;

    @DatabaseField
    private long durationOfDriving;

    public DrivingRecord(DrivingTask drivingTask, DateTime startTime, long durationOfDriving) {
        this.drivingTask = drivingTask;
        this.startTime = startTime;
        this.durationOfDriving = durationOfDriving;
    }

    public DrivingRecord() {
    }

    public int getId() {
        return id;
    }

    public DrivingTask getDrivingTask() {
        return drivingTask;
    }

    public void setDrivingTask(DrivingTask drivingTask) {
        this.drivingTask = drivingTask;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public long getDurationOfDriving() {
        return durationOfDriving;
    }

    public void setDurationOfDriving(long durationOfDriving) {
        this.durationOfDriving = durationOfDriving;
    }
}
