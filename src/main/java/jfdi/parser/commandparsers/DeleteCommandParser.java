package jfdi.parser.commandparsers;

import java.util.ArrayList;

import jfdi.logic.commands.DeleteTaskCommand;
import jfdi.logic.commands.DeleteTaskCommand.Builder;
import jfdi.logic.interfaces.Command;

/**
 * The DeleteCommandParser class takes in a user input representing a "Delete"
 * command and parses it into an DeleteCommand object. All delete user commands
 * must be in the format: "{delete identifier} {task ID(s)}"
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
        Builder deleteCommandBuilder = new Builder();
        ArrayList<Integer> taskIds = new ArrayList<>();
        taskIds.addAll(getTaskIds(input));
        deleteCommandBuilder.addIds(taskIds);
        DeleteTaskCommand deleteCommand = deleteCommandBuilder.build();
        return deleteCommand;
    }

}
