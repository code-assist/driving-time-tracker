package io.ehdev.android.drivingtime.backend;

import io.ehdev.android.drivingtime.backend.model.Record;
import io.ehdev.android.drivingtime.backend.model.Task;
import org.joda.time.Duration;

import java.util.List;

public class AggregatedRecord {
    private final Task thisTask;
    private List<Record> listOfRecords;
    private Duration timeServed = null;

    public AggregatedRecord(Task thisTask, List<Record> listOfRecords){

        this.thisTask = thisTask;
        this.listOfRecords = listOfRecords;

        if(null == listOfRecords)
            throw new ValueCannotBeNullException("listOfRecords");
        else if(null == thisTask)
            throw new ValueCannotBeNullException("thisTask");
    }

    public Duration getDuration(){
        if(null == timeServed){
            timeServed = new Duration(0);
            for(Record record : listOfRecords){
                timeServed = timeServed.plus(record.getDurationOfDriving());
            }
        }

        return timeServed;
    }

    public String getTaskName(){
        return thisTask.getTaskName();
    }

    public int getTaskId(){
        return thisTask.getId();
    }

    public Duration getRequiredTime(){
        return thisTask.getRequiredHours();
    }

    public float getPercentageComplete(){
        float result = (float)getDuration().getMillis() / thisTask.getRequiredHours().getMillis();
        result = (float)((int)(result * 1000)) / 1000;
        return result > 1 ? 1 : result;
    }

    public Duration timeLeft(){
        Duration timeServed = getDuration();
        if(timeServed.isLongerThan(thisTask.getRequiredHours()))
            return Duration.ZERO;
        else
            return thisTask.getRequiredHours().minus(timeServed);
    }

    public static class ValueCannotBeNullException extends RuntimeException {
        ValueCannotBeNullException(String parameter){
            super(parameter);
        }
    }

    public String getRemainingTime(){
        return StringHelper.getPeriodAsString(getRequiredTime().toPeriod());
    }

}
