package jfditests.parsertests;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;
import java.util.List;

import jfdi.parser.DateTimeParser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DateTimeParserTest {
    DateTimeParser parser;

    @Before
    public void setupParser() {
        parser = DateTimeParser.getInstance();
    }

    @Test
    public void testParseRelativeQueries() {
        String dateTimeInput = "next year";
        List<LocalDateTime> res = parser.parseDateTime(dateTimeInput);
        Assert.assertTrue(res.size() == 1);
        LocalDateTime testDateTime = LocalDateTime.of(getCurrentYear() + 1,
                getCurrentMonth(), getCurrentDay(), getCurrentHour(),
                getCurrentMinutes());
        Assert.assertEquals(testDateTime.toLocalDate(), res.get(0)
                .toLocalDate());
    }

    @Test
    public void testParseExplicitQueries() {
        String dateTimeInput = "26th February 2017";
        List<LocalDateTime> res = parser.parseDateTime(dateTimeInput);
        Assert.assertTrue(res.size() == 1);
        LocalDateTime testDateTime = LocalDateTime.of(2017, 2, 26,
                getCurrentHour(), getCurrentMinutes());
        Assert.assertEquals(testDateTime.toLocalDate(), res.get(0)
                .toLocalDate());

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
