package jfdi.parser.commandparsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jfdi.logic.commands.RescheduleTaskCommand.Builder;
import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants;
import jfdi.parser.DateTimeObject;
import jfdi.parser.DateTimeParser;
import jfdi.parser.exceptions.BadDateTimeException;
import jfdi.parser.exceptions.NoTaskIdFoundException;

/**
 * The RenameCommandParser class is used to parse user input String that
 * resembles a rename command. All user inputs for renaming tasks must adhere to
 * the following format: {reschedule identifier} {task ID} {new task schedule}
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
        assert isValidInput(input);
        String originalInput = input;
        Builder rescheduleCommandBuilder = new Builder();
        // Remove the reschedule command identifier.
        input = removeFirstWord(input);
        try {
            input = setAndRemoveTaskId(input, rescheduleCommandBuilder);
            setTaskDateTime(input, rescheduleCommandBuilder);
        } catch (NoTaskIdFoundException | BadDateTimeException e) {
            return createInvalidCommand(Constants.CommandType.reschedule,
                originalInput);
        }

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
    protected String setAndRemoveTaskId(String input,
        Builder rescheduleCommandBuilder) throws NoTaskIdFoundException {
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
            // instance of
            // all task ID instances found.
            String taskId = getTrimmedSubstringInRange(input,
                taskIdMatcher.start(), taskIdMatcher.end());
            rescheduleCommandBuilder.setId(toInteger(taskId));

            // Remove the task ID from the input
            input = getTrimmedSubstringInRange(input, taskIdMatcher.end(),
                input.length());
        }

        return input;
    }

    /**
     * This method finds the date time fields from the user input, and adds them
     * to the RescheduleCommand object builder. If the previous inputs were all
     * properly executed, then the input itself should be the task date time
     * fields. If the input does not match the regex for a correctly formatted
     * date time, an 'invalid' flag is set in the builder. If the input is empty
     * (meaning no date time is specified, effectively implying that the task is
     * meant to have no date time constraints), then an empty ArrayList is added
     * to the builder.
     *
     * @param input
     *            the String that corresponds to the new task date time.
     * @return the input string.
     */
    private String setTaskDateTime(String input, Builder builder)
        throws BadDateTimeException {
        System.out.println("setTaskDateTime " + input);
        if (input.isEmpty()) {
            // No date time specified: user does not want any date/time
            // restrictions on task
            return input;
        }
        DateTimeParser dateTimeParser = DateTimeParser.getInstance();

        // The 'to' keyword needs special attention as it can change either
        // start or end date time depending on the current task type of the task
        if (input.matches("(to )?" + Constants.REGEX_DATE_TIME_FORMAT)) {
            String dateTimeString = getTrimmedSubstringInRange(input, 3,
                input.length());
            DateTimeObject dateTimeObject = dateTimeParser
                .parseDateTime(dateTimeString);

            // Since we are parsing an input that strictly matches
            // DATE_TIME_FORMAT, dateTimeString should be parsed as a point
            // task, with only start date time.
            assert dateTimeObject.getTaskType()
                .equals(Constants.TaskType.point);
            assert dateTimeObject.getStartDateTime() != null
                && dateTimeObject.getEndDateTime() == null;
            builder.setShiftedDateTime(dateTimeObject.getStartDateTime());
        } else if (input.matches(Constants.REGEX_POINT_TASK_IDENTIFIER + "|"
            + Constants.REGEX_DEADLINE_IDENTIFIER + "|"
            + Constants.REGEX_EVENT_IDENTIFIER)) {
            System.out.println("Here");
            // If the input explicitly specifies a deadline or point task, then
            // set start/end date time accordingly
            DateTimeObject dateTimeObject = DateTimeParser.getInstance()
                .parseDateTime(input);
            System.out.println(dateTimeObject.getStartDateTime());
            System.out.println(dateTimeObject.getEndDateTime());
            builder.setStartDateTime(dateTimeObject.getStartDateTime());
            builder.setEndDateTime(dateTimeObject.getEndDateTime());
        } else {
            throw new BadDateTimeException(input);
        }

        return input;
    }

    private boolean isValidDateTime(String input) {
        return input.matches(Constants.REGEX_DATE_TIME_IDENTIFIER);
    }
}
