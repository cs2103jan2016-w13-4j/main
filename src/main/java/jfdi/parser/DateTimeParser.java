// @@author A0127393B

package jfdi.parser;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jfdi.common.utilities.JfdiLogger;
import jfdi.parser.Constants.TaskType;
import jfdi.parser.DateTimeObject.DateTimeObjectBuilder;
import jfdi.parser.exceptions.BadDateTimeException;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

/**
 * DateTimeParser is a class used to parse a String input into one a list of
 * LocalDateTime objects it represents. It supports parsing Strings of different
 * formats. For example, it can parse "by 23rd February 2015" or "from 23/02/15
 * 2359hrs to 29/03/16 09:40pm". For an idea of the format of String parseable,
 * see parser.Constants.java.
 *
 * @author Leonard Hio
 *
 */
public class DateTimeParser {
    private static DateTimeParser dateTimeParser;
    private static final String SOURCECLASS = DateTimeParser.class.getName();
    private static final Logger LOGGER = JfdiLogger.getLogger();

    public static DateTimeParser getInstance() {
        return dateTimeParser == null ? dateTimeParser = new DateTimeParser()
            : dateTimeParser;
    }

    /**
     * This method parses the given input String into a DateTimeObject.
     *
     * @param input
     *            a String that should match the date-time format listed in
     *            parser.Constants.java.
     * @return a DateTimeObject encapsulating the details of the input date time
     *         String.
     * @throws a
     *             BadDateTimeException if the input String doesn't match a
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

    // =========================================
    // First Level of Abstraction
    // =========================================

    /**
     * This method builds a DateTimeObject from a given String input. An 'event'
     * type task will have both start and end date time. A 'point' task will
     * only have a start date time and NULL end date time. A 'deadline' task
     * will only have an end date time and NULL start date time. A 'floating'
     * task will have neither.
     *
     * @param input
     *            a String that contains date time fields
     * @return a DateTimeObject.
     * @throws BadDateTimeException
     *             if the start date parsed is later than the end date parsed.
     */
    private DateTimeObject buildDateTimeObject(String input)
        throws BadDateTimeException {
        assert isValidDateTime(input);
        String originalInput = input;
        DateTimeObjectBuilder dateTimeObjectBuilder = new DateTimeObjectBuilder();

        TaskType taskType = getTaskType(input);
        input = formatDateTimeInput(input);
        System.out.println("After formatting: " + input);

        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;
        switch (taskType) {
            case event:
                System.out.println("event");
                String[] splitInput = input.split("\\bto\\b");
                String[] splitOriginalInput = originalInput.split("\\bto\\b");
                assert splitInput.length == 2;
                startDateTime = getLocalDateTime(splitInput[0]);
                endDateTime = getLocalDateTime(splitInput[1]);
                if (!checkTimeSpecified(originalInput)) {
                    startDateTime = setTime(startDateTime,
                        Constants.TIME_BEGINNING_OF_DAY);
                    endDateTime = setTime(endDateTime,
                        Constants.TIME_END_OF_DAY);
                } else {
                    if (!checkTimeSpecified(splitOriginalInput[0])) {
                        startDateTime = setTime(startDateTime,
                            Constants.TIME_DEFAULT);
                    }
                    if (!checkTimeSpecified(splitOriginalInput[1])) {
                        endDateTime = setTime(endDateTime,
                            Constants.TIME_DEFAULT);
                    }
                    if (!checkDateSpecified(splitOriginalInput[1])) {
                        System.out.println(true);
                        endDateTime = setDate(endDateTime, startDateTime);
                    }
                }
                if (startDateTime.compareTo(endDateTime) > 0) {
                    LOGGER.throwing(SOURCECLASS, "buildDateTimeObject",
                        new BadDateTimeException(input));
                    throw new BadDateTimeException(input);
                }
                break;
            case point:
                startDateTime = getLocalDateTime(input);
                if (!checkTimeSpecified(input)) {
                    startDateTime = setTime(startDateTime,
                        Constants.TIME_DEFAULT);
                }
                break;
            case deadline:
                endDateTime = getLocalDateTime(input);
                if (!checkTimeSpecified(input)) {
                    endDateTime = setTime(endDateTime, Constants.TIME_DEFAULT);
                }
                break;
            default:
                break;
        }

        dateTimeObjectBuilder.setTaskType(taskType);
        dateTimeObjectBuilder.setStartDateTime(startDateTime);
        dateTimeObjectBuilder.setEndDateTime(endDateTime);

        DateTimeObject dateTimeObject = dateTimeObjectBuilder.build();

        return dateTimeObject;
    }

