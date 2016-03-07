package jfditests.parsertests;

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

    /*
     * @Test public void testParseRelativeQueries() { String dateTimeInput =
     * "next year"; List<LocalDateTime> res =
     * parser.parseDateTime(dateTimeInput); Assert.assertTrue(res.size() == 1);
     * LocalDateTime testDateTime = LocalDateTime.of(getCurrentYear() + 1,
     * getCurrentMonth(), getCurrentDay(), getCurrentHour(),
     * getCurrentMinutes()); Assert.assertEquals(testDateTime.toLocalDate(),
     * res.get(0) .toLocalDate()); }
     */

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
