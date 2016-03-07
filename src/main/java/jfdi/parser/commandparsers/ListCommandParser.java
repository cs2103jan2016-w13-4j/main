package jfdi.parser.commandparsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jfdi.logic.commands.ListCommand;
import jfdi.parser.Constants;

/**
 * The ListCommandParser class is used to parse a user input String that
 * resembles a list command. All user inputs for listing tasks must adhere to
 * the following format: {list identifier} {tags}(optional). If the {tags} field
 * is unspecified, all tasks currently in storage will be displayed.
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
        ArrayList<String> withoutFirstIndex = new ArrayList<String>();
        for (int i = 1; i <= inputAsList.size() - 1; i++) {
            withoutFirstIndex.add(inputAsList.get(i));
        }
        return withoutFirstIndex;
    }

    private List<String> getInputAsList(String input) {
        return Arrays.asList(input.split(Constants.REGEX_WHITESPACE));
    }
}
