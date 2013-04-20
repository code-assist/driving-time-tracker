package io.ehdev.android.drivingtime.backend.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

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

    public String getDurationAsString(){
        PeriodFormatter minutesAndSeconds = new PeriodFormatterBuilder()
                .appendHours()
                .appendSeparator(":")
                .printZeroAlways()
                .minimumPrintedDigits(2)
                .appendMinutes()
                .toFormatter();
        return minutesAndSeconds.print(durationOfDriving.toPeriod());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Record record = (Record) o;

        if (id != record.id) return false;
        if (startTime != record.startTime) return false;
        if (!drivingTask.equals(record.drivingTask)) return false;
        if (!durationOfDriving.equals(record.durationOfDriving)) return false;
        if (!timeZone.equals(record.timeZone)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = drivingTask.hashCode();
        result = 31 * result + id;
        result = 31 * result + (int) (startTime ^ (startTime >>> 32));
        result = 31 * result + durationOfDriving.hashCode();
        result = 31 * result + timeZone.hashCode();
        return result;
    }
}
