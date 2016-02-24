package jfdi.parser.commandparsers;

import java.util.Arrays;
import java.util.List;

import jfdi.parser.ActionType;
import jfdi.parser.Constants;
import command.Command;

public class ListCommandParser extends CommandParser {
    private static ListCommandParser instance;

    public static ListCommandParser getInstance() {
        return (instance == null) ? instance = new ListCommandParser()
                : instance;
    }

    @Override
    public Command build(String input) {
        List<String> tags = getTags(input);
        return new Command(ActionType.READ, userInput, null, null, null, null);
    }

    private List<String> getTags(String input) {
        List<String> inputAsList = getInputAsList(input);
        return removeFirstIndex(inputAsList);
    }

    private List<String> removeFirstIndex(List<String> inputAsList) {
        inputAsList.remove(0);
        return inputAsList;
    }

    private List<String> getInputAsList(String input) {
        return Arrays.asList(input.split(Constants.REGEX_WHITESPACE));
    }
}
