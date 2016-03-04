package jfdi.parser.commandparsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants;

public abstract class CommandParser {
    protected String userInput;

    public abstract Command build(String input);

    protected List<String> getArguments(String input) {
        List<String> arguments = new ArrayList<String>();
        arguments.addAll(Arrays.asList(input.trim().split(
                Constants.REGEX_WHITESPACE)));
        return arguments;
    }

    /**
     * Get the substring of an input String from startindex inclusive to
     * endindex exclusive, trimming it at the same time.
     *
     * @param input
     *            a string to get substring of
     * @param startIndex
     *            index of the start of the substring (inclusive)
     * @param endIndex
     *            index of the end of the substring (exclusive)
     * @return the trimmed substring
     */
    protected String getTrimmedSubstringInRange(String input, int startIndex,
            int endIndex) {
        return input.substring(startIndex, endIndex).trim();
    }
}
