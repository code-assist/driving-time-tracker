package io.ehdev.android.drivingtime.backend.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;

import java.text.DateFormat;


@DatabaseTable(tableName= Record.TABLE_NAME)
public class Record {

    public final static String TABLE_NAME = "record";
    public final static String DRIVING_TASK_COLUMN_NAME = "task";

    @DatabaseField(foreign = true, columnName = DRIVING_TASK_COLUMN_NAME)
    private Task drivingTask;

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(dataType = DataType.LONG)
    private long startTime;

    @DatabaseField(dataType=DataType.SERIALIZABLE)
    private Duration durationOfDriving;

    @DatabaseField(canBeNull = false, dataType= DataType.SERIALIZABLE)
    private DateTimeZone timeZone;

    public Record(Task drivingTask, DateTime startTime, Duration durationOfDriving) {
        this.drivingTask = drivingTask;
        this.startTime = startTime.getMillis();
        this.timeZone = startTime.getZone();
        this.durationOfDriving = durationOfDriving;
    }

    public Record() {
    }

    public int getId() {
        return id;
    }

    public Task getDrivingTask() {
        return drivingTask;
    }

    public void setDrivingTask(Task drivingTask) {
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

    public String createStringFromDate(DateFormat dateFormatter){
        return dateFormatter.format(getStartTime());
    }
}
