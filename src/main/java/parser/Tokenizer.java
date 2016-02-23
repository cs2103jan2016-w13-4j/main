package parser;

import java.util.Arrays;
import java.util.List;

import command.Command;

public class Tokenizer {

    private static final String REGEX_WHITESPACE = " ";
    private static final String REGEX_ADD = "^(add)";
    private static final String REGEX_READ = "^(see)";
    private static final String REGEX_EDIT = "^(edit)";
    private static final String REGEX_DELETE = "^(delete)";

    private static final int INDEX_ACTION = 0;

    private enum ActionType {
        ADD, READ, EDIT, DELETE
    }

    public Tokenizer() {
        init();
    }

    private void init() {
    }

    public void tokenize(String userInput) {
        List<String> userArguments = getUserArgumentsFromInput(userInput);
        ActionType action = getAction(userArguments);
        getCommand(action);
    }

    private List<String> getUserArgumentsFromInput(String userInput) {
        String[] userArgumentsAsArray = userInput.split(REGEX_WHITESPACE);
        return getList(userArgumentsAsArray);
    }

    private ActionType getAction(List<String> userArguments) {
        String actionAsString = userArguments.get(INDEX_ACTION);

        switch (actionAsString) {
            case REGEX_ADD:
                return ActionType.ADD;
            case REGEX_READ:
                return ActionType.READ;
            case REGEX_EDIT:
                return ActionType.EDIT;
            case REGEX_DELETE:
                return ActionType.DELETE;
            default:
                return ActionType.ADD;
        }
    }

    public Command getCommand(ActionType action) {
        return new Command();
    }

    public List<String> getList(String[] array) {
        return Arrays.asList(array);
    }
}
