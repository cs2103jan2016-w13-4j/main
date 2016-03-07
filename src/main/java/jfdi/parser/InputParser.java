package jfdi.parser;

import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants.CommandType;
import jfdi.parser.commandparsers.AddCommandParser;
import jfdi.parser.commandparsers.DeleteCommandParser;
import jfdi.parser.commandparsers.ListCommandParser;
import jfdi.parser.commandparsers.RenameCommandParser;
import jfdi.parser.commandparsers.RescheduleCommandParser;
import jfdi.parser.exceptions.InvalidInputException;

/**
 * The InputParser class is used to parse a String input into its associated
 * Command object. This class should be used to interface with the Logic
 * component via the parse(String) method.
 *
 * @author Leonard Hio
 *
 */
public class InputParser implements IParser {
    private static InputParser parserInstance;

    public static InputParser getInstance() {
        if (parserInstance == null) {
            parserInstance = new InputParser();
        }
        return parserInstance;
    }

    @Override
    public Command parse(String input) throws InvalidInputException {
        if (!isValidInput(input)) {
            throw new InvalidInputException(input);
        }

        // / input is guaranteed to be at least one word long
        String firstWord = getFirstWord(input);
        CommandType commandType = getCommandType(firstWord);
        Command userCommand = getCommand(commandType, input);
        return userCommand;
    }

    /**
     * This method returns the first word of the input String.
     *
     * @param input
     *            a String from which the first word is to be returned.
     * @return the first word of the input String.
     */
    private String getFirstWord(String input) {
        assert isValidInput(input);
        return input.trim().split(Constants.REGEX_WHITESPACE)[0];
    }

    /**
     * This method returns the CommandType associated with the input String.
     *
     * @param input
     *            a String interpretation of a CommandType.
     * @return a CommandType enum.
     */
    private CommandType getCommandType(String input) {
        assert input.split(Constants.REGEX_WHITESPACE).length == 1;
        if (input.matches(Constants.REGEX_ADD)) {
            return Constants.CommandType.add;
        } else if (input.matches(Constants.REGEX_LIST)) {
            return Constants.CommandType.list;
        } else if (input.matches(Constants.REGEX_DELETE)) {
            return Constants.CommandType.delete;
        } else if (input.matches(Constants.REGEX_RENAME)) {
            return Constants.CommandType.rename;
        } else if (input.matches(Constants.REGEX_RESCHEDULE)) {
            return Constants.CommandType.reschedule;
        } else {
            return Constants.CommandType.add;
        }

    }

    /**
     * This method gets the Command object associated with the user's input.
     *
     * @param commandType
     *            the CommandType of the user's input.
     * @param input
     *            the user's input itself.
     * @return a Command object that was built from the user's input.
     */
    private Command getCommand(CommandType commandType, String input) {
        switch (commandType) {
            case add:
                return AddCommandParser.getInstance().build(input);
            case list:
                return ListCommandParser.getInstance().build(input);
            case delete:
                return DeleteCommandParser.getInstance().build(input);
            case rename:
                return RenameCommandParser.getInstance().build(input);
            case reschedule:
                return RescheduleCommandParser.getInstance().build(input);
            default:
                return AddCommandParser.getInstance().build(input);
        }
    }

    /**
     * This method checks if the given input is valid. A valid input is one that
     * is (1) not empty, and (2) not made of whitespaces only.
     *
     * @param input
     *            the input which validity is to be checked
     * @return true if the input is valid; false otherwise
     */
    private boolean isValidInput(String input) {
        return !(input.isEmpty() || input.trim().isEmpty());
    }
}
