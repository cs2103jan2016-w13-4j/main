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

    // month[1] represents the first month (Jan), month[2] represents Feb, etc
    static final Month[] MONTH = {null, Month.JANUARY, Month.FEBRUARY,
        Month.MARCH, Month.APRIL, Month.MAY, Month.JUNE, Month.JULY,
        Month.AUGUST, Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER,
        Month.DECEMBER};

    // Time values specifying the beginning of day
    static final int BEGINNING_NANO = Constants.TIME_BEGINNING_OF_DAY.nanoseconds;
    static final int BEGINNING_SECONDS = Constants.TIME_BEGINNING_OF_DAY.seconds;
    static final int BEGINNING_MINUTES = Constants.TIME_BEGINNING_OF_DAY.minutes;
    static final int BEGINNING_HOUR = Constants.TIME_BEGINNING_OF_DAY.hour;

    // Default time values for date time inputs without specified time
    static final int DEFAULT_NANO = Constants.TIME_DEFAULT.nanoseconds;
    static final int DEFAULT_SECONDS = Constants.TIME_DEFAULT.seconds;
    static final int DEFAULT_MINUTES = Constants.TIME_DEFAULT.minutes;
    static final int DEFAULT_HOUR = Constants.TIME_DEFAULT.hour;

    // Time values specifying the end of day
    static final int END_NANO = Constants.TIME_END_OF_DAY.nanoseconds;
    static final int END_SECONDS = Constants.TIME_END_OF_DAY.seconds;
    static final int END_MINUTES = Constants.TIME_END_OF_DAY.minutes;
    static final int END_HOUR = Constants.TIME_END_OF_DAY.hour;

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
        DateTimeObject res = parseDateTime("by next year");

        // Start date-time should be null
        Assert.assertNull(res.getStartDateTime());

        // Check end date time is corrently parsed
        checkMatchingDateTime(res.getEndDateTime(), getCurrentYear() + 1,
            getCurrentMonth(), getCurrentDay(), DEFAULT_HOUR, DEFAULT_MINUTES);

        res = parseDateTime("by 1 week later");

        // Start date-time should be null
        Assert.assertNull(res.getStartDateTime());

        // Check end date time is corrently parsed
        checkMatchingDateTime(res.getEndDateTime(), getCurrentYear(),
            getCurrentMonth(), getCurrentDay() + 7, DEFAULT_HOUR,
            DEFAULT_MINUTES);

        res = parseDateTime("by tomorrow");

        // Start date-time should be null
        Assert.assertNull(res.getStartDateTime());

        // Check end date time is corrently parsed
        checkMatchingDateTime(res.getEndDateTime(), getCurrentYear(),
            getCurrentMonth(), getCurrentDay() + 1, DEFAULT_HOUR,
            DEFAULT_MINUTES);
    }

    // Relative date time queries
    // Deadline task
    // With time specified
    @Test
    public void testParseRelativeQueries2() {
        DateTimeObject res = parseDateTime("by next week 11pm");

        // Start date-time should be null
        Assert.assertNull(res.getStartDateTime());

        // Check end date time is corrently parsed
        checkMatchingDateTime(res.getEndDateTime(), getCurrentYear(),
            getCurrentMonth(), getCurrentDay() + 7, 23, 00);

        res = parseDateTime("by 18:32h, tomorrow");

        // Start date-time should be null
        Assert.assertNull(res.getStartDateTime());

        // Check end date time is corrently parsed
        checkMatchingDateTime(res.getEndDateTime(), getCurrentYear(),
            getCurrentMonth(), getCurrentDay() + 1, 18, 32);

        res = parseDateTime("by 3 days later, 23:00h");

        // Start date-time should be null
        Assert.assertNull(res.getStartDateTime());

        // Check end date time is corrently parsed
        checkMatchingDateTime(res.getEndDateTime(), getCurrentYear(),
            getCurrentMonth(), getCurrentDay() + 3, 23, 0);

        res = parseDateTime("by 23:00h 3 days later");

        // Start date-time should be null
        Assert.assertNull(res.getStartDateTime());

        // Check end date time is corrently parsed
        checkMatchingDateTime(res.getEndDateTime(), getCurrentYear(),
            getCurrentMonth(), getCurrentDay() + 3, 23, 0);

    }

    // Explicit date time queries
    // Point task
    // With time specified
    @Test
    public void testParseExplicitQueries() {
        DateTimeObject res = parseDateTime("on 26th February, 9.30pm");

        // End date-time should be null
        Assert.assertNull(res.getEndDateTime());

        checkMatchingDateTime(res.getStartDateTime(), 2016, MONTH[2], 26, 21,
            30);

        res = parseDateTime("on 22nd july, 21:00h");

        // End date-time should be null
        Assert.assertNull(res.getEndDateTime());

        // Check end date time is corrently parsed
        checkMatchingDateTime(res.getStartDateTime(), 2016, MONTH[7], 22, 21,
            00);
    }

    // Explicit date time queries
    // Point Task
    // Without time specified
    @Test
    public void testParseExplicitQueries2() {
        DateTimeObject res = parseDateTime("on 22/07/17");

        // End date-time should be null
        Assert.assertNull(res.getEndDateTime());

        // Check end date time is corrently parsed
        checkMatchingDateTime(res.getStartDateTime(), 2017, MONTH[7], 22,
            DEFAULT_HOUR, DEFAULT_MINUTES);

        res = parseDateTime("on 09-Jan-2022");

        // End date-time should be null
        Assert.assertNull(res.getEndDateTime());

        // Check end date time is corrently parsed
        checkMatchingDateTime(res.getStartDateTime(), 2022, MONTH[1], 9,
            DEFAULT_HOUR, DEFAULT_MINUTES);
    }

    // Explicit date time queries
    // Event Task
    // Without time specified
    @Test
    public void testParseExplicitQueries3() {
        DateTimeObject res = parseDateTime("From 26th February 2017 to 28th December 2019");

        // Check start date time is corrently parsed
        checkMatchingDateTime(res.getStartDateTime(), 2017, MONTH[2], 26,
            BEGINNING_HOUR, BEGINNING_MINUTES);

        // Check end date time is corrently parsed
        checkMatchingDateTime(res.getEndDateTime(), 2019, MONTH[12], 28,
            END_HOUR, END_MINUTES);

        res = parseDateTime("From 25/11/94 to 23/12/97");

        // Check start date time is corrently parsed
        checkMatchingDateTime(res.getStartDateTime(), 1994, MONTH[11], 25,
            BEGINNING_HOUR, BEGINNING_MINUTES);

        // Check end date time is corrently parsed
        checkMatchingDateTime(res.getEndDateTime(), 1997, MONTH[12], 23,
            END_HOUR, END_MINUTES);

        res = parseDateTime("From 3pm to 8pm");

        // Check start date time is corrently parsed
        checkMatchingDateTime(res.getStartDateTime(), getCurrentYear(),
            getCurrentMonth(), getCurrentDay(), 15, 00);

        // Check end date time is corrently parsed
        checkMatchingDateTime(res.getEndDateTime(), getCurrentYear(),
            getCurrentMonth(), getCurrentDay(), 20, 00);
    }

    // Explicit date time queries
    // Event Task
    // With time specified
    @Test
    public void testParseExplicitQueries4() {
        DateTimeObject res = parseDateTime("From 26th.February.2017 11pm to 11pm 28th December 2019");

        // Check start date time is corrently parsed
        checkMatchingDateTime(res.getStartDateTime(), 2017, MONTH[2], 26, 23,
            DEFAULT_MINUTES);
        // Check end date time is corrently parsed
        checkMatchingDateTime(res.getEndDateTime(), 2019, MONTH[12], 28, 23,
            DEFAULT_MINUTES);

        res = parseDateTime("From 25/11 23.12hr to 12:34hrs, 23/12");

        // Check start date time is corrently parsed
        checkMatchingDateTime(res.getStartDateTime(), 2016, Month.NOVEMBER, 25,
            23, 12);

        // Check end date time is corrently parsed
        checkMatchingDateTime(res.getEndDateTime(), 2016, Month.DECEMBER, 23,
            12, 34);
    }

    @Test(expected = BadDateTimeException.class)
    public void testParseInvalid1() throws BadDateTimeException {
        parser.parseDateTime("no date time format");
    }

    @Test(expected = BadDateTimeException.class)
    public void testParseInvalid2() throws BadDateTimeException {
        parser.parseDateTime("by 42/01/99");
    }

    @Test(expected = BadDateTimeException.class)
    public void testParseInvalid3() throws BadDateTimeException {
        parser.parseDateTime("by Wednesday Thursday");
    }

    @Test(expected = BadDateTimeException.class)
    public void testParseInvalid4() throws BadDateTimeException {
        parser.parseDateTime("from 3pm and 9pm");
    }

    private DateTimeObject parseDateTime(String input) {
        DateTimeObject res = null;
        try {
            res = parser.parseDateTime(input);
        } catch (BadDateTimeException e) {
            Assert.assertTrue(true);
        }

        return res;
    }

    private void checkMatchingDateTime(LocalDateTime res, int year,
        Month month, int day, int hour, int minutes) {
        Assert.assertNotNull(res);
        Assert.assertEquals(year, res.getYear());
        Assert.assertEquals(month, res.getMonth());
        Assert.assertEquals(day, res.getDayOfMonth());
        Assert.assertEquals(minutes, res.getMinute());
        Assert.assertEquals(hour, res.getHour());
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
