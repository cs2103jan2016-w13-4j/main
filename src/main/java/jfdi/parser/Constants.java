package jfdi.parser;

import java.time.ZoneId;

public class Constants {

    // ===============================
    // Regex Constants
    // ===============================

    // General Regex Strings
    public static final String REGEX_WHITESPACE = "\\s";

    // Task type specific Regex Strings
    public static final String REGEX_ADD = "^(add)";
    public static final String REGEX_LIST = "^(list)";
    public static final String REGEX_EDIT = "^(edit)";
    public static final String REGEX_DELETE = "^(delete)";

    // Task related Regex Strings
    // Tags are always appended to the end of any input, hence the boundary
    // condition $
    public static final String REGEX_TAGS = "\\+\\w+$";
    // A taskID is simply an integer
    public static final String REGEX_TASKID = "\\d+";

    // Date and Time related Regex Strings
    public static final String REGEX_DAYS_NUMERIC = "(0?[1-9]|[12][\\d]|3[01])(st|th|nd|rd)?";
    public static final String REGEX_DAYS_TEXTUAL = "((mon|MON)(day|DAY)?|"
            + "(tue|TUE)(sday|sDAY)?|" + "(wed|WED)(nesday|NESDAY)?|"
            + "(thu|THU)(rsday|RSDAY)?|" + "(fri|FRI)(day|DAY)?|"
            + "(sat|SAT)(urday|URDAY)?|" + "(sun|SUN)(day|DAY)?)";
    public static final String REGEX_MONTHS_NUMERIC = "(0?[1-9]|1[0-2])";
    public static final String REGEX_MONTHS_TEXTUAL = "((j|J)(an|AN)(uary|UARY)?|"
            + "(f|F)(eb|EB)(ruary|RUARY)?|"
            + "(m|M)(ar|AR)(ch|CH)?|"
            + "(a|A)(pr|PR)(il|IL)?|"
            + "(m|M)(ay|AY)|"
            + "(j|J)(un|UN)(e|E)?|"
            + "(j|J)(ul|UL)(y)?|"
            + "(a|A)(ug|UG)(ust|UST)?|"
            + "(s|S)(ep|EP)(tember|TEMBER)?|"
            + "(o|O)(ct|CT)(ober|OBER)?|"
            + "(n|N)(ov|OV)(ember|EMBER)?|" + "(d|D)(ec|EC)(ember|EMBER)?)";
    public static final String REGEX_YEARS = "((19|20)?\\d\\d)";
    public static final String REGEX_DATE_ATTRIBUTES = "((day|DAY)(s|S)?|"
            + "(week|WEEK|wk|WK)(s|S)?|" + "(month|MONTH|mth|MTH)(s|S)?|"
            + "(year|YEAR|yr|YR))(s|S)?";
    public static final String REGEX_TIME_MILITARY = "[0-2][0-3][ :-]?[0-5][\\d]([h|H]([r|R][s|S]?)?)?";
    public static final String REGEX_TIME_NORMAL = "(0?[1-9]|1[0-2])[ -:]?([0-5][0-9])[ -:]?([a|A|p|P][m|M])?";

    public static final String REGEX_RELATIVE_DATE_1 = "(tomorrow|yesterday)";
    public static final String REGEX_RELATIVE_DATE_2 = "((next|NEXT) ("
            + REGEX_DATE_ATTRIBUTES + "|" + REGEX_DAYS_TEXTUAL + "))";
    public static final String REGEX_RELATIVE_DATE_3 = "(\\d+ "
            + REGEX_DATE_ATTRIBUTES + " (later|before|after))";

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
    public static final String REGEX_DEADLINE_IDENTIFIER = "(by|before) "
            + REGEX_DATE_TIME_FORMAT;
    public static final String REGEX_EVENT_IDENTIFIER = "(from) "
            + REGEX_DATE_TIME_FORMAT + " to " + REGEX_DATE_TIME_FORMAT;
    public static final String REGEX_POINT_TASK_IDENTIFIER = "(on|at) "
            + REGEX_DATE_TIME_FORMAT;
    public static final String REGEX_REPEATED_TASK_IDENTIFIER = "(every) "
            + REGEX_DATE_TIME_FORMAT + "( to " + REGEX_DATE_TIME_FORMAT + ")?";
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

}
