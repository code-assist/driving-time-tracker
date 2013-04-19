package io.ehdev.android.drivingtime.backend.model;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RecordTest {

    @Test
    public void testDurationStringIsCorrectWithMin(){
        Record record = new Record(new Task(), DateTime.now(), Duration.standardMinutes(130));
        assertEquals("2:10", record.getDurationAsString());
    }

    @Test
    public void testDurationStringIsCorrectOnlyHour(){
        Record record = new Record(new Task(), DateTime.now(), Duration.standardMinutes(120));
        assertEquals("2:00", record.getDurationAsString());
    }
}
