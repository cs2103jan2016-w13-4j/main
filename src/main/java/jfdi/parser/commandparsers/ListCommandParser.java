package jfdi.parser.commandparsers;

import jfdi.logic.commands.ListCommand;
import jfdi.logic.commands.ListCommand.ListType;
import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants.CommandType;
import jfdi.parser.exceptions.InvalidInputException;

/**
 * The ListCommandParser class is used to parse a user input String that
 * resembles a list command. All user inputs for listing tasks must adhere to
 * the following format: {list identifier} {completed | incomplete}. If the
 * {completed | incomplete} field is unspecified, all tasks currently in storage
 * will be displayed.
 *
 * @author Leonard Hio
 *
 */
public class ListCommandParser extends AbstractCommandParser {
    private static ListCommandParser instance;

    private ListCommandParser() {

    }

    public static ListCommandParser getInstance() {
        return instance == null ? instance = new ListCommandParser() : instance;
    }

    @Override
    /**
     * This method parses the user input (representing a list command) and
     * builds the ListCommand object.
     * @param input
     *            the user input String
     * @return the ListCommand object encapsulating the details of the list command.
     */
    public Command build(String input) {
        ListCommand.Builder builder = new ListCommand.Builder();
        ListType listType = null;
        try {
            listType = getListType(input);
        } catch (InvalidInputException e) {
            return createInvalidCommand(CommandType.list, input);
        }
        builder.setListType(listType);
        return builder.build();
    }

    /**
     * This method gets the type of the list, which corresponds to whether the
     * user wishes to list all tasks, or just those that are
     * completed/incomplete.
     *
     * @param input
     *            the list command input
     * @return the ListType specified in the command input
     * @throws InvalidInputException
     *             if no valid ListType is found
     */
    private ListType getListType(String input) throws InvalidInputException {
        input = removeFirstWord(input);
        if (input.isEmpty()) {
            return ListType.ALL;
        } else if (input.matches("(?i)Completed")) {
            return ListType.COMPLETED;
        } else if (input.matches("(?i)Incomplete")) {
            return ListType.INCOMPLETE;
        } else {
            throw new InvalidInputException(input);
        }
    }

}
