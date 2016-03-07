package jfdi.parser;

import java.time.ZoneId;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Constants class is a database of all relevant constants used for parsing.
 * These constants include Regex constants that define date time formats,
 * command formats, and other miscellaneous formats.
 *
 * @author Leonard Hio
 *
 */
public class Constants {

    // ===============================
    // Regex Constants
    // ===============================

    // General Regex Strings
    public static final String REGEX_WHITESPACE = "\\s";

    // Task type specific Regex Strings
    public static final String REGEX_ADD = "(?i)^(add)";
    public static final String REGEX_LIST = "(?i)^(list)";
    public static final String REGEX_RENAME = "(?i)^(rename)";
    public static final String REGEX_RESCHEDULE = "(?i)^(reschedule)";
    public static final String REGEX_DELETE = "(?i)^(delete)";

    // Task related Regex Strings
    // Tags are always appended to the end of any input, hence the boundary
    // condition $
    public static final String REGEX_TAGS = "\\+\\w+";
    // A taskID is simply an integer
    public static final String REGEX_TASKID = "\\b\\d+\\b";

    // Date and Time related Regex Strings
    public static final String REGEX_DAYS_NUMERIC = "((?i)0?[1-9]|[12][\\d]|3[01])(st|th|nd|rd)?";
    public static final String REGEX_DAYS_TEXTUAL = "((?i)(mon)(day)?|"
            + "(tue)(sday)?|" + "(wed)(nesday)?|" + "(thu)(rsday)?|"
            + "(fri)(day)?|" + "(sat)(urday)?|" + "(sun)(day)?)";
    public static final String REGEX_MONTHS_NUMERIC = "(0?[1-9]|1[0-2])";
    public static final String REGEX_MONTHS_TEXTUAL = "((?i)(jan)(uary)?|"
            + "(feb)(ruary)?|" + "(mar)(ch)?|" + "(apr)(il)?|" + "(may)|"
            + "(jun)(e)?|" + "(jul)(y)?|" + "(aug)(ust)?|" + "(sep)(tember)?|"
            + "(oct)(ober)?|" + "(nov)(ember)?|" + "(dec)(ember)?)";
    public static final String REGEX_YEARS = "((19|20)?\\d\\d)";
    public static final String REGEX_DATE_ATTRIBUTES = "((?i)(day)(s)?|"
            + "(week|wk)(s)?|" + "(month|mth)(s)?|" + "(year|yr))(s)?";
    public static final String REGEX_TIME_MILITARY = "(?i)[0-2][0-3][ :-]?[0-5][\\d]([h]([r][s]?)?)";
    public static final String REGEX_TIME_NORMAL = "((?i)0?[1-9]|1[0-2])[ -:]?([0-5][0-9])?[ -:]?([a|p][m])";

    public static final String REGEX_RELATIVE_DATE_1 = "(?i)(tomorrow|yesterday)";
    public static final String REGEX_RELATIVE_DATE_2 = "(?i)((next |this )?("
            + REGEX_DATE_ATTRIBUTES + "|" + REGEX_DAYS_TEXTUAL + "))";
    public static final String REGEX_RELATIVE_DATE_3 = "(\\d+ "
            + REGEX_DATE_ATTRIBUTES + " (?i)(later|before|after|from now))";

    public static final String REGEX_RELATIVE_DATE = "("
            + REGEX_RELATIVE_DATE_1 + "|" + REGEX_RELATIVE_DATE_2 + "|"
            + REGEX_RELATIVE_DATE_3 + ")";

    public static final String REGEX_ABSOLUTE_DATE_DDMMYYYY = REGEX_DAYS_NUMERIC
            + "[-./]?" + REGEX_MONTHS_NUMERIC + "[-./]?" + REGEX_YEARS + "?";
    public static final String REGEX_ABSOLUTE_DATE_DDMONTHYYYY = REGEX_DAYS_NUMERIC
            + "[-./ ]?" + REGEX_MONTHS_TEXTUAL + "[-./ ]?" + REGEX_YEARS + "?";

