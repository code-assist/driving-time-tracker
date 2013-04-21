package io.ehdev.android.drivingtime.backend;

import org.joda.time.Duration;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringHelperTest {
    @Test
    public void testGetPeriodAsString() throws Exception {
        assertEquals("0:00", StringHelper.getPeriodAsString(0));
        assertEquals("0:00", StringHelper.getPeriodAsString(-1000));
        assertEquals("1:00", StringHelper.getPeriodAsString(Duration.standardHours(1).getMillis()));
        assertEquals("1:30", StringHelper.getPeriodAsString(Duration.standardHours(1).plus(Duration.standardMinutes(30)).getMillis()));
        assertEquals("12:00", StringHelper.getPeriodAsString(Duration.standardHours(12).getMillis()));
    }
}
