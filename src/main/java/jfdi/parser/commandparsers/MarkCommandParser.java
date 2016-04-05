// @@author A0127393B

package jfdi.parser.commandparsers;

import java.util.Collection;
import java.util.HashSet;

import jfdi.logic.commands.MarkTaskCommand;
import jfdi.logic.commands.MarkTaskCommand.Builder;
import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants;
import jfdi.parser.Constants.CommandType;
import jfdi.parser.exceptions.BadTaskIdException;

/**
 * The MarkCommandParser class is used to parse a given 'Mark' user input. The
 * 'Mark' user input is given by the user when there is a need to mark a task
 * (denoted by its taskID) as complete. Use this class in tandem with the
 * UnmarkCommandParser class to mark a task as complete to incomplete. The
 * format of an Unmark command is given by: {mark identifier} {task IDs}. Note
 * that task IDs can be represented as a range i.e. "1-10".
 *
 * @author Leonard Hio
 *
 */
public class MarkCommandParser extends AbstractCommandParser {

    public static MarkCommandParser instance;

    private MarkCommandParser() {

    }

    public static MarkCommandParser getInstance() {
        if (instance == null) {
            return instance = new MarkCommandParser();
        }

        return instance;
    }

    /**
     * This method builds a MarkCommand by extracting out the list of taskIDs
     * specfied to be marked as complete by the user and passing it into the
     * command builder.
     *
     * @param input
     *            the user input, representing a mark command.
     * @return a MarkTaskCommand object, or an InvalidCommand if any exceptions
     *         are thrown, or if the input is not in a valid mark format.
     *
     */
    @Override
    public Command build(String input) {
        assert isValidInput(input)
            && matchesCommandType(input, CommandType.MARK);

        if (!isValidMarkCommand(input)) {
            return createInvalidCommand(CommandType.MARK, input);
        }
        input = removeFirstWord(input);
        Builder markTaskCommandBuilder = new Builder();
        Collection<Integer> taskIds = new HashSet<>();
        try {
            taskIds = getTaskIds(input);
        } catch (BadTaskIdException e) {
            return createInvalidCommand(CommandType.MARK, input);
        }
        System.out.println(taskIds);
        markTaskCommandBuilder.addTaskIds(taskIds);
        MarkTaskCommand markTaskCommand = markTaskCommandBuilder.build();
        return markTaskCommand;
    }

    /**
     * Checks to see if the given input is in a valid Mark format.
     */
    private boolean isValidMarkCommand(String input) {
        return input.trim().matches(Constants.REGEX_MARK_FORMAT);
    }
}
