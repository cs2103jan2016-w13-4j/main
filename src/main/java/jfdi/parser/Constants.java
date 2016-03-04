package jfdi.parser;

import java.time.ZoneId;

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
    public static final String REGEX_TAGS = "\\+\\w+$";
    // A taskID is simply an integer
    public static final String REGEX_TASKID = "\\d+";

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
    public static final String REGEX_TIME_MILITARY = "(?i)[0-2][0-3][ :-]?[0-5][\\d]([h]([r][s]?)?)?";
    public static final String REGEX_TIME_NORMAL = "((?i)0?[1-9]|1[0-2])[ -:]?([0-5][0-9])[ -:]?([a|p][m])?";

    public static final String REGEX_RELATIVE_DATE_1 = "(?i)(tomorrow|yesterday)";
    public static final String REGEX_RELATIVE_DATE_2 = "(?i)((next) ("
            + REGEX_DATE_ATTRIBUTES + "|" + REGEX_DAYS_TEXTUAL + "))";
    public static final String REGEX_RELATIVE_DATE_3 = "(\\d+ "
            + REGEX_DATE_ATTRIBUTES + " (?i)(later|before|after))";

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
            + REGEX_REPEATED_TASK_IDENTIFIER + ")$";

    // =============================
    // Non-Regex constants
    // =============================
    public static final int INDEX_ACTION = 0;

    // The current time zone of the system, used to find LocalDateTime when
    // parsing dates
    public static final ZoneId ZONE_ID = ZoneId.systemDefault();

    public static void main(String[] args) {
        System.out.println("4 weeks lAter".matches(REGEX_RELATIVE_DATE_3));
    }
}
