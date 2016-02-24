package parser;

import java.util.Arrays;
import java.util.List;

import command.Command;

/**
 * The Parser class exposes the key API for the parser component.
 *
 * @author leona_000
 *
 */
public class Parser implements IParser {
    private static final String REGEX_WHITESPACE = " ";
    private static final String REGEX_ADD = "^(add)";
    private static final String REGEX_READ = "^(see)";
    private static final String REGEX_EDIT = "^(edit)";
    private static final String REGEX_DELETE = "^(delete)";

    private static final int INDEX_ACTION = 0;

    private Parser parserInstance;

    public Parser getInstance() {
        if (parserInstance == null) {
            parserInstance = new Parser();
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
        String[] userArgumentsAsArray = userInput.split(REGEX_WHITESPACE);
        return getList(userArgumentsAsArray);
    }

    private String getActionAsString(List<String> userArguments) {
        String userActionAsString = userArguments.get(INDEX_ACTION);
        return userActionAsString;
    }

    private Command getCommand(String userActionAsString, String input) {
        switch (userActionAsString) {
            case REGEX_ADD:
                return new AddCommandBuilder(input).build();
            case REGEX_READ:
                return new ReadCommandBuilder(input).build();
            case REGEX_EDIT:
                return new EditCommandBuilder(input).build();
            case REGEX_DELETE:
                return new DeleteCommandBuilder(input).build();
            default:
                return new AddCommandBuilder(input).build();
        }
    }

    public List<String> getList(String[] array) {
        return Arrays.asList(array);
    }
}
