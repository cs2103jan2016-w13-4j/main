// @@author A0127393B

package jfdi.parser;

import java.time.ZoneId;

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
    public static final String REGEX_DESCRIPTION_ESCAPE_DELIMITER = "\"";

    // Task type specific Regex Strings
    public static final String REGEX_ADD = "(?i)^(add)";
    public static final String REGEX_LIST = "(?i)^(list)";
    public static final String REGEX_RENAME = "(?i)^(rename)";
    public static final String REGEX_RESCHEDULE = "(?i)^(reschedule)";
    public static final String REGEX_DELETE = "(?i)^(delete|remove)";
    public static final String REGEX_SEARCH = "(?i)^(search)";
    public static final String REGEX_MARK = "(?i)^(mark)";
    public static final String REGEX_UNMARK = "(?i)^(unmark)";
    public static final String REGEX_ALIAS = "(?i)^(alias)";
    public static final String REGEX_UNALIAS = "(?i)^(unalias)";
    public static final String REGEX_DIRECTORY = "(?i)^(directory)";
    public static final String REGEX_USE = "(?i)^(use)";
    public static final String REGEX_MOVE = "(?i)^(move)";
    public static final String REGEX_UNDO = "(?i)^(undo)";
    public static final String REGEX_HELP = "(?i)^(help)";
    public static final String REGEX_WILDCARD = "(?i)^(surprise[!]*)";
    public static final String REGEX_EXIT = "(?i)^(exit|quit)";

    // Task related Regex Strings
    public static final String REGEX_DELETE_FORMAT = String.format(
        "%s ((\\d+|\\d+[ ]*-[ ]*\\d+),?[ ]*)+", REGEX_DELETE);
    public static final String REGEX_MARK_FORMAT = String.format(
        "%s ((\\d+|\\d+[ ]*-[ ]*\\d+),?[ ]*)+", REGEX_MARK);
    public static final String REGEX_UNMARK_FORMAT = String.format(
        "%s ((\\d+|\\d+[ ]*-[ ]*\\d+),?[ ]*)+", REGEX_UNMARK);

    // A taskID is simply an integer
    public static final String REGEX_TASKID = "\\b\\d+\\b";

    // Date and Time related Regex Strings
    public static final String REGEX_DAYS_NUMERIC = "(((?i)0?[1-9]|[12][\\d]|3[01])(st|th|nd|rd)?)";
    public static final String REGEX_DAYS_TEXTUAL = "((?i)(mon)(day)?|"
        + "(tue)(sday)?|" + "(wed)(nesday)?|" + "(thu)(rsday)?|"
        + "(fri)(day)?|" + "(sat)(urday)?|" + "(sun)(day)?)";
    public static final String REGEX_MONTHS_NUMERIC = "(0?[1-9]|1[0-2])";
    public static final String REGEX_MONTHS_TEXTUAL = "((?i)(jan)(uary)?|"
        + "(feb)(ruary)?|" + "(mar)(ch)?|" + "(apr)(il)?|" + "(may)|"
        + "(jun)(e)?|" + "(jul)(y)?|" + "(aug)(ust)?|" + "(sep)(tember)?|"
        + "(oct)(ober)?|" + "(nov)(ember)?|" + "(dec)(ember)?)";
    public static final String REGEX_YEARS = "((19|20)?\\d\\d)";
    public static final String REGEX_DATE_ATTRIBUTES = "(?i)((day|(week|wk)|(month|mth)|(year|yr))(s)?)";
    public static final String REGEX_TIME_ATTRIBUTES = "(?i)(((sec|second)|(min|minute)|(hr|hour))(s)?)";
    public static final String REGEX_TIME_MILITARY = "(?i)([0-1][0-9]|[2][0-4])[.:]?[0-5][\\d]([h]([r][s]?)?)";
    public static final String REGEX_TIME_NORMAL = "(?i)(0?[1-9]|1[0-2])([.:]?([0-5][0-9]))?[ :]?([a|p][m])";
    public static final String REGEX_TIME_RELATIVE = "((?i)((this )?(morning|afternoon|evening)|(to)?night|midnight))";

    public static final String REGEX_RELATIVE_DATE_1 = "(?i)(tomorrow|yesterday|today|now)";
    public static final String REGEX_RELATIVE_DATE_2 = String.format(
        "(?i)((((the )?next|this|last) %s)|((the )?next |this |last )?%s)",
        REGEX_DATE_ATTRIBUTES, REGEX_DAYS_TEXTUAL);
    public static final String REGEX_RELATIVE_DATE_3 = String.format(
        "(\\d+ (%s|%s) (?i)(later|before|after|from now))",
        REGEX_DATE_ATTRIBUTES, REGEX_TIME_ATTRIBUTES);
    public static final String REGEX_RELATIVE_DATE_4 = String.format(
        "(?i)(in )?\\d+ (%s|%s)'?( time)?", REGEX_TIME_ATTRIBUTES,
        REGEX_DATE_ATTRIBUTES);

    public static final String REGEX_RELATIVE_DATE = String.format(
        "(%s|%s|%s|%s)", REGEX_RELATIVE_DATE_1, REGEX_RELATIVE_DATE_2,
        REGEX_RELATIVE_DATE_3, REGEX_RELATIVE_DATE_4);

    public static final String REGEX_ABSOLUTE_DATE_DDMMYYYY = String.format(
        "\\b%s[-/.]%s([-/.]%s)?\\b", REGEX_DAYS_NUMERIC, REGEX_MONTHS_NUMERIC,
        REGEX_YEARS);
    public static final String REGEX_ABSOLUTE_DATE_DDMONTHYYYY = String.format(
        "\\b%s[-/. ]%s([-/. ]%s)?\\b", REGEX_DAYS_NUMERIC,
        REGEX_MONTHS_TEXTUAL, REGEX_YEARS);

    // Formats for date, time, and date-time in Regex (built from date and time
    // related Regex Strings)
    public static final String REGEX_DATE_FORMAT = String.format("(%s|%s|%s)",
        REGEX_ABSOLUTE_DATE_DDMMYYYY, REGEX_ABSOLUTE_DATE_DDMONTHYYYY,
        REGEX_RELATIVE_DATE);
    public static final String REGEX_TIME_FORMAT = String.format("(%s|%s|%s)",
        REGEX_TIME_MILITARY, REGEX_TIME_NORMAL, REGEX_TIME_RELATIVE);
    public static final String REGEX_DATE_TIME_FORMAT_DATE_FIRST = String
        .format("(%s,? %s)", REGEX_DATE_FORMAT, REGEX_TIME_FORMAT);
    public static final String REGEX_DATE_TIME_FORMAT_DATE_FIRST_WITH_NAMED_GROUPS = String
        .format("((?<date1>%s),? (?<time1>%s))", REGEX_DATE_FORMAT,
            REGEX_TIME_FORMAT);
    public static final String REGEX_DATE_TIME_FORMAT_TIME_FIRST = String
        .format("(%s,? %s)", REGEX_TIME_FORMAT, REGEX_DATE_FORMAT);
    public static final String REGEX_DATE_TIME_FORMAT_TIME_FIRST_WITH_NAMED_GROUPS = String
        .format("((?<time2>%s),? (?<date2>%s))", REGEX_TIME_FORMAT,
            REGEX_DATE_FORMAT);
    public static final String REGEX_DATE_TIME_FORMAT = String.format(
        "(%s|%s|%s|%s)", REGEX_DATE_FORMAT, REGEX_TIME_FORMAT,
        REGEX_DATE_TIME_FORMAT_DATE_FIRST, REGEX_DATE_TIME_FORMAT_TIME_FIRST);

    // Regex used to detect date-time fields in the user's input String
    public static final String REGEX_DEADLINE_IDENTIFIER = String.format(
        "(?i)\\b(by|before) %s", REGEX_DATE_TIME_FORMAT);
    public static final String REGEX_EVENT_IDENTIFIER = String.format(
        "(?i)\\b((from )?%s (to|until) %s)", REGEX_DATE_TIME_FORMAT,
        REGEX_DATE_TIME_FORMAT);
    public static final String REGEX_POINT_TASK_IDENTIFIER = String.format(
        "(?i)\\b(on |at )?%s", REGEX_DATE_TIME_FORMAT);
    public static final String REGEX_DATE_TIME_IDENTIFIER = String.format(
        "(%s|%s|%s)", REGEX_DEADLINE_IDENTIFIER, REGEX_EVENT_IDENTIFIER,
        REGEX_POINT_TASK_IDENTIFIER);

    // =============================
    // Non-Regex constants
    // =============================

    // A task can be any one of these task types
    public enum TaskType {
        floating,
        deadline,
        event,
        point,
        repeated;
    }

    // An enum containing all the commands available to the user
    public enum CommandType {
        add,
        delete,
        rename,
        reschedule,
        list,
        search,
        mark,
        unmark,
        alias,
        unalias,
        directory,
        move,
        use,
        undo,
        help,
        wildcard,
        invalid,
        exit;
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

        public final int hour;
        public final int minutes;
        public final int seconds;
        public final int nanoseconds;

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
        Time.DEFAULT_MINUTES, Time.DEFAULT_SECONDS, Time.DEFAULT_NANOSECONDS);

    // The current time zone of the system, used to find LocalDateTime when
    // parsing dates
    public static final ZoneId ZONE_ID = ZoneId.systemDefault();

    protected static final String[] COMMAND_REGEXES = {REGEX_ADD, REGEX_LIST,
        REGEX_RENAME, REGEX_RENAME, REGEX_RESCHEDULE, REGEX_DELETE,
        REGEX_SEARCH, REGEX_MARK, REGEX_UNMARK, REGEX_ALIAS, REGEX_UNALIAS,
        REGEX_DIRECTORY, REGEX_USE, REGEX_MOVE, REGEX_UNDO, REGEX_HELP,
        REGEX_WILDCARD, REGEX_EXIT};

    public static void main(String[] args) {

        System.out.println("delete 1-10".matches(REGEX_DELETE_FORMAT));
        System.out.println("mark 1".matches(REGEX_MARK_FORMAT));
        System.out.println("12:34hrs, 23/12".matches(REGEX_DATE_FORMAT));
    }
}
