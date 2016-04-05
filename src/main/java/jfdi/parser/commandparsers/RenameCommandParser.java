// @@author A0127393B

package jfdi.parser.commandparsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jfdi.logic.commands.RenameTaskCommand;
import jfdi.logic.commands.RenameTaskCommand.Builder;
import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants;
import jfdi.parser.Constants.CommandType;
import jfdi.parser.exceptions.BadTaskDescriptionException;
import jfdi.parser.exceptions.NoTaskIdFoundException;

/**
 * The RenameCommandParser class is used to parse user input String that
 * resembles a rename command. All user inputs for renaming tasks must adhere to
 * the following format: {rename identifier} {task ID} {new task description}
 *
 * @author leona_000
 *
 */
public class RenameCommandParser extends AbstractCommandParser {

    public static AbstractCommandParser instance;

    private RenameCommandParser() {

    }

    public static AbstractCommandParser getInstance() {
        if (instance == null) {
            return instance = new RenameCommandParser();
        }

        return instance;
    }

    /**
     * This method builds a RenameCommand by extracting out the task ID of the
     * task to be renamed, then its new task description, passing all these
     * information into a RenameCommand object.
     */
    @Override
    public Command build(String input) {
        if (!isValidRenameInput(input)) {
            return createInvalidCommand(CommandType.RENAME, input);
        }

        String originalInput = input;
        Builder renameCommandBuilder = new Builder();
        // Remove the rename command identifier.
        input = removeFirstWord(input);
        try {
            input = setAndRemoveTaskId(input, renameCommandBuilder);
            setTaskDescription(input, renameCommandBuilder);
        } catch (NoTaskIdFoundException | BadTaskDescriptionException e) {
            return createInvalidCommand(Constants.CommandType.RENAME, originalInput);
        }

        RenameTaskCommand renameCommand = renameCommandBuilder.build();
        return renameCommand;
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
     * @param builder
     *            the rename command object builder.
     * @return the input String, without the task ID.
     */
    protected String setAndRemoveTaskId(String input, Builder builder) throws NoTaskIdFoundException {
        Pattern taskIdPattern = Pattern.compile(Constants.REGEX_TASKID);
        Matcher taskIdMatcher = taskIdPattern.matcher(input);

        // If the input is empty, or the matcher is unable to find a task ID,
        // then effectively the user input does not contain a task ID
        if (input.isEmpty() || !taskIdMatcher.find()) {
            throw new NoTaskIdFoundException(input);
        } else {
            assert taskIdMatcher.start() == 0;
            // The task ID for all rename command inputs should be the first
            // instance of all task ID instances found.
            String taskId = getTrimmedSubstringInRange(input, taskIdMatcher.start(), taskIdMatcher.end());
            builder.setId(toInteger(taskId));

            // Remove the task ID from the input
            input = getTrimmedSubstringInRange(input, taskIdMatcher.end(), input.length());
        }

        return input;
    }

    /**
     * This method finds the task description from the user input. If the
     * previous inputs were all properly executed, then the input itself should
     * be the task description.
     *
     * @param input
     *            the String including the task description.
     * @return the task description.
     * @throws BadTaskDescriptionException
     *             if the input is empty.
     */
    private String setTaskDescription(String input, Builder builder) throws BadTaskDescriptionException {
        if (!isValidInput(input)) {
            throw new BadTaskDescriptionException(input);
        }
        builder.setDescription(input);
        return input;
    }

    private boolean isValidRenameInput(String input) {
        return isValidInput(input) && getFirstWord(input).matches(Constants.REGEX_RENAME);

    }
}
