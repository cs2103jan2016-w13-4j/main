package jfdi.parser.commandparsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jfdi.logic.commands.ListCommand;
import jfdi.parser.Constants;

public class ListCommandParser extends CommandParser {
    private static ListCommandParser instance;

    private ListCommandParser() {

    }

    public static ListCommandParser getInstance() {
        return (instance == null) ? instance = new ListCommandParser()
                : instance;
    }

    @Override
    public ListCommand build(String input) {
        ArrayList<String> tags = getTags(input);
        ListCommand.Builder builder = new ListCommand.Builder();
        builder.addTags(tags);
        return builder.build();
    }

    private ArrayList<String> getTags(String input) {
        List<String> inputAsList = getInputAsList(input);
        return removeFirstIndex(inputAsList);
    }

    private ArrayList<String> removeFirstIndex(List<String> inputAsList) {
        inputAsList.remove(0);
        ArrayList<String> withoutFirstIndex = new ArrayList<String>();
        withoutFirstIndex.addAll(inputAsList);
        return withoutFirstIndex;
    }

    private List<String> getInputAsList(String input) {
        return Arrays.asList(input.split(Constants.REGEX_WHITESPACE));
    }
}
