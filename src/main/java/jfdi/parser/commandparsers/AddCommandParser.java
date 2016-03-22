package jfdi.parser.commandparsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jfdi.logic.commands.AddTaskCommand;
import jfdi.logic.commands.AddTaskCommand.Builder;
import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants;
import jfdi.parser.DateTimeObject;
import jfdi.parser.DateTimeParser;
import jfdi.parser.exceptions.BadDateTimeException;
import jfdi.parser.exceptions.BadTaskDescriptionException;

/**
 * The AddCommandParser class is used to parse a user input String that
 * resembles an add command. All user inputs for adding tasks must adhere to the
 * following format: {add identifier}(optional) {task description} {date time
 * identifier}(optional). In addition, a user may wrap his task description with
 * "{task description}", where " is the escape delimiter, to escape inadventent
 * formatting of date-tme in the description.
 *
 * @author Leonard Hio
 *
 */
public class AddCommandParser extends AbstractCommandParser {

    private static AddCommandParser instance;

    private AddCommandParser() {

    }

    public static AddCommandParser getInstance() {
        if (instance == null) {
            return instance = new AddCommandParser();
        }

        return instance;
    }

    @Override
    /**
     * This method parses the user input (representing an add command) and builds the
     * AddTaskCommand object. To build the add command, we traverse from the back,
     * retrieving the date time identifiers if present, then the
     * task description.
     *
     * @param input
     *            the user input String
     * @return the AddTaskCommand object encapsulating the details of the add command.
     */
    public Command build(String input) {
        String originalInput = input;
        Builder addCommandBuilder = new Builder();
        try {
            input = setAndRemoveDateTime(input, addCommandBuilder);
            setDescription(input, addCommandBuilder);
        } catch (BadDateTimeException | BadTaskDescriptionException e) {
            return createInvalidCommand(Constants.CommandType.add,
                originalInput);
        }

        AddTaskCommand addCommand = addCommandBuilder.build();
        return addCommand;
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
     *
     * @throws BadDateTimeException
     *             if input cannot be parsed as a date time
     */
    private String setAndRemoveDateTime(String input, Builder builder)
        throws BadDateTimeException {
        // Date time identifier must be at the end of the input String, hence
        // the "$" end-of-line flag
        Pattern dateTimePattern = Pattern
            .compile(Constants.REGEX_DATE_TIME_IDENTIFIER + "$");
        Matcher matcher = dateTimePattern.matcher(input);
        String dateTimeIdentifier = null;

        if (matcher.find()) {
            dateTimeIdentifier = getTrimmedSubstringInRange(input,
                matcher.start(), matcher.end());
            input = getTrimmedSubstringInRange(input, 0, matcher.start());
        }

        if (dateTimeIdentifier != null) {
            DateTimeParser dateTimeParser = DateTimeParser.getInstance();
            DateTimeObject dateTime = null;
            try {
                dateTime = dateTimeParser.parseDateTime(dateTimeIdentifier);
            } catch (BadDateTimeException e) {
                throw new BadDateTimeException(e.getInput());
            }
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
     * @throws BadTaskDescriptionException
     *             if input is invalid as a task description
     */
    private void setDescription(String input, Builder builder)
        throws BadTaskDescriptionException {

        if (!input.isEmpty()) {
            String firstWord = getFirstWord(input);
            String taskDescription = null;
            if (firstWord.matches(Constants.REGEX_ADD)) {
                taskDescription = removeFirstWord(input);
            } else {
                taskDescription = input;
            }

            if (taskDescription != null && !taskDescription.isEmpty()) {
                if (isWrappedWithEscapeDelimiters(taskDescription)) {
                    taskDescription = removeEscapeDelimiters(taskDescription);
                }
                builder.setDescription(taskDescription);
            } else {
                throw new BadTaskDescriptionException(input);
            }

        } else {
            throw new BadTaskDescriptionException(input);
        }
    }

    private String getFirstWord(String input) {
        return input.split(Constants.REGEX_WHITESPACE)[0];
    }

    /**
     * This method checks to see if the given input is a task description,
     * wrapped with the description escape delimiters.
     *
     * @param input
     *            the task description input
     * @return true if description is wrapped; false otherwise
     */
    private boolean isWrappedWithEscapeDelimiters(String input) {
        return input.substring(0, 1).matches(
            Constants.REGEX_DESCRIPTION_ESCAPE_DELIMITER)
            && input.substring(input.length() - 1, input.length()).matches(
                Constants.REGEX_DESCRIPTION_ESCAPE_DELIMITER);
    }

    /**
     * This method strips the given input of the description escape delimiters.
     *
     * @param input
     *            the task description that is wrapped with delimiters.
     * @return the input without delimiters.
     * @throws BadTaskDescriptionException
     *             if the resulting input without delimiters is an empty string
     */
    private String removeEscapeDelimiters(String input)
        throws BadTaskDescriptionException {
        assert isWrappedWithEscapeDelimiters(input);
        String unwrappedInput = input.substring(1, input.length() - 1);
        if (unwrappedInput.isEmpty()) {
            throw new BadTaskDescriptionException(input);
        } else {
            return unwrappedInput;
        }
    }

}
