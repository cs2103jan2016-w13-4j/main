// @@author A0127393B

package jfdi.parser.commandparsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jfdi.logic.commands.RescheduleTaskCommand.Builder;
import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants;
import jfdi.parser.Constants.CommandType;
import jfdi.parser.DateTimeObject;
import jfdi.parser.DateTimeParser;
import jfdi.parser.exceptions.BadDateTimeException;
import jfdi.parser.exceptions.NoTaskIdFoundException;

/**
 * The RescheduleCommandParser class is used to parse user input String that
 * resembles a reschedule command. All user inputs for renaming tasks must
 * adhere to the following format: {reschedule identifier} {task ID} to
 * (optional) {new task schedule}
 *
 * @author Leonard Hio
 *
 */
public class RescheduleCommandParser extends AbstractCommandParser {
    public static AbstractCommandParser instance;

    private RescheduleCommandParser() {

    }

    public static AbstractCommandParser getInstance() {
        if (instance == null) {
            return instance = new RescheduleCommandParser();
        }

        return instance;
    }

    /**
     * This method builds a RescheduleCommand by extracting out the task ID of
     * the task to be renamed, then its date time schedule, passing all these
     * information into a RenameCommand object.
     */
    @Override
    public Command build(String input) {
        if (!isValidRescheduleInput(input)) {
            return createInvalidCommand(CommandType.RESCHEDULE, input);
        }

        String originalInput = input;
        Builder rescheduleCommandBuilder = new Builder();
        // Remove the reschedule command identifier.
        input = removeFirstWord(input);
        try {
            input = setAndRemoveTaskId(input, rescheduleCommandBuilder);
            setTaskDateTime(input, rescheduleCommandBuilder);
        } catch (NoTaskIdFoundException | BadDateTimeException e) {
            return createInvalidCommand(Constants.CommandType.RESCHEDULE, originalInput);
        }

        setIsDateTimeSpecified(input, rescheduleCommandBuilder);

        Command rescheduleCommand = rescheduleCommandBuilder.build();
        return rescheduleCommand;
    }

    /**
     * This method finds the task ID (if any) in the input, removes it from the
     * input, and adds the task ID to the rename command builder. If properly
     * called, the task ID should be located at index 0 of the input String.
     * This should be the case if the edit command identifier has been removed
     * beforehand. In the case where no task ID can be found, a NULL value is
     * added to the builder instead.
     *
     * @param input
     *            the user input String.
     * @param rescheduleCommandBuilder
     *            the reschedule command object builder.
     * @return the input String, without the task ID.
     */
    protected String setAndRemoveTaskId(String input, Builder rescheduleCommandBuilder) throws NoTaskIdFoundException {
        assert input != null && rescheduleCommandBuilder != null;

        Pattern taskIdPattern = Pattern.compile(Constants.REGEX_TASKID);
        Matcher taskIdMatcher = taskIdPattern.matcher(input);

        // If the input is empty, or the matcher is unable to find a task ID,
        // then effectively the user input
        // does not contain a task ID
        if (!taskIdMatcher.find() || input.isEmpty()) {
            throw new NoTaskIdFoundException(input);
        } else {
            assert taskIdMatcher.start() == 0;
            // The task ID for all rename command inputs should be the first
            // instance of all task ID instances found.
            String taskId = getTrimmedSubstringInRange(input, taskIdMatcher.start(), taskIdMatcher.end());
            rescheduleCommandBuilder.setId(toInteger(taskId));

            // Remove the task ID from the input
            input = getTrimmedSubstringInRange(input, taskIdMatcher.end(), input.length());
        }

        return input;
    }

    /**
     * This method finds the date time fields from the user input, and adds them
     * to the RescheduleCommand object builder. If the previous inputs were all
     * properly executed, then the input itself should be the task date time
     * fields.
     *
     * @param input
     *            the String that corresponds to the new task date time.
     * @return the input String.
     */
    private String setTaskDateTime(String input, Builder builder) throws BadDateTimeException {
        assert input != null && builder != null;

        if (input.isEmpty()) {
            // No date time specified: user does not want any date/time
            // restrictions on task
            return input;
        }

        DateTimeParser dateTimeParser = DateTimeParser.getInstance();

        String dateTimeString = input;
        // The 'to' keyword needs special attention as it can change either
        // start or end date time depending on the current task type of the task
        if (input.matches("(to )?" + Constants.REGEX_DATE_TIME_FORMAT)) {
            if (input.matches("(to )" + Constants.REGEX_DATE_TIME_FORMAT)) {
                dateTimeString = getTrimmedSubstringInRange(input, 3, input.length());
            }

            DateTimeObject dateTimeObject = dateTimeParser.parseDateTime(dateTimeString);

            // Since we are parsing an input that strictly matches
            // DATE_TIME_FORMAT, dateTimeString should be parsed as a point
            // task, with only start date time.
            assert dateTimeObject.getTaskType().equals(Constants.TaskType.POINT);
            assert dateTimeObject.getStartDateTime() != null && dateTimeObject.getEndDateTime() == null;
            builder.setShiftedDateTime(dateTimeObject.getStartDateTime());

        } else if (input.matches(Constants.REGEX_POINT_TASK_IDENTIFIER + "|" + Constants.REGEX_DEADLINE_IDENTIFIER
            + "|" + Constants.REGEX_EVENT_IDENTIFIER + "|(to " + Constants.REGEX_DATE_TIME_FORMAT + " to "
            + Constants.REGEX_DATE_TIME_FORMAT + ")")) {

            if (input.matches("to " + Constants.REGEX_DATE_TIME_FORMAT + " to " + Constants.REGEX_DATE_TIME_FORMAT)) {
                dateTimeString = input.replaceAll("^to ", "from ");
            }
            System.out.println(dateTimeString);

            // If the input explicitly specifies a deadline, event, or point
            // task, then set start/end date time accordingly
            DateTimeObject dateTimeObject = DateTimeParser.getInstance().parseDateTime(dateTimeString);
            builder.setStartDateTime(dateTimeObject.getStartDateTime());
            builder.setEndDateTime(dateTimeObject.getEndDateTime());
        } else {
            throw new BadDateTimeException(input);
        }

        return input;
    }

    /**
     * This method sets the relevant boolean flags to true according to if the
     * input contains a date or a time or both.
     *
     * @param input
     *            the input on which checks are made to see if it contains a
     *            date, time or both.
     * @param rescheduleCommandBuilder
     *            the RescheduleCommandBuilder object.
     */
    private void setIsDateTimeSpecified(String input, Builder rescheduleCommandBuilder) {
        Pattern pattern = Pattern.compile(Constants.REGEX_TIME_FORMAT);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            rescheduleCommandBuilder.setShiftedTimeSpecified(true);
        }

        pattern = Pattern.compile(Constants.REGEX_DATE_FORMAT + "|" + Constants.REGEX_TIME_RELATIVE);
        matcher = pattern.matcher(input);
        if (matcher.find()) {
            rescheduleCommandBuilder.setShiftedDateSpecified(true);
        }

    }

    private boolean isValidRescheduleInput(String input) {
        return isValidInput(input) && getFirstWord(input).matches(Constants.REGEX_RESCHEDULE);
    }
}
