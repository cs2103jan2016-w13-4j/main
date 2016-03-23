package jfdi.parser.commandparsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jfdi.logic.commands.InvalidCommand;
import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants;
import jfdi.parser.Constants.CommandType;
import jfdi.parser.exceptions.BadTaskIdException;

/**
 * The AbstractCommandParser class is the abstract class that all specific
 * command parser should inherit from. It also contains the full implementation
 * of some methods that can be used across many different command parsers.
 *
 * @author Leonard Hio
 *
 */
public abstract class AbstractCommandParser {
    protected String userInput;

    /**
     * This method is used to build a command object based on the input string.
     *
     * @param input
     *            the input string from which a command object is to be built
     * @return a Command object.
     */
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
    protected Collection<Integer> getTaskIds(String input)
        throws BadTaskIdException {
        Set<Integer> taskIdsForDeletion = new HashSet<Integer>();
        input = input.replaceAll("(\\d+)[ ]*-[ ]*(\\d+)", "$1-$2");
        input = input.replaceAll("[ ]+", ",");
        input = input.replaceAll(",+", ",");

        String[] taskIds = input.split(",");
        for (String taskId : taskIds) {
            if (taskId.matches("\\d+")) {
                taskIdsForDeletion.add(toInteger(taskId));
            } else if (taskId.matches("\\d+[ ]?-[ ]?\\d+")) {
                taskIdsForDeletion.addAll(getTaskIdsFromRange(taskId));
            } else {
                throw new BadTaskIdException(input);
            }
        }

        return taskIdsForDeletion;
    }

    private Collection<? extends Integer> getTaskIdsFromRange(String taskId)
        throws BadTaskIdException {
        assert taskId.matches("\\d+[ ]?-[ ]?\\d+");
        String[] taskIdRangeArray = taskId.split("-");
        Collection<Integer> taskIdsInRange = new HashSet<>();
        assert taskIdRangeArray.length == 2;
        if (toInteger(taskIdRangeArray[0]) > toInteger(taskIdRangeArray[1])) {
            throw new BadTaskIdException(taskId);
        } else {
            for (int i = toInteger(taskIdRangeArray[0]); i <= toInteger(taskIdRangeArray[1]); i++) {
                taskIdsInRange.add(i);
            }
        }

        return taskIdsInRange;
    }

    /**
     * This method converts a string object to an Integer.
     *
     * @param toInt
     *            a string that is to be converted to an Integer.
     * @return an Integer representation of the string.
     *
     * @throws NumberFormatException
     *             if the input string does not represent an Integer.
     */
    protected Integer toInteger(String toInt) throws NumberFormatException {
        return Integer.parseInt(toInt);
    }

    /**
     * Removes the first word in the input string, and returns the rest of the
     * input.
     *
     * @param input
     *            the string from which the first word is to be removed
     * @return the input string without the first word and the whitespace
     *         separating the first word from the rest of the string. If the
     *         string only consists of one word, return an empty string.
     */
    protected String removeFirstWord(String input) {
        String[] splitInput = input.split(Constants.REGEX_WHITESPACE, 2);
        if (splitInput.length == 1) {
            return "";
        } else {
            return splitInput[1];
        }
    }

    /**
     * This method builds an InvalidCommand object. An InvalidCommand object has
     * to be built whenever a user inputs a String that cannot be parsed by the
     * parser for whatever reason.
     *
     * @param commandType
     *            the command type specified in the user's input.
     * @param inputString
     *            the actual input of the user.
     * @return an InvalidCommand object, containing the command type of the
     *         invalid user's input, and the user's input itself.
     */
    protected InvalidCommand createInvalidCommand(CommandType commandType,
        String inputString) {
        InvalidCommand.Builder invalidCommandBuilder = new InvalidCommand.Builder();
        invalidCommandBuilder.setInputString(inputString);
        invalidCommandBuilder.setCommandType(commandType);

        return invalidCommandBuilder.build();
    }

    /**
     * This method checks if the given input is valid. A valid input is one that
     * is (1) not empty, and (2) not made of whitespaces only.
     *
     * @param input
     *            the input which validity is to be checked
     * @return true if the input is valid; false otherwise
     */
    protected boolean isValidInput(String input) {
        return input != null && !input.isEmpty() && !input.trim().isEmpty();
    }

}
