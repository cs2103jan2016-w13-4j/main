// @@author A0127393B

package jfdi.parser.commandparsers;

import jfdi.logic.commands.ListCommand;
import jfdi.logic.commands.ListCommand.ListType;
import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants.CommandType;
import jfdi.parser.exceptions.InvalidInputException;

/**
 * The ListCommandParser class is used to parse a user input String that
 * resembles a list command. All user inputs for listing tasks must adhere to
 * the following format: {list identifier} {completed | all | incomplete}. If
 * the {completed | all} field is unspecified, all incomplete tasks will by
 * default be displayed.
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
        assert isValidInput(input)
            && matchesCommandType(input, CommandType.list);

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
        if (input.isEmpty() || input.matches("(?i)Incomplete")) {
            return ListType.INCOMPLETE;
        } else if (input.matches("(?i)Completed")) {
            return ListType.COMPLETED;
        } else if (input.matches("(?i)All")) {
            return ListType.ALL;
        } else if (input.matches("(?i)Alias(es)?")) {
            return ListType.ALIASES;
        } else if (input.matches("(?i)Overdue")) {
            return ListType.OVERDUE;
        } else if (input.matches("(?i)Upcoming")) {
            return ListType.UPCOMING;
        } else {
            throw new InvalidInputException(input);
        }
    }

}
