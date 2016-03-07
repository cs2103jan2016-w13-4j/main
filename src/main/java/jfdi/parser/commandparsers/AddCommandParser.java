package jfdi.parser.commandparsers;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jfdi.logic.commands.AddTaskCommand;
import jfdi.logic.commands.AddTaskCommand.Builder;
import jfdi.parser.Constants;
import jfdi.parser.DateTimeObject;
import jfdi.parser.DateTimeParser;

/**
 * The AddCommandParser class is used to parse a user input String that
 * resembles an add command. All user inputs for adding tasks must adhere to the
 * following format: {add identifier}(optional) {task description} {date time
 * identifier}(optional) {tags}(optional)
 *
 * @author Leonard Hio
 *
 */
public class AddCommandParser extends AbstractCommandParser {

    private static AbstractCommandParser instance;

    private AddCommandParser() {

    }

    public static AbstractCommandParser getInstance() {
        if (instance == null) {
            return instance = new AddCommandParser();
        }

        return instance;
    }

    @Override
    /**
     * This method parses the user input (representing an add command) and builds the
     * AddTaskCommand object. To build the add command, we traverse from the back,
     * retrieving the tags first, then the date time identifiers if present, then the
     * task description.
     *
     * @param input
     *            the user input String
     * @return the AddTaskCommand object encapsulating the details of the add command.
     */
    public AddTaskCommand build(String input) {
        Builder addCommandBuilder = new Builder();
        input = setAndRemoveTags(input, addCommandBuilder);
        input = setAndRemoveDateTime(input, addCommandBuilder);
        setDescription(input, addCommandBuilder);

        AddTaskCommand addCommand = addCommandBuilder.build();
        return addCommand;
    }

    /**
     * Finds instances that match the tag Regex specified in
     * parser.Constants.java, removes them from the input string, then adds the
     * list of tags found into the Builder object.
     *
     * @param input
     *            the user input String
     * @param builder
     *            the builder object for AddTaskCommand
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
        }

        builder.addTags(tags);

        return input;
    }

    /**
     * Sets the date time identifier field in the builder, if it can be found in
     * the string input. If the user input has many instances of substrings that
     * match the Regex for date time identifiers (see parser.Constants.java),
     * then only the one closest to the tail of the string is taken as the date
     * time identifier. This is to allow for the user to both specify date times
     * for his task, while still allowing him the flexibility of typing in dates
     * and times in his task description.
     *
     * @param input
     *            the input string
     * @param builder
     *            the builder object for AddTaskCommand
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
            input = getTrimmedSubstringInRange(input, 0, matcher.start());
        }

        if (dateTimeIdentifier != null) {
            DateTimeParser dateTimeParser = DateTimeParser.getInstance();
            DateTimeObject dateTime = dateTimeParser
                    .parseDateTime(dateTimeIdentifier);
            builder.setStartDateTime(dateTime.getStartDateTime());
            builder.setEndDateTime(dateTime.getEndDateTime());
        }

        return input;
    }

    /**
     * Sets the description field of the builder object. Sets it to null if the
     * input String is empty, or just an add identifier without any task
     * descriptions.
     *
     * @param input
     *            is the input string from which the description is extracted.
     * @param builder
     *            the builder object for AddTaskCommand
     */
    private void setDescription(String input, Builder builder) {
        if (!input.isEmpty()) {
            String firstWord = getFirstWord(input);
            String taskDescription = null;
            if (firstWord.matches(Constants.REGEX_ADD)) {
                taskDescription = removeFirstWord(input);
            } else {
                taskDescription = input;
            }

            builder.setDescription(taskDescription);
        } else {
            builder.setDescription(null);
        }
    }

    private String removeFirstChar(String string) {
        return string.substring(1, string.length());
    }

    private String getFirstWord(String input) {
        return input.split(Constants.REGEX_WHITESPACE)[0];
    }

}
