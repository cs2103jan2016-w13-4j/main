package jfdi.parser.commandparsers;

import java.util.Collection;
import java.util.HashSet;

import jfdi.logic.commands.UnmarkTaskCommand;
import jfdi.logic.commands.UnmarkTaskCommand.Builder;
import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants;
import jfdi.parser.Constants.CommandType;
import jfdi.parser.exceptions.BadTaskIdException;

/**
 * The UnmarkCommandParser class is used to parse a given 'Unmark' user input.
 * The 'Unmark' user input is given by the user when there is a need to mark a
 * task (denoted by its taskID) as incomplete. Use this class in tandem with the
 * MarkCommandParser class to mark a task as incomplete to complete. The format
 * of an Unmark command is given by: {unmark identifier} {task IDs}. Note that
 * task IDs can be represented as a range i.e. "1-10".
 *
 * @author Leonard Hio
 *
 */
public class UnmarkCommandParser extends AbstractCommandParser {

    public static UnmarkCommandParser instance;

    private UnmarkCommandParser() {

    }

    public static UnmarkCommandParser getInstance() {
        if (instance == null) {
            return instance = new UnmarkCommandParser();
        }

        return instance;
    }

    /**
     * This method builds an UnmarkCommand by extracting out the list of taskIDs
     * specfied to be marked as incomplete by the user and passing it into the
     * command builder.
     *
     * @param input
     *            the user input, representing an unmark command.
     * @return an UnmarkTaskCommand object, or an InvalidCommand if any
     *         exceptions are thrown, or if the input is not in a valid Unmark
     *         format.
     *
     */
    @Override
    public Command build(String input) {
        assert isValidInput(input)
            && matchesCommandType(input, CommandType.unmark);

        if (!isValidUnmarkCommand(input)) {
            return createInvalidCommand(CommandType.unmark, input);
        }
        input = removeFirstWord(input);
        Builder unmarkTaskCommandBuilder = new Builder();
        Collection<Integer> taskIds = new HashSet<>();
        try {
            taskIds = getTaskIds(input);
        } catch (BadTaskIdException e) {
            return createInvalidCommand(CommandType.unmark, input);
        }
        unmarkTaskCommandBuilder.addTaskIds(taskIds);
        UnmarkTaskCommand unmarkTaskCommand = unmarkTaskCommandBuilder.build();
        return unmarkTaskCommand;
    }

    /**
     * Checks to see if the given input is in a valid Unmark format.
     */
    private boolean isValidUnmarkCommand(String input) {
        return input.trim().matches(Constants.REGEX_UNMARK_FORMAT);
    }

}
