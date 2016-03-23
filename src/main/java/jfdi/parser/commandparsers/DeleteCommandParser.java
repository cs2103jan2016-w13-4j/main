package jfdi.parser.commandparsers;

import java.util.ArrayList;

import jfdi.logic.commands.DeleteTaskCommand;
import jfdi.logic.commands.DeleteTaskCommand.Builder;
import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants;
import jfdi.parser.Constants.CommandType;
import jfdi.parser.exceptions.BadTaskIdException;

/**
 * The DeleteCommandParser class takes in a user input representing a "Delete"
 * command and parses it into an DeleteCommand object. All delete user commands
 * must be in the format: "{delete identifier} {task ID(s)}". Additionally, task
 * IDs can be represented as a range i.e. "1-10", and all task IDs are to be
 * separated by a space and/or a comma.
 *
 * @author Leonard Hio
 *
 */
public class DeleteCommandParser extends AbstractCommandParser {

    public static DeleteCommandParser instance;

    private DeleteCommandParser() {

    }

    public static DeleteCommandParser getInstance() {
        if (instance == null) {
            return instance = new DeleteCommandParser();
        }

        return instance;
    }

    /**
     * This method builds a DeleteCommand by extracting out the list of taskIDs
     * specfied for deletion by the user and passing it into the command
     * builder.
     *
     * @param input
     *            the user input, representing a delete command.
     * @return a DeleteTaskCommand object, or an InvalidCommand if any
     *         exceptions are thrown.
     *
     */
    @Override
    public Command build(String input) {
        assert isValidInput(input)
            && matchesCommandType(input, CommandType.delete);

        if (!isValidDeleteCommand(input)) {
            return createInvalidCommand(CommandType.delete, input);
        }
        input = removeFirstWord(input);
        Builder deleteCommandBuilder = new Builder();
        ArrayList<Integer> taskIds = new ArrayList<>();
        try {
            taskIds.addAll(getTaskIds(input));
        } catch (BadTaskIdException e) {
            return createInvalidCommand(CommandType.delete, input);
        }
        deleteCommandBuilder.addIds(taskIds);
        DeleteTaskCommand deleteCommand = deleteCommandBuilder.build();
        return deleteCommand;
    }

    /**
     * Checks to see if the given input is in a valid Delete format.
     */
    private boolean isValidDeleteCommand(String input) {
        return input.trim().matches(Constants.REGEX_DELETE_FORMAT);
    }
}
