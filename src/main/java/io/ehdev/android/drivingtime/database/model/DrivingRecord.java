package io.ehdev.android.drivingtime.database.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;


@DatabaseTable(tableName = DrivingRecord.TableName)
public class DrivingRecord {
    public final static String TableName = "drivingRecord";
    public final static String DRIVING_TASK_COLUMN_NAME = "drivingTask";

    @DatabaseField(foreign = true, columnName = DRIVING_TASK_COLUMN_NAME)
    private DrivingTask drivingTask;

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(dataType = DataType.LONG)
    private long startTime;

    @DatabaseField(dataType=DataType.SERIALIZABLE)
    private Duration durationOfDriving;

    @DatabaseField(canBeNull = false, dataType= DataType.SERIALIZABLE)
    private DateTimeZone timeZone;

    public DrivingRecord(DrivingTask drivingTask, DateTime startTime, Duration durationOfDriving) {
        this.drivingTask = drivingTask;
        this.startTime = startTime.getMillis();
        this.timeZone = startTime.getZone();
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
        return new DateTime(startTime, timeZone);
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime.getMillis();
        this.timeZone = startTime.getZone();
    }

    public Duration getDurationOfDriving() {
        return durationOfDriving;
    }

    public void setDurationOfDriving(Duration durationOfDriving) {
        this.durationOfDriving = durationOfDriving;
    }
}
