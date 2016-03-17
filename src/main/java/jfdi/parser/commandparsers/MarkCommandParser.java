package jfdi.parser.commandparsers;

import java.util.ArrayList;

import jfdi.logic.commands.MarkTaskCommand;
import jfdi.logic.commands.MarkTaskCommand.Builder;
import jfdi.logic.interfaces.Command;

/**
 * The MarkCommandParser class is used to parse a given 'Mark' user input. The
 * 'Mark' user input is given by the user when there is a need to mark a task
 * (denoted by its taskID) as complete. Use this class in tandem with the
 * UnmarkCommandParser class to mark a task as complete to incomplete.
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
     *         are thrown.
     *
     */
    @Override
    public Command build(String input) {
        Builder markTaskCommandBuilder = new Builder();
        ArrayList<Integer> taskIds;
        taskIds = getTaskIds(input);
        markTaskCommandBuilder.addTaskIds(taskIds);
        MarkTaskCommand markTaskCommand = markTaskCommandBuilder.build();
        return markTaskCommand;
    }

}
