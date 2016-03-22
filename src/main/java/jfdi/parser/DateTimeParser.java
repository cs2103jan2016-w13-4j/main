package jfdi.parser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jfdi.common.utilities.JfdiLogger;
import jfdi.parser.Constants.TaskType;
import jfdi.parser.DateTimeObject.DateTimeObjectBuilder;
import jfdi.parser.exceptions.BadDateTimeException;

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
    // TODO: support for "from {date}{time} to {time}"
    private static DateTimeParser dateTimeParser;
    private static final String SOURCECLASS = DateTimeParser.class.getName();
    private static final Logger LOGGER = JfdiLogger.getLogger();

    public static DateTimeParser getInstance() {
        return dateTimeParser == null ? dateTimeParser = new DateTimeParser()
            : dateTimeParser;
    }

    /**
     * This method parses the given input string into a DateTimeObject.
     *
     * @param input
     *            a string that should match the date-time format listed in
     *            parser.Constants.java.
     * @return a DateTimeObject encapsulating the details of the input date time
     *         string.
     * @throws a
     *             BadDateTimeException if the input string doesn't match a
     *             valid date time format.
     */
    public DateTimeObject parseDateTime(String input)
        throws BadDateTimeException {
        if (!isValidDateTime(input)) {
            LOGGER.throwing(SOURCECLASS, "parseDateTime",
                new BadDateTimeException(input));
            throw new BadDateTimeException(input);
        }

        DateTimeObject dateTimeObject = buildDateTimeObject(input);

        return dateTimeObject;
    }

    /**
     * This method builds a DateTimeObject from a given string input. An 'event'
     * type task will have both start and end date time. A 'point' task will
     * only have a start date time and NULL end date time. A 'deadline' task
     * will only have an end date time and NULL start date time. A 'floating'
     * task will have neither.
     *
     * @param input
     *            a string that contains date time fields
     * @return a DateTimeObject.
     * @throws BadDateTimeException
     *             if the start date parsed is later than the end date parsed.
     */
    private DateTimeObject buildDateTimeObject(String input)
        throws BadDateTimeException {
        assert isValidDateTime(input);

        DateTimeObjectBuilder dateTimeObjectBuilder = new DateTimeObjectBuilder();

        TaskType taskType = getTaskType(input);
        System.out.println(taskType);
        input = formatDate(input);
        input = toAmericanTime(input);
        System.out.println(input);
        // This might not be sufficient for event tasks
        // TODO: create methods for individual task type
        boolean isTimeSpecified = checkTimeSpecified(input);
        List<Date> dateList = getDateList(input);
        List<LocalDateTime> dateTimeList = toLocalDateTime(dateList);

        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;
        switch (taskType) {
            case event:
                assert dateTimeList.size() == 2;
                startDateTime = dateTimeList.get(0);
                endDateTime = dateTimeList.get(1);
                if (!isTimeSpecified) {
                    startDateTime = setTime(startDateTime,
                        Constants.TIME_BEGINNING_OF_DAY);
                    endDateTime = setTime(endDateTime,
                        Constants.TIME_END_OF_DAY);
                }

                if (startDateTime.compareTo(endDateTime) > 0) {
                    LOGGER.throwing(SOURCECLASS, "buildDateTimeObject",
                        new BadDateTimeException(input));
                    throw new BadDateTimeException(input);
                }
                break;
            case point:
                assert dateTimeList.size() == 1;
                startDateTime = dateTimeList.get(0);
                if (!isTimeSpecified) {
                    startDateTime = setTime(startDateTime,
                        Constants.TIME_DEFAULT);
                    System.out.println(startDateTime.getHour());
                }
                break;
            case deadline:
                assert dateTimeList.size() == 1;
                endDateTime = dateTimeList.get(0);
                if (!isTimeSpecified) {
                    endDateTime = setTime(endDateTime, Constants.TIME_DEFAULT);
                }
                break;
            default:
                break;
        }

        dateTimeObjectBuilder.setTaskType(taskType);
        dateTimeObjectBuilder.setIsTimeSpecified(isTimeSpecified);
        dateTimeObjectBuilder.setStartDateTime(startDateTime);
        dateTimeObjectBuilder.setEndDateTime(endDateTime);

        DateTimeObject dateTimeObject = dateTimeObjectBuilder.build();

        return dateTimeObject;
    }

    /**
     * This method converts dates of the form {day}/{month}/{year} to
     * {month}/{day}/{year}. This has to be done because the underlying parsing
     * library, prettyTime, can only parse American dates.
     *
     * @param input
     *            the date time string to be parsed.
     * @return the same input string, except with day and month reversed, if
     *         any.
     */
    private String toAmericanTime(String input) {
        assert isValidDateTime(input);

        return input
            .replaceAll(
                "\\b(?<day>0?[1-9]|[12][\\d]|3[01])(?<delimiter1>[-/.])"
                    + "(?<month>0?[1-9]|1[0-2])((?<delimiter2>[-/.])(?<year>(19|20)?\\d\\d))?\\b",
                "${month}${delimiter1}${day}${delimiter2}${year}");
    }

    /**
     * This method formats the given date-time input into something readable by
     * the underlying date-time parser.
     *
     * @param input
     *            the date-time input.
     * @return the formatted string.
     */
    private String formatDate(String input) {

        input = input.replaceAll(
            Constants.REGEX_DATE_TIME_FORMAT_DATE_FIRST_WITH_NAMED_GROUPS,
            "${time1}, ${date1}");
        input = input.replaceAll(
            Constants.REGEX_DATE_TIME_FORMAT_TIME_FIRST_WITH_NAMED_GROUPS,
            "${time2}, ${date2}");
        StringBuilder inputBuilder = new StringBuilder(input);
        Pattern dateFormatPattern = Pattern
            .compile(Constants.REGEX_ABSOLUTE_DATE_DDMMYYYY);
        Matcher dateFormatMatcher = dateFormatPattern.matcher(input);
        while (dateFormatMatcher.find()) {
            int start = dateFormatMatcher.start();
            int end = dateFormatMatcher.end();
            inputBuilder.replace(start, end, inputBuilder.substring(start, end)
                .replaceAll("[.-]", "/"));
        }

        Pattern dateFormatPattern2 = Pattern
            .compile(Constants.REGEX_ABSOLUTE_DATE_DDMONTHYYYY);
        Matcher dateFormatMatcher2 = dateFormatPattern2.matcher(input);
        while (dateFormatMatcher2.find()) {
            int start = dateFormatMatcher2.start();
            int end = dateFormatMatcher2.end();
            inputBuilder.replace(start, end, inputBuilder.substring(start, end)
                .replaceAll("[./-]", " "));
            System.out.println("lol" + inputBuilder.toString());
        }

        return inputBuilder.toString();
    }

    /**
     * This method parses the input string into a list of Dates.
     *
     * @param input
     *            the input string representing a date time.
     * @return a list of Date objects, storing information about the date and
     *         time specified in the string.
     */
    private List<Date> getDateList(String input) {
        assert input != null;
        PrettyTimeParser parser = new PrettyTimeParser();
        List<Date> dateList = parser.parse(input);
        return dateList;
    }

    /**
     * This method checks to see if a particular time is specified in the date
     * time string
     *
     * @param input
     *            a date time string.
     * @return true if time is specified; false otherwise.
     */
    private boolean checkTimeSpecified(String input) {
        Pattern pattern = Pattern.compile(Constants.REGEX_TIME_FORMAT);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }

    /**
     * This method converts a list of Dates to a list of LocalDateTimes.
     *
     * @param dateTimeList
     *            the list of Date objects to be converted.
     * @return a list of LocalDateTime objects.
     */
    private List<LocalDateTime> toLocalDateTime(List<Date> dateTimeList) {
        assert dateTimeList != null;
        List<LocalDateTime> ldtList = new ArrayList<LocalDateTime>();
        for (Date d : dateTimeList) {
            ldtList.add(getLocalDateTimeFromDate(d));
        }
        return ldtList;
    }

    /**
     * This method gets the type of the task depending on the format of the
     * input.
     *
     * @param input
     *            the date time string.
     * @return a TaskType enum representing the task type of the input string.
     */
    private TaskType getTaskType(String input) {
        assert input != null;
        if (input.matches(Constants.REGEX_EVENT_IDENTIFIER)) {
            return TaskType.event;
        } else if (input.matches(Constants.REGEX_DEADLINE_IDENTIFIER)) {
            return TaskType.deadline;
        } else if (input.matches(Constants.REGEX_POINT_TASK_IDENTIFIER)) {
            return TaskType.point;
        } else {
            return TaskType.floating;
        }
    }

    /**
     * This method checks to see if the provided input matches a format out of
     * all supported date time formats.
     *
     * @param input
     *            the String to be checked.
     * @return true if the String is in a valid date time format; false
     *         otherwise.
     */
    private boolean isValidDateTime(String input) {
        assert input != null;
        return input.matches(Constants.REGEX_DATE_TIME_IDENTIFIER);
    }

    /**
     * This method reformats a given Date object to a LocalDateTime object. The
     * zone is taken to be the one found in the system.
     *
     * @param d
     *            a Date object.
     * @return the LocalDateTime object formatted from the Date object.
     */
    public LocalDateTime getLocalDateTimeFromDate(Date d) {
        assert d != null;
        return LocalDateTime.ofInstant(d.toInstant(), Constants.ZONE_ID);
    }

    /**
     * This method sets the time of a LocalDateTime object to the time
     * specified.
     *
     * @param dateTime
     *            the LocalDateTime object which time is to be changed.
     * @param time
     *            the time to change to.
     * @return a LocalDateTime object with time changed.
     */
    private LocalDateTime setTime(LocalDateTime dateTime, Constants.Time time) {
        return dateTime.withHour(time.hour).withMinute(time.minutes)
            .withSecond(time.seconds).withNano(time.nanoseconds);
    }

    public static void main(String[] args) throws Exception {
        DateTimeParser parser = DateTimeParser.getInstance();
        System.out.println(parser.parseDateTime("by 3 days later, 2300hrs")
            .getStartDateTime());
    }
}
