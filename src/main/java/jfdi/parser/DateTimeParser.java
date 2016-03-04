package jfdi.parser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

/**
 * DateTimeParser is a class used to parse a string input into one a list of
 * LocalDateTime objects it represents. It supports parsing strings of different
 * formats. For example, it can parse "by 23rd February 2015" or "from 23/02/15
 * 2359hrs to 29/03/16 09:40pm". For an idea of the format of String parseable,
 * see parser.Constants.java.
 *
 * @author Leonard Hio
 *
 */
public class DateTimeParser {
    private static DateTimeParser dateTimeParser;

    public static DateTimeParser getInstance() {
        return (dateTimeParser == null) ? dateTimeParser = new DateTimeParser()
                : dateTimeParser;
    }

    /**
     * This method parses the given input string into a list of LocalDateTime
     * objects.
     *
     * @param input
     *            a string that should match the date-time format listed in
     *            parser.Constants.java.
     * @return a list of localDateTime objects, parsed from the input string.
     *         The list can be of size 0, which indicates the input string
     *         contains no parseable date time String.
     */
    public List<LocalDateTime> parseDateTime(String input) {
        PrettyTimeParser parser = new PrettyTimeParser();
        List<Date> dateTimeList = parser.parse(input);
        List<LocalDateTime> ldtList = new ArrayList<LocalDateTime>();
        for (Date d : dateTimeList) {
            ldtList.add(getLocalDateTimeFromDate(d));
        }

        ldtList.forEach(System.out::println);
        return ldtList;
    }

    /**
     * This method reformats a given Date object to a LocalDateTime object. The
     * zone is taken to be the one found in the system.
     *
     * @param d
     *            a Date object
     * @return the LocalDateTime object formatted from the Date object.
     */
    public LocalDateTime getLocalDateTimeFromDate(Date d) {
        return LocalDateTime.ofInstant(d.toInstant(), Constants.ZONE_ID);
    }

    public static void main(String[] args) {
        DateTimeParser parser = DateTimeParser.getInstance();
        System.out.println(parser.parseDateTime("12/23/1993"));
    }
}
