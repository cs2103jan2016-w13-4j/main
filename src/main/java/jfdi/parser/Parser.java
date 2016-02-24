package jfdi.parser;

import java.util.Arrays;
import java.util.List;

import jfdi.logic.interfaces.AbstractCommand;
import jfdi.parser.commandparsers.ListCommandParser;

/**
 * The Parser class exposes the key API for the parser component.
 *
 * @author leona_000
 *
 */
public class Parser implements IParser {
    private static Parser parserInstance;

    public static Parser getInstance() {
        if (parserInstance == null) {
            parserInstance = new Parser();
        }
        return parserInstance;
    }

    public AbstractCommand parse(String input) {
        List<String> userArguments = getUserArgumentsFromInput(input);
        String userActionAsString = getActionAsString(userArguments);
        AbstractCommand userCommand = getCommand(userActionAsString, input);
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

    private AbstractCommand getCommand(String userActionAsString, String input) {
        switch (userActionAsString) {
            case Constants.REGEX_ADD:
                // return AddCommandParser.getInstance().build(input);
            case Constants.REGEX_LIST:
                return ListCommandParser.getInstance().build(input);
                /*
                 * case Constants.REGEX_EDIT: return
                 * EditCommandParser.getInstance().build(input); case
                 * Constants.REGEX_DELETE: return
                 * DeleteCommandParser.getInstance().build(input);
                 */
            default:
                // return AddCommandParser.getInstance().build(input);
        }

        return null;
    }

    public List<String> getList(String[] array) {
        return Arrays.asList(array);
    }
}

