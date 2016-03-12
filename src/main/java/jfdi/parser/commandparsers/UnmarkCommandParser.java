package jfdi.parser.commandparsers;

import java.util.ArrayList;

import jfdi.logic.commands.UnmarkTaskCommandStub;
import jfdi.logic.commands.UnmarkTaskCommandStub.Builder;
import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants.CommandType;
import jfdi.parser.exceptions.NoTaskIdFoundException;

/**
 * The MarkCommandParser class is used to parse a given 'Unmark' user input. The
 * 'Unmark' user input is given by the user when there is a need to mark a task
 * (denoted by its taskID) as incomplete. Use this class in tandem with the
 * MarkCommandParser class to mark a task as incomplete to complete.
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
     *         exceptions are thrown.
     *
     */
    @Override
    public Command build(String input) {
        Builder unmarkTaskCommandBuilder = new Builder();
        ArrayList<Integer> taskIds;
        try {
            taskIds = getTaskIds(input);
        } catch (NoTaskIdFoundException e) {
            return createInvalidCommand(CommandType.unmark, input);
        }
        unmarkTaskCommandBuilder.addIds(taskIds);
        UnmarkTaskCommandStub unmarkTaskCommand = unmarkTaskCommandBuilder
                .build();
        return unmarkTaskCommand;
    }

}
