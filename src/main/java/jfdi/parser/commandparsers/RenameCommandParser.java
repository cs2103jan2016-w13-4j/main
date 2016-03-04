package jfdi.parser.commandparsers;

import jfdi.logic.commands.RenameCommandStub;
import jfdi.logic.commands.RenameCommandStub.Builder;
import jfdi.logic.interfaces.AbstractCommand;
import jfdi.parser.Constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The RenameCommandParser class is used to parse user input String that
 * resembles a rename command. All user inputs for renaming tasks must adhere to
 * the following format: {rename identifier} {task ID} {new task description}
 *
 * @author leona_000
 *
 */
public class RenameCommandParser extends CommandParser {

    public static CommandParser instance;

    private RenameCommandParser() {

    }

    public static CommandParser getInstance() {
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
    public AbstractCommand build(String input) {
        RenameCommandStub.Builder renameCommandBuilder = new RenameCommandStub.Builder();
        // Remove the rename command identifier.
        input = removeFirstWord(input);
        input = setAndRemoveTaskId(input, renameCommandBuilder);

        setTaskDescription(input, renameCommandBuilder);

        RenameCommandStub renameCommand = renameCommandBuilder.build();
        return renameCommand;
    }

    /**
     * This method finds the task ID (if any) in the input, removes it from the
     * input, and adds the task ID to the rename command builder.
     * If properly called, the task ID should be located at index 0 of the input
     * String. This should be the case if the rename command identifier has been
     * removed beforehand.
     * In the case where no task ID can be found, a NULL value is added to the
     * builder instead.
     *
     * @param input
     *            the user input String.
     * @param builder
     *            the rename command object builder.
     * @return the input String, without the task ID.
     */
    private String setAndRemoveTaskId(String input, Builder builder) {
        Pattern taskIdPattern = Pattern.compile(Constants.REGEX_TASKID);
        Matcher taskIdMatcher = taskIdPattern.matcher(input);

        // If the input is empty, or the matcher is unable to find a task ID,
        // then effectively the user input
        // does not contain a task ID
        if (!taskIdMatcher.find() || input.isEmpty()) {
            builder.addTaskId(null);
        } else {
            assert taskIdMatcher.start() == 0;
            // The task ID for all rename command inputs should be the first
            // instance of
            // all task ID instances found.
            String taskId = getTrimmedSubstringInRange(input,
                    taskIdMatcher.start(), taskIdMatcher.end());

            // Remove the task ID from the input
            input = getTrimmedSubstringInRange(input, taskIdMatcher.end(),
                    input.length());
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
     * @return the task description. This can be an empty String.
     */
    private String setTaskDescription(String input, Builder builder) {
        builder.addTaskDescription(input);
        return input;
    }
}
