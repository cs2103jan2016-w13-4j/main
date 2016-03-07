package jfdi.parser;

import java.util.Arrays;
import java.util.List;

import jfdi.logic.interfaces.Command;
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
        List<String> userArguments = getUserArgumentsFromInput(input);
        String userActionAsString = getActionAsString(userArguments);
        Command userCommand = getCommand(userActionAsString, input);
        return userCommand;
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

    private List<String> getUserArgumentsFromInput(String userInput) {
        String[] userArgumentsAsArray = userInput
                .split(Constants.REGEX_WHITESPACE);
        return getList(userArgumentsAsArray);
    }

    private String getActionAsString(List<String> userArguments) {
        String userActionAsString = userArguments.get(Constants.INDEX_ACTION);
        return userActionAsString;
    }

    private Command getCommand(String userActionAsString, String input) {
        if (userActionAsString.matches(Constants.REGEX_ADD)) {
            return AddCommandParser.getInstance().build(input);
        } else if (userActionAsString.matches(Constants.REGEX_LIST)) {
            return ListCommandParser.getInstance().build(input);
        } else if (userActionAsString.matches(Constants.REGEX_DELETE)) {
            return DeleteCommandParser.getInstance().build(input);
        } else if (userActionAsString.matches(Constants.REGEX_RENAME)) {
            return RenameCommandParser.getInstance().build(input);
        } else if (userActionAsString.matches(Constants.REGEX_RESCHEDULE)) {
            return RescheduleCommandParser.getInstance().build(input);
        } else {
            return AddCommandParser.getInstance().build(input);
        }
    }

    public List<String> getList(String[] array) {
        return Arrays.asList(array);
    }
}
