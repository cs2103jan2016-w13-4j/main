package jfdi.parser;

import java.time.ZoneId;

public class Constants {
    public static final String REGEX_WHITESPACE = " ";
    public static final String REGEX_ADD = "^(add)";
    public static final String REGEX_LIST = "^(list)";
    public static final String REGEX_EDIT = "^(edit)";
    public static final String REGEX_DELETE = "^(delete)";

    public static final String REGEX_TAGS = "[+\\w]";

    public static final int INDEX_ACTION = 0;

    // The current time zone of the system, used to find LocalDateTime when
    // parsing dates
    public static final ZoneId ZONE_ID = ZoneId.systemDefault();
}
