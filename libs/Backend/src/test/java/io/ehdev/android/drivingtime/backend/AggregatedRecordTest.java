package io.ehdev.android.drivingtime.backend;

import io.ehdev.android.drivingtime.backend.model.Record;
import io.ehdev.android.drivingtime.backend.model.Task;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class AggregatedRecordTest {

    private Task tempTask;
    private Record tempRecord;
    private ArrayList<Record> list;

    @Before
    public void setup() {
        tempTask = new Task("foo", Duration.standardMinutes(100));
        tempRecord = new Record(tempTask, DateTime.now(), Duration.standardMinutes(75));
        list = new ArrayList<Record>();
        list.add(tempRecord);
    }

    @Test
    public void testEmptyDuration() {
        AggregatedRecord aggregatedRecord = new AggregatedRecord(new Task(), new ArrayList<Record>());
        assertEquals(Duration.ZERO, aggregatedRecord.getDuration());
    }

    @Test(expected = AggregatedRecord.ValueCannotBeNullException.class)
    public void testThatExceptionWillBeThrowIfNullIsPassedInAsFirstParameter(){
        new AggregatedRecord(null, new ArrayList<Record>());
    }

    @Test(expected = AggregatedRecord.ValueCannotBeNullException.class)
    public void testThatExceptionWillBeThrowIfNullIsPassedInAsSecondParameter(){
        new AggregatedRecord(new Task(), null);
    }

    @Test
    public void testPercentage_ShouldBe75(){
        AggregatedRecord aggregatedRecord = new AggregatedRecord(tempTask, list);
        assertEquals(0.75f, aggregatedRecord.getPercentageComplete(), 0.001);
    }

    @Test
    public void testPercentage_ShouldBe100(){
        tempTask = new Task("foo", Duration.standardMinutes(100));
        tempRecord = new Record(tempTask, DateTime.now(), Duration.standardMinutes(150));
        list = new ArrayList<Record>();
        list.add(tempRecord);
        AggregatedRecord aggregatedRecord = new AggregatedRecord(tempTask, list);
        assertEquals(1f, aggregatedRecord.getPercentageComplete(), 0.001);
    }

    @Test
    public void testTimeLeft_ShouldBe25Min(){
        AggregatedRecord aggregatedRecord = new AggregatedRecord(tempTask, list);
        assertEquals(25, aggregatedRecord.timeLeft().getStandardMinutes());
    }

    @Test
    public void testTimeLeft_ShouldBe0Min(){
        tempTask = new Task("foo", Duration.standardMinutes(100));
        tempRecord = new Record(tempTask, DateTime.now(), Duration.standardMinutes(150));
        list = new ArrayList<Record>();
        list.add(tempRecord);
        AggregatedRecord aggregatedRecord = new AggregatedRecord(tempTask, list);
        assertEquals(0, aggregatedRecord.timeLeft().getStandardMinutes());
    }


}
