package jfdi.test.parser;

import java.time.LocalDateTime;
import java.time.Month;

import jfdi.parser.Constants;
import jfdi.parser.DateTimeObject;
import jfdi.parser.DateTimeParser;
import jfdi.parser.exceptions.BadDateTimeException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DateTimeParserTest {
    final int year = 2017;
    final int month = 2;
    final int dayOfMonth = 26;
    final int hour = 23;
    final int minute = 50;

    DateTimeParser parser;

    @Before
    public void setupParser() {
        parser = DateTimeParser.getInstance();
    }

    // Relative date time queries
    // Deadline task
    // With no time specified
    @Test
    public void testParseRelativeQueries() {
        String dateTimeInput = "by next year";
        DateTimeObject res = null;
        try {
            res = parser.parseDateTime(dateTimeInput);
        } catch (BadDateTimeException e) {
            Assert.fail();
        }
        Assert.assertNull(res.getStartDateTime());
        Assert.assertEquals(year, res.getEndDateTime().getYear());

        dateTimeInput = "by 1 year later";
        res = null;
        try {
            res = parser.parseDateTime(dateTimeInput);
        } catch (BadDateTimeException e) {
            Assert.fail();
        }
        Assert.assertNull(res.getStartDateTime());
        Assert.assertEquals(year, res.getEndDateTime().getYear());

        dateTimeInput = "by 1 year later";
        res = null;
        try {
            res = parser.parseDateTime(dateTimeInput);
        } catch (BadDateTimeException e) {
            Assert.fail();
        }
        Assert.assertNull(res.getStartDateTime());
        Assert.assertEquals(year, res.getEndDateTime().getYear());
    }

    // Relative date time queries
    // Deadline task
    // With time specified
    @Test
    public void testParseRelativeQueries2() {
        String dateTimeInput = "by next year 11pm";
        DateTimeObject res = null;
        try {
            res = parser.parseDateTime(dateTimeInput);
        } catch (BadDateTimeException e) {
            Assert.fail();
        }
        Assert.assertNull(res.getStartDateTime());
        Assert.assertEquals(year, res.getEndDateTime().getYear());

        dateTimeInput = "by 2300hrs 1 year later";
        res = null;
        try {
            res = parser.parseDateTime(dateTimeInput);
        } catch (BadDateTimeException e) {
            Assert.fail();
        }
        Assert.assertNull(res.getStartDateTime());
        Assert.assertEquals(year, res.getEndDateTime().getYear());
        Assert.assertEquals(hour, res.getEndDateTime().getHour());

        dateTimeInput = "by next year, 2300h";
        res = null;
        try {
            res = parser.parseDateTime(dateTimeInput);
        } catch (BadDateTimeException e) {
            Assert.fail();
        }
        Assert.assertNull(res.getStartDateTime());
        Assert.assertEquals(year, res.getEndDateTime().getYear());
        Assert.assertEquals(hour, res.getEndDateTime().getHour());
    }

    // Explicit date time queries
    // Point task
    // With time specified
    @Test
    public void testParseExplicitQueries() {
        String dateTimeInput = "on 26th February 2017 2350hrs";
        DateTimeObject res = null;
        try {
            res = parser.parseDateTime(dateTimeInput);
        } catch (BadDateTimeException e) {
            Assert.fail();
        }
        LocalDateTime testDateTime = LocalDateTime.of(year, month, dayOfMonth,
            getCurrentHour(), getCurrentMinutes());
        Assert.assertEquals(testDateTime.toLocalDate(), res.getStartDateTime()
            .toLocalDate());
        Assert.assertEquals(hour, res.getStartDateTime().getHour());
        Assert.assertEquals(minute, res.getStartDateTime().getMinute());

    }

    // Explicit date time queries
    // Point Task
    // Without time specified
    @Test
    public void testParseExplicitQueries2() {
        String dateTimeInput = "on 26th February 2017";
        DateTimeObject res = null;
        try {
            res = parser.parseDateTime(dateTimeInput);
        } catch (BadDateTimeException e) {
            Assert.fail();
        }
        LocalDateTime testDateTime = LocalDateTime.of(year, month, dayOfMonth,
            getCurrentHour(), getCurrentMinutes());
        Assert.assertEquals(testDateTime.toLocalDate(), res.getStartDateTime()
            .toLocalDate());
        Assert.assertEquals(Constants.Time.DEFAULT_HOUR, res.getStartDateTime()
            .getHour());
        Assert.assertEquals(Constants.Time.DEFAULT_MINUTES, res
            .getStartDateTime().getMinute());

    }

    // Explicit date time queries
    // Event Task
    // Without time specified
    @Test
    public void testParseExplicitQueries3() {
        String dateTimeInput = "From 26th February 2017 to 28th December 2019";
        DateTimeObject res = null;
        try {
            res = parser.parseDateTime(dateTimeInput);
        } catch (BadDateTimeException e) {
            Assert.fail();
        }

        // Check start date time is corrently parsed
        Assert.assertEquals(2017, res.getStartDateTime().getYear());
        Assert.assertEquals(Month.FEBRUARY, res.getStartDateTime().getMonth());
        Assert.assertEquals(26, res.getStartDateTime().getDayOfMonth());
        Assert.assertEquals(Constants.TIME_BEGINNING_OF_DAY.nanoseconds, res
            .getStartDateTime().getNano());
        Assert.assertEquals(Constants.TIME_BEGINNING_OF_DAY.seconds, res
            .getStartDateTime().getSecond());
        Assert.assertEquals(Constants.TIME_BEGINNING_OF_DAY.minutes, res
            .getStartDateTime().getMinute());
        Assert.assertEquals(Constants.TIME_BEGINNING_OF_DAY.hour, res
            .getStartDateTime().getHour());

        // Check end date time is corrently parsed
        Assert.assertEquals(2019, res.getEndDateTime().getYear());
        Assert.assertEquals(Month.DECEMBER, res.getEndDateTime().getMonth());
        Assert.assertEquals(28, res.getEndDateTime().getDayOfMonth());
        Assert.assertEquals(Constants.TIME_BEGINNING_OF_DAY.nanoseconds, res
            .getStartDateTime().getNano());
        Assert.assertEquals(Constants.TIME_BEGINNING_OF_DAY.seconds, res
            .getStartDateTime().getSecond());
        Assert.assertEquals(Constants.TIME_BEGINNING_OF_DAY.minutes, res
            .getStartDateTime().getMinute());
        Assert.assertEquals(Constants.TIME_BEGINNING_OF_DAY.hour, res
            .getStartDateTime().getHour());

        dateTimeInput = "From 25/11/94 to 23/12/97";
        res = null;
        try {
            res = parser.parseDateTime(dateTimeInput);
        } catch (BadDateTimeException e) {
            Assert.fail();
        }

        // Check start date time is corrently parsed
        Assert.assertEquals(1994, res.getStartDateTime().getYear());
        Assert.assertEquals(Month.NOVEMBER, res.getStartDateTime().getMonth());
        Assert.assertEquals(25, res.getStartDateTime().getDayOfMonth());
        Assert.assertEquals(Constants.TIME_BEGINNING_OF_DAY.nanoseconds, res
            .getStartDateTime().getNano());
        Assert.assertEquals(Constants.TIME_BEGINNING_OF_DAY.seconds, res
            .getStartDateTime().getSecond());
        Assert.assertEquals(Constants.TIME_BEGINNING_OF_DAY.minutes, res
            .getStartDateTime().getMinute());
        Assert.assertEquals(Constants.TIME_BEGINNING_OF_DAY.hour, res
            .getStartDateTime().getHour());

        // Check end date time is corrently parsed
        Assert.assertEquals(1997, res.getEndDateTime().getYear());
        Assert.assertEquals(Month.DECEMBER, res.getEndDateTime().getMonth());
        Assert.assertEquals(23, res.getEndDateTime().getDayOfMonth());
        Assert.assertEquals(Constants.TIME_BEGINNING_OF_DAY.nanoseconds, res
            .getStartDateTime().getNano());
        Assert.assertEquals(Constants.TIME_BEGINNING_OF_DAY.seconds, res
            .getStartDateTime().getSecond());
        Assert.assertEquals(Constants.TIME_BEGINNING_OF_DAY.minutes, res
            .getStartDateTime().getMinute());
        Assert.assertEquals(Constants.TIME_BEGINNING_OF_DAY.hour, res
            .getStartDateTime().getHour());
    }

    // Explicit date time queries
    // Event Task
    // With time specified
    @Test
    public void testParseExplicitQueries4() {
        String dateTimeInput = "From 26th February 2017 11pm to 11pm 28th December 2019";
        DateTimeObject res = null;
        try {
            res = parser.parseDateTime(dateTimeInput);
        } catch (BadDateTimeException e) {
            Assert.fail();
        }

        // Check start date time is corrently parsed
        Assert.assertEquals(2017, res.getStartDateTime().getYear());
        Assert.assertEquals(Month.FEBRUARY, res.getStartDateTime().getMonth());
        Assert.assertEquals(26, res.getStartDateTime().getDayOfMonth());
        Assert.assertEquals(Constants.TIME_BEGINNING_OF_DAY.nanoseconds, res
            .getStartDateTime().getNano());
        Assert.assertEquals(Constants.TIME_BEGINNING_OF_DAY.seconds, res
            .getStartDateTime().getSecond());
        Assert.assertEquals(Constants.TIME_BEGINNING_OF_DAY.minutes, res
            .getStartDateTime().getMinute());
        Assert.assertEquals(hour, res.getStartDateTime().getHour());

        // Check end date time is corrently parsed
        Assert.assertEquals(2019, res.getEndDateTime().getYear());
        Assert.assertEquals(Month.DECEMBER, res.getEndDateTime().getMonth());
        Assert.assertEquals(28, res.getEndDateTime().getDayOfMonth());
        Assert.assertEquals(Constants.TIME_BEGINNING_OF_DAY.nanoseconds, res
            .getStartDateTime().getNano());
        Assert.assertEquals(Constants.TIME_BEGINNING_OF_DAY.seconds, res
            .getStartDateTime().getSecond());
        Assert.assertEquals(Constants.TIME_BEGINNING_OF_DAY.minutes, res
            .getStartDateTime().getMinute());
        Assert.assertEquals(hour, res.getStartDateTime().getHour());

        dateTimeInput = "From 25/11/94 to 23/12/97";
        res = null;
        try {
            res = parser.parseDateTime(dateTimeInput);
        } catch (BadDateTimeException e) {
            Assert.fail();
        }

        // Check start date time is corrently parsed
        Assert.assertEquals(1994, res.getStartDateTime().getYear());
        Assert.assertEquals(Month.NOVEMBER, res.getStartDateTime().getMonth());
        Assert.assertEquals(25, res.getStartDateTime().getDayOfMonth());
        Assert.assertEquals(Constants.TIME_BEGINNING_OF_DAY.nanoseconds, res
            .getStartDateTime().getNano());
        Assert.assertEquals(Constants.TIME_BEGINNING_OF_DAY.seconds, res
            .getStartDateTime().getSecond());
        Assert.assertEquals(Constants.TIME_BEGINNING_OF_DAY.minutes, res
            .getStartDateTime().getMinute());
        Assert.assertEquals(Constants.TIME_BEGINNING_OF_DAY.hour, res
            .getStartDateTime().getHour());

        // Check end date time is corrently parsed
        Assert.assertEquals(1997, res.getEndDateTime().getYear());
        Assert.assertEquals(Month.DECEMBER, res.getEndDateTime().getMonth());
        Assert.assertEquals(23, res.getEndDateTime().getDayOfMonth());
        Assert.assertEquals(Constants.TIME_BEGINNING_OF_DAY.nanoseconds, res
            .getStartDateTime().getNano());
        Assert.assertEquals(Constants.TIME_BEGINNING_OF_DAY.seconds, res
            .getStartDateTime().getSecond());
        Assert.assertEquals(Constants.TIME_BEGINNING_OF_DAY.minutes, res
            .getStartDateTime().getMinute());
        Assert.assertEquals(Constants.TIME_BEGINNING_OF_DAY.hour, res
            .getStartDateTime().getHour());
    }

    private int getCurrentYear() {
        return LocalDateTime.now().getYear();
    }

    private Month getCurrentMonth() {
        return LocalDateTime.now().getMonth();
    }

    private int getCurrentDay() {
        return LocalDateTime.now().getDayOfMonth();
    }

    private int getCurrentHour() {
        return LocalDateTime.now().getHour();
    }

    private int getCurrentMinutes() {
        return LocalDateTime.now().getMinute();
    }

}
