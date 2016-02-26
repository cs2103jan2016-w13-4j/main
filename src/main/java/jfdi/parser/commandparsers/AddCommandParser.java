package jfdi.parser.commandparsers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jfdi.logic.commands.AddCommandStub.Builder;
import jfdi.logic.commands.ListCommand;
import jfdi.parser.Constants;
import jfdi.parser.DateTimeParser;

public class AddCommandParser extends CommandParser {

    public static AddCommandParser instance;

    public static AddCommandParser getInstance() {
        if (instance == null) {
            return instance = new AddCommandParser();
        }

        return instance;
    }

    @Override
    /**
     * All user inputs for adding tasks adhere to the following format:
     * "add(optional) <task description> <date time identifier)(optional) <tags>(optional)
     * To build the add command, we traverse from the back, retrieving the tags
     * first, then the date time identifiers if present, then the task.
     */
    public ListCommand build(String input) {
        Builder addCommandBuilder = new Builder();
        input = setAndRemoveTags(input, addCommandBuilder);
        input = setAndRemoveDateTime(input, addCommandBuilder);
        // String taskType = getTaskType(input);
        // String description = getDescription(input);

        return new ListCommand.Builder().build();
    }

    /**
     * Finds instances that match the tag Regex specified in
     * parser.Constants.java, removes them from the input string, then adds the
     * list of tags found into the Builder object.
     * @param input
     *            typically the user input
     * @return the input, trimmed and without tags.
     */
    private String setAndRemoveTags(String input, Builder builder) {
        ArrayList<String> tags = new ArrayList<String>();

        Pattern tagPattern = Pattern.compile(Constants.REGEX_TAGS);
        Matcher matcher = tagPattern.matcher(input);

        while (matcher.find()) {
            // All tags are prepended with an identifier specified in
            // parser.Constants.java. We need to remove this identifier.
            String tag = removeFirstChar(getTrimmedSubstringInRange(input,
                    matcher.start(), matcher.end()));
            input = getTrimmedSubstringInRange(input, 0, matcher.start());
            tags.add(tag);
            matcher.reset(input);

            System.out.println(tag);
        }

        builder.addTags(tags);

        return input;
    }

    /**
     * Finds the date time identifier in the string input, if available. If the
     * user input has many instances of substrings that match the Regex for date
     * time identifiers (see parser.Constants.java), then only the one closest
     * to the tail of the string is taken as the date time identifier. This is
     * to allow for the user to both specify date times for his task, while
     * still allowing him the flexibility of typing in dates and times in his
     * task description.
     * @param input
     *            the input string
     * @return the input, trimmed and without date time identifiers.
     */
    private String setAndRemoveDateTime(String input, Builder builder) {
        Pattern dateTimePattern = Pattern
                .compile(Constants.REGEX_DATE_TIME_IDENTIFIER);
        Matcher matcher = dateTimePattern.matcher(input);
        String dateTimeIdentifier = null;

        if (matcher.find()) {
            dateTimeIdentifier = getTrimmedSubstringInRange(input,
                    matcher.start(), matcher.end());
            System.out.println(dateTimeIdentifier);

            input = getTrimmedSubstringInRange(input, 0, matcher.start());
        }

        if (dateTimeIdentifier != null) {
            DateTimeParser dateTimeParser = DateTimeParser.getInstance();
            List<LocalDateTime> dateTimeList = dateTimeParser
                    .parseDateTime(dateTimeIdentifier);
            ArrayList<LocalDateTime> dateTimeArrayList = new ArrayList<LocalDateTime>();
            dateTimeArrayList.addAll(dateTimeList);
            builder.addDateTimes(dateTimeArrayList);
        }

        return input;
    }

    private String removeFirstChar(String string) {
        return string.substring(1, string.length());
    }

    /**
     * Get the substring of an input String from startindex inclusive to
     * endindex exclusive, trimming it at the same time.
     * @param input
     *            a string to get substring of
     * @param startIndex
     *            index of the start of the substring (inclusive)
     * @param endIndex
     *            index of the end of the substring (exclusive)
     * @return the trimmed substring
     */
    private String getTrimmedSubstringInRange(String input, int startIndex,
            int endIndex) {
        return input.substring(startIndex, endIndex).trim();
    }

    public static void main(String[] args) {
        AddCommandParser.getInstance().build(
                "add task description from 23rd Dec to 25th Dec +helo +itsme");
    }
}
