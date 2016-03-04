package jfdi.parser.commandparsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jfdi.logic.interfaces.AbstractCommand;
import jfdi.parser.Constants;

public abstract class CommandParser {
    protected String userInput;

    public abstract AbstractCommand build(String input);

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

    /**
     * This method searches the input String for all instances of task IDs, and
     * returns them as an ArrayList. The search proceeds from left to right i.e.
     * from the start of the input to the end.
     *
     * @param input
     *            the String from which task IDs are found and extracted.
     * @return an ArrayList of task IDs, all Strings. If no task IDs can be
     *         found, an empty ArrayList is returned.
     */
    protected ArrayList<String> getTaskIds(String input) {
        Pattern pattern = Pattern.compile(Constants.REGEX_TASKID);
        Matcher matcher = pattern.matcher(input);
        ArrayList<String> taskIds = new ArrayList<>();

        while (matcher.find()) {
            String taskId = getTrimmedSubstringInRange(input, matcher.start(),
                    matcher.end());
            taskIds.add(taskId);
        }

        return taskIds;
    }

    /**
     * Removes the first word in the input string, and returns the rest of the
     * input.
     *
     * @param input
     *            the string from which the first word is to be removed
     * @return the input string without the first word and the whitespace
     *         separating the first word from the rest of the string. If the
     *         string only consists of one word, return null.
     */
    protected String removeFirstWord(String input) {
        String[] splitInput = input.split(Constants.REGEX_WHITESPACE, 2);
        if (splitInput.length == 1) {
            return null;
        } else {
            return splitInput[1];
        }
    }
}
