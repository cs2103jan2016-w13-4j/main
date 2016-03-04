package jfdi.parser;

import java.util.Arrays;
import java.util.List;

import jfdi.logic.interfaces.Command;
import jfdi.parser.commandparsers.AddCommandParser;
import jfdi.parser.commandparsers.DeleteCommandParser;
import jfdi.parser.commandparsers.ListCommandParser;

/**
 * The Parser class exposes the key API for the parser component.
 *
 * @author leona_000
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

    public Command parse(String input) {
        List<String> userArguments = getUserArgumentsFromInput(input);
        String userActionAsString = getActionAsString(userArguments);
        Command userCommand = getCommand(userActionAsString, input);
        return userCommand;
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
        switch (userActionAsString) {
            case Constants.REGEX_ADD:
                return AddCommandParser.getInstance().build(input);
            case Constants.REGEX_LIST:
                return ListCommandParser.getInstance().build(input);
            case Constants.REGEX_DELETE:
                return DeleteCommandParser.getInstance().build(input);
                /*
                 * case Constants.REGEX_EDIT: return
                 * EditCommandParser.getInstance().build(input); case
                 * Constants.REGEX_DELETE: return
                 * DeleteCommandParser.getInstance().build(input);
                 */
            default:
                return AddCommandParser.getInstance().build(input);
        }

    }

    public List<String> getList(String[] array) {
        return Arrays.asList(array);
    }
}