    private String formatDateTimeInput(String input) {
        assert isValidDateTime(input);
        input = formatDate(input);
        input = formatTime(input);
        input = toAmericanTime(input);
        return input;
    }

    // =========================================
    // Second Level of Abstraction
    // =========================================

    /**
     * This method converts dates of the form {day}/{month}/{year} to
     * {month}/{day}/{year}. This has to be done because the underlying parsing
     * library, prettyTime, can only parse American dates.
     *
     * @param input
     *            the date time String to be parsed.
     * @return the same input String, except with day and month reversed, if
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
     * @return the formatted String.
     */
    private String formatDate(String input) {

        input = input.replaceAll(
            Constants.REGEX_DATE_TIME_FORMAT_DATE_FIRST_WITH_NAMED_GROUPS,
            "${time1}, ${date1}");
        input = input.replaceAll(
            Constants.REGEX_DATE_TIME_FORMAT_TIME_FIRST_WITH_NAMED_GROUPS,
            "${time2}, ${date2}");
        input = input.replaceAll("\\b(?<days>" + Constants.REGEX_DAYS_NUMERIC
            + ")[-/. ](?<months>" + Constants.REGEX_MONTHS_TEXTUAL + ")[-/. ]"
            + "(?<year>\\d\\d)\\b", "${days} ${months} 20${year}");
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
        }

        return inputBuilder.toString();
    }

    private String formatTime(String input) {
        input = input.replaceAll(
            "(?i)(0?[1-9]|1[0-2])([0-5][0-9])([ :]?([a|p][m]))", "$1.$2$3");
        return input;
    }

    /**
     * This method parses the input String into a list of Dates.
     *
     * @param input
     *            the input String representing a date time.
     * @return a list of Date objects, storing information about the date and
     *         time specified in the String.
     */
    private LocalDateTime getLocalDateTime(String input) {
        assert input != null;
        Parser parser = new Parser();
        List<DateGroup> dateList = parser.parse(input);
        assert dateList.size() == 1;
        return getLocalDateTimeFromDate(dateList.get(0).getDates().get(0));
    }

    /**
     * This method checks to see if a time is specified in the date time String.
     *
     * @param input
     *            a date time String.
     * @return true if time is specified; false otherwise.
     */
    private boolean checkTimeSpecified(String input) {
        Pattern pattern = Pattern.compile(Constants.REGEX_TIME_FORMAT + "|"
            + Constants.REGEX_TIME_ATTRIBUTES);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }

    /**
     * This method checks to see if a date is specified in the date time String.
     *
     * @param input
     *            a date time String.
     * @return true if date is specified; false otherwise.
     */
    private boolean checkDateSpecified(String input) {
        Pattern pattern = Pattern.compile(Constants.REGEX_DATE_FORMAT);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }

    /**
     * This method gets the type of the task depending on the format of the
     * input.
     *
     * @param input
     *            the date time String.
     * @return a TaskType enum representing the task type of the input String.
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

    /**
     * This method sets the time of a LocalDateTime object to the time
     * specified.
     *
     * @param to
     *            the LocalDateTime object which date is to be changed.
     * @param from
     *            the LocalDateTime with the new date to change to.
     * @return a LocalDateTime object with date changed.
     */
    private LocalDateTime setDate(LocalDateTime to, LocalDateTime from) {
        return to.withYear(from.getYear()).withMonth(from.getMonthValue())
            .withDayOfMonth(from.getDayOfMonth());
    }

    public static void main(String[] args) throws Exception {
        DateTimeParser parser = DateTimeParser.getInstance();
        System.out.println(parser.formatDate("by 23rd feb 16"));
    }
}
