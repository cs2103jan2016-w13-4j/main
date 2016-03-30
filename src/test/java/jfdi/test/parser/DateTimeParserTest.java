// @@author A0127393B

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
    static final Month[] MONTH = {null, Month.JANUARY, Month.FEBRUARY, Month.MARCH, Month.APRIL, Month.MAY, Month.JUNE,
        Month.JULY, Month.AUGUST, Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER, Month.DECEMBER};

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
        LocalDateTime expectedDateTime = getCurrentDatePlus(1, 0, 0, 0, 0, 0);
        checkMatchingDateTime(res.getEndDateTime(), expectedDateTime.getYear(), expectedDateTime.getMonth(),
            expectedDateTime.getDayOfMonth(), DEFAULT_HOUR, DEFAULT_MINUTES);

        res = parseDateTime("by 1 month later");

        // Start date-time should be null
        Assert.assertNull(res.getStartDateTime());

        // Check end date time is corrently parsed
        expectedDateTime = getCurrentDatePlus(0, 1, 0, 0, 0, 0);
        checkMatchingDateTime(res.getEndDateTime(), expectedDateTime.getYear(), expectedDateTime.getMonth(),
            expectedDateTime.getDayOfMonth(), DEFAULT_HOUR, DEFAULT_MINUTES);

        res = parseDateTime("by tomorrow");

        // Start date-time should be null
        Assert.assertNull(res.getStartDateTime());

        // Check end date time is corrently parsed
        expectedDateTime = getCurrentDatePlus(0, 0, 0, 1, 0, 0);
        checkMatchingDateTime(res.getEndDateTime(), expectedDateTime.getYear(), expectedDateTime.getMonth(),
            expectedDateTime.getDayOfMonth(), DEFAULT_HOUR, DEFAULT_MINUTES);
    }

    // Relative date time queries
    // Deadline task
    // With time specified
    @Test
    public void testParseRelativeQueries2() {
        DateTimeObject res = parseDateTime("by 18.32h, tomorrow");

        // Start date-time should be null
        Assert.assertNull(res.getStartDateTime());

        // Check end date time is corrently parsed
        LocalDateTime expectedDateTime = getCurrentDatePlus(0, 0, 0, 1, 0, 0);
        checkMatchingDateTime(res.getEndDateTime(), expectedDateTime.getYear(), expectedDateTime.getMonth(),
            expectedDateTime.getDayOfMonth(), 18, 32);
        res = parseDateTime("by 3 days later, 1100pm");

        // Start date-time should be null
        Assert.assertNull(res.getStartDateTime());

        // Check end date time is corrently parsed
        expectedDateTime = getCurrentDatePlus(0, 0, 0, 3, 0, 0);
        checkMatchingDateTime(res.getEndDateTime(), expectedDateTime.getYear(), expectedDateTime.getMonth(),
            expectedDateTime.getDayOfMonth(), 23, 00);

        res = parseDateTime("by 23:00h 3 days later");

        // Start date-time should be null
        Assert.assertNull(res.getStartDateTime());

        // Check end date time is corrently parsed
        expectedDateTime = getCurrentDatePlus(0, 0, 0, 3, 0, 0);
        checkMatchingDateTime(res.getEndDateTime(), expectedDateTime.getYear(), expectedDateTime.getMonth(),
            expectedDateTime.getDayOfMonth(), 23, 00);

    }

    // Relative date time queries
    // Point task
    // With time specified
    @Test
    public void testParseRelativeQueries3() {
        DateTimeObject res = parseDateTime("next month 11pm");

        // Start date-time should be null
        Assert.assertNull(res.getEndDateTime());

        // Check end date time is corrently parsed
        LocalDateTime expectedDateTime = getCurrentDatePlus(0, 1, 0, 0, 0, 0);
        checkMatchingDateTime(res.getStartDateTime(), expectedDateTime.getYear(), expectedDateTime.getMonth(),
            expectedDateTime.getDayOfMonth(), 23, 00);

        res = parseDateTime("11pm, tomorrow");

        // Start date-time should be null
        Assert.assertNull(res.getEndDateTime());

        // Check end date time is corrently parsed
        expectedDateTime = getCurrentDatePlus(0, 0, 0, 1, 0, 0);
        checkMatchingDateTime(res.getStartDateTime(), expectedDateTime.getYear(), expectedDateTime.getMonth(),
            expectedDateTime.getDayOfMonth(), 23, 00);

        res = parseDateTime("3 hours later");

        // Start date-time should be null
        Assert.assertNull(res.getEndDateTime());

        // Check end date time is corrently parsed
        expectedDateTime = getCurrentDatePlus(0, 0, 0, 0, 3, 0);
        checkMatchingDateTime(res.getStartDateTime(), expectedDateTime.getYear(), expectedDateTime.getMonth(),
            expectedDateTime.getDayOfMonth(), expectedDateTime.getHour(), expectedDateTime.getMinute());

        res = parseDateTime("5 hours from now");

        // Start date-time should be null
        Assert.assertNull(res.getEndDateTime());

        // Check end date time is corrently parsed
        expectedDateTime = getCurrentDatePlus(0, 0, 0, 0, 5, 0);
        checkMatchingDateTime(res.getStartDateTime(), expectedDateTime.getYear(), expectedDateTime.getMonth(),
            expectedDateTime.getDayOfMonth(), expectedDateTime.getHour(), expectedDateTime.getMinute());

        res = parseDateTime("in 4 days' time");

        // Start date-time should be null
        Assert.assertNull(res.getEndDateTime());

        // Check end date time is corrently parsed
        expectedDateTime = getCurrentDatePlus(0, 0, 0, 4, 0, 0);
        checkMatchingDateTime(res.getStartDateTime(), expectedDateTime.getYear(), expectedDateTime.getMonth(),
            expectedDateTime.getDayOfMonth(), DEFAULT_HOUR, DEFAULT_MINUTES);

        res = parseDateTime("in 4 hours");

        // Start date-time should be null
        Assert.assertNull(res.getEndDateTime());

        // Check end date time is corrently parsed
        expectedDateTime = getCurrentDatePlus(0, 0, 0, 0, 4, 0);
        checkMatchingDateTime(res.getStartDateTime(), expectedDateTime.getYear(), expectedDateTime.getMonth(),
            expectedDateTime.getDayOfMonth(), expectedDateTime.getHour(), expectedDateTime.getMinute());

    }

    // Relative date time queries
    // Event Task
    // Without time specified
    @Test
    public void testParseRelativeQueries4() {
        DateTimeObject res = parseDateTime("From tomorrow to 4 days later");

        // Check start date time is corrently parsed
        LocalDateTime expectedDateTime = getCurrentDatePlus(0, 0, 0, 1, 0, 0);
        checkMatchingDateTime(res.getStartDateTime(), expectedDateTime.getYear(), expectedDateTime.getMonth(),
            expectedDateTime.getDayOfMonth(), BEGINNING_HOUR, BEGINNING_MINUTES);
        // Check end date time is corrently parsed
        expectedDateTime = getCurrentDatePlus(0, 0, 0, 4, 0, 0);
        checkMatchingDateTime(res.getEndDateTime(), expectedDateTime.getYear(), expectedDateTime.getMonth(),
            expectedDateTime.getDayOfMonth(), END_HOUR, END_MINUTES);

        res = parseDateTime("From yesterday to 3 weeks later");

        // Check start date time is corrently parsed
        expectedDateTime = getCurrentDatePlus(0, 0, 0, -1, 0, 0);
        checkMatchingDateTime(res.getStartDateTime(), expectedDateTime.getYear(), expectedDateTime.getMonth(),
            expectedDateTime.getDayOfMonth(), BEGINNING_HOUR, BEGINNING_MINUTES);

        // Check end date time is corrently parsed
        expectedDateTime = getCurrentDatePlus(0, 0, 3, 0, 0, 0);
        checkMatchingDateTime(res.getEndDateTime(), expectedDateTime.getYear(), expectedDateTime.getMonth(),
            expectedDateTime.getDayOfMonth(), END_HOUR, END_MINUTES);
    }

    // Explicit date time queries
    // Point task
    // With time specified
    @Test
    public void testParseExplicitQueries() {
        DateTimeObject res = parseDateTime("on 26th February 17, 9.30pm");

        // End date-time should be null
        Assert.assertNull(res.getEndDateTime());

        checkMatchingDateTime(res.getStartDateTime(), 2017, MONTH[2], 26, 21, 30);

        res = parseDateTime("on 22nd july, 21:00h");

        // End date-time should be null
        Assert.assertNull(res.getEndDateTime());

        // Check end date time is corrently parsed
        checkMatchingDateTime(res.getStartDateTime(), 2016, MONTH[7], 22, 21, 00);
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
        checkMatchingDateTime(res.getStartDateTime(), 2017, MONTH[7], 22, DEFAULT_HOUR, DEFAULT_MINUTES);

        res = parseDateTime("09-Jan-2022");

        // End date-time should be null
        Assert.assertNull(res.getEndDateTime());

        // Check end date time is corrently parsed
        checkMatchingDateTime(res.getStartDateTime(), 2022, MONTH[1], 9, DEFAULT_HOUR, DEFAULT_MINUTES);
    }

    // Explicit date time queries
    // Event Task
    // Without time specified
    @Test
    public void testParseExplicitQueries3() {
        DateTimeObject res = parseDateTime("26th February 17 to 28th December 2019");

        // Check start date time is corrently parsed
        checkMatchingDateTime(res.getStartDateTime(), 2017, MONTH[2], 26, BEGINNING_HOUR, BEGINNING_MINUTES);

        // Check end date time is corrently parsed
        checkMatchingDateTime(res.getEndDateTime(), 2019, MONTH[12], 28, END_HOUR, END_MINUTES);

        res = parseDateTime("From 25/11/94 to 23/12/97");

        // Check start date time is corrently parsed
        checkMatchingDateTime(res.getStartDateTime(), 1994, MONTH[11], 25, BEGINNING_HOUR, BEGINNING_MINUTES);

        // Check end date time is corrently parsed
        checkMatchingDateTime(res.getEndDateTime(), 1997, MONTH[12], 23, END_HOUR, END_MINUTES);

    }

    // Explicit date time queries
    // Event Task
    // With time specified
    @Test
    public void testParseExplicitQueries4() {
        DateTimeObject res = parseDateTime("From 26th.February.2017 9pm to 930pm 28th December 19");

        // Check start date time is corrently parsed
        checkMatchingDateTime(res.getStartDateTime(), 2017, MONTH[2], 26, 21, DEFAULT_MINUTES);
        // Check end date time is corrently parsed
        checkMatchingDateTime(res.getEndDateTime(), 2019, MONTH[12], 28, 21, 30);

        res = parseDateTime("From 25/11 23.12hr to 12:34hrs, 23/12");

        res = parseDateTime("From 26th.February.2017 to 930pm 28th December 19");

        // Check start date time is corrently parsed
        checkMatchingDateTime(res.getStartDateTime(), 2017, MONTH[2], 26, DEFAULT_HOUR, DEFAULT_MINUTES);
        // Check end date time is corrently parsed
        checkMatchingDateTime(res.getEndDateTime(), 2019, MONTH[12], 28, 21, 30);

        res = parseDateTime("From 25/11 23.12hr to 12:34hrs, 23/12");

        res = parseDateTime("From 26th.February.2017 2100hrs to 28th December 19");

        // Check start date time is corrently parsed
        checkMatchingDateTime(res.getStartDateTime(), 2017, MONTH[2], 26, 21, DEFAULT_MINUTES);
        // Check end date time is corrently parsed
        checkMatchingDateTime(res.getEndDateTime(), 2019, MONTH[12], 28, DEFAULT_HOUR, DEFAULT_MINUTES);

        res = parseDateTime("From 25/11 23.12hr to 12:34hrs, 23/12");

        // Check start date time is corrently parsed
        checkMatchingDateTime(res.getStartDateTime(), 2016, Month.NOVEMBER, 25, 23, 12);

        // Check end date time is corrently parsed
        checkMatchingDateTime(res.getEndDateTime(), 2016, Month.DECEMBER, 23, 12, 34);

        res = parseDateTime("From 3pm to 8pm");

        // Check start date time is corrently parsed
        LocalDateTime expectedDateTime = getCurrentDate();
        checkMatchingDateTime(res.getStartDateTime(), expectedDateTime.getYear(), expectedDateTime.getMonth(),
            expectedDateTime.getDayOfMonth(), 15, 00);

        // Check end date time is corrently parsed
        expectedDateTime = getCurrentDate();
        checkMatchingDateTime(res.getEndDateTime(), expectedDateTime.getYear(), expectedDateTime.getMonth(),
            expectedDateTime.getDayOfMonth(), 20, 00);

    }

    @Test
    public void testParseInvalid1() {
        try {
            parser.parseDateTime("no date time format");
        } catch (BadDateTimeException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testParseInvalid2() {
        try {
            parser.parseDateTime("by 42/01/99");
        } catch (BadDateTimeException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testParseInvalid3() {
        try {
            parser.parseDateTime("by Wednesday Thursday");
        } catch (BadDateTimeException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testParseInvalid4() {
        try {
            parser.parseDateTime("from 3pm and 9pm");
        } catch (BadDateTimeException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testParseInvalid5() {
        try {
            parser.parseDateTime("from 26/5/17 3pm to 22/5/17 9pm");
        } catch (BadDateTimeException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testParseInvalid6() {
        try {
            parser.parseDateTime("");
        } catch (BadDateTimeException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testParseInvalid7() {
        try {
            parser.parseDateTime(null);
        } catch (BadDateTimeException e) {
            Assert.assertTrue(true);
        }
    }

    private DateTimeObject parseDateTime(String input) {
        DateTimeObject res = null;
        try {
            res = parser.parseDateTime(input);
        } catch (BadDateTimeException e) {
            Assert.fail();
        }

        return res;
    }

    private void checkMatchingDateTime(LocalDateTime res, int year, Month month, int day, int hour, int minutes) {
        Assert.assertNotNull(res);
        Assert.assertEquals(year, res.getYear());
        Assert.assertEquals(month, res.getMonth());
        Assert.assertEquals(day, res.getDayOfMonth());
        Assert.assertEquals(minutes, res.getMinute());
        Assert.assertEquals(hour, res.getHour());
    }

    private LocalDateTime getCurrentDatePlus(int addYears, int addMonths, int addWeeks, int addDays, int addHours,
        int addMinutes) {
        return LocalDateTime.now().plusYears(addYears).plusMonths(addMonths).plusWeeks(addWeeks).plusDays(addDays)
            .plusHours(addHours).plusMinutes(addMinutes);
    }

    private LocalDateTime getCurrentDate() {
        return LocalDateTime.now();
    }

}