    // Formats for date, time, and date-time in Regex (built from date and time
    // related Regex Strings)
    public static final String REGEX_DATE_FORMAT = "("
            + REGEX_ABSOLUTE_DATE_DDMMYYYY + "|"
            + REGEX_ABSOLUTE_DATE_DDMONTHYYYY + ")";
    public static final String REGEX_TIME_FORMAT = "(" + REGEX_TIME_MILITARY
            + "|" + REGEX_TIME_NORMAL + ")";
    public static final String REGEX_DATE_TIME_FORMAT = "(("
            + REGEX_DATE_FORMAT + "(,?[ ]" + REGEX_TIME_FORMAT + ")?)|("
            + REGEX_TIME_FORMAT + "(,?[ ]" + REGEX_DATE_FORMAT + ")?))";

    // Regex used to detect date-time fields in the user's input String
    public static final String REGEX_DEADLINE_IDENTIFIER = "(?i)(by|before) "
            + REGEX_DATE_TIME_FORMAT;
    public static final String REGEX_EVENT_IDENTIFIER = "(?i)(from) "
            + REGEX_DATE_TIME_FORMAT + " to " + REGEX_DATE_TIME_FORMAT;
    public static final String REGEX_POINT_TASK_IDENTIFIER = "(?i)(on|at) "
            + REGEX_DATE_TIME_FORMAT;
    public static final String REGEX_REPEATED_TASK_IDENTIFIER = "(?i)(every) "
            + REGEX_DATE_TIME_FORMAT + "(?i)( to " + REGEX_DATE_TIME_FORMAT
            + ")?";
    public static final String REGEX_DATE_TIME_IDENTIFIER = "("
            + REGEX_DEADLINE_IDENTIFIER + "|" + REGEX_EVENT_IDENTIFIER + "|"
            + REGEX_POINT_TASK_IDENTIFIER + "|"
            + REGEX_REPEATED_TASK_IDENTIFIER + ")";

    // =============================
    // Non-Regex constants
    // =============================

    // A task can be any one of these task types
    public enum TaskType {
        floating, deadline, event, point, repeated;
    }

    // An enum containing all the commands available to the user
    public enum CommandType {
        add, delete, rename, reschedule, list, view, invalid;
    }

    public static final int INDEX_ACTION = 0;

    /**
     * A class to encapsulate the fields associated with time, such as the hour,
     * the minute, etc.
     *
     * @author Leonard Hio
     *
     */
    public static class Time {
        public static final int MIN_HOUR = 0;
        public static final int MIN_MINUTES = 0;
        public static final int MIN_SECONDS = 0;
        public static final int MIN_NANOSECONDS = 0;
        public static final int MAX_HOUR = 23;
        public static final int MAX_MINUTES = 59;
        public static final int MAX_SECONDS = 59;
        public static final int MAX_NANOSECONDS = 999999999;
        public static final int DEFAULT_HOUR = 12;
        public static final int DEFAULT_MINUTES = 0;
        public static final int DEFAULT_SECONDS = 0;
        public static final int DEFAULT_NANOSECONDS = 0;

        int hour;
        int minutes;
        int seconds;
        int nanoseconds;

        public Time(int hour, int minutes, int seconds, int nanoseconds) {
            this.hour = hour;
            this.minutes = minutes;
            this.seconds = seconds;
            this.nanoseconds = nanoseconds;
        }
    }

    public static final Time TIME_BEGINNING_OF_DAY = new Time(Time.MIN_HOUR,
            Time.MIN_MINUTES, Time.MIN_SECONDS, Time.MIN_NANOSECONDS);
    public static final Time TIME_END_OF_DAY = new Time(Time.MAX_HOUR,
            Time.MAX_MINUTES, Time.MAX_SECONDS, Time.MAX_NANOSECONDS);
    public static final Time TIME_DEFAULT = new Time(Time.DEFAULT_HOUR,
            Time.DEFAULT_MINUTES, Time.DEFAULT_SECONDS,
            Time.DEFAULT_NANOSECONDS);

    // The current time zone of the system, used to find LocalDateTime when
    // parsing dates
    public static final ZoneId ZONE_ID = ZoneId.systemDefault();

    public static void main(String[] args) {
        Pattern pat = Pattern.compile(REGEX_DATE_TIME_IDENTIFIER);
        Matcher mat = pat.matcher("");
        System.out.println("4 weeks from now".matches(REGEX_RELATIVE_DATE));
    }
}
