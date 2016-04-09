// @@author A0127393B

package jfdi.parser;

import jfdi.parser.Constants.CommandType;

/**
 * This class contains some common methods to be shared amongst all Parser
 * classes.
 *
 * @author Leonard Hio
 *
 */
public class ParserUtils {

    /**
     * This method returns the CommandType associated with the input String.
     *
     * @param input
     *            a String interpretation of a CommandType.
     * @return a CommandType enum. If no matches were found, return invalid.
     */
    public static CommandType getCommandType(String input) {
        assert input.split(Constants.REGEX_WHITESPACE).length == 1;
        if (input.matches(Constants.REGEX_ADD)) {
            return CommandType.ADD;
        } else if (input.matches(Constants.REGEX_LIST)) {
            return CommandType.LIST;
        } else if (input.matches(Constants.REGEX_DELETE)) {
            return CommandType.DELETE;
        } else if (input.matches(Constants.REGEX_RENAME)) {
            return CommandType.RENAME;
        } else if (input.matches(Constants.REGEX_RESCHEDULE)) {
            return CommandType.RESCHEDULE;
        } else if (input.matches(Constants.REGEX_SEARCH)) {
            return CommandType.SEARCH;
        } else if (input.matches(Constants.REGEX_MARK)) {
            return CommandType.MARK;
        } else if (input.matches(Constants.REGEX_UNMARK)) {
            return CommandType.UNMARK;
        } else if (input.matches(Constants.REGEX_ALIAS)) {
            return CommandType.ALIAS;
        } else if (input.matches(Constants.REGEX_UNALIAS)) {
            return CommandType.UNALIAS;
        } else if (input.matches(Constants.REGEX_DIRECTORY)) {
            return CommandType.DIRECTORY;
        } else if (input.matches(Constants.REGEX_MOVE)) {
            return CommandType.MOVE;
        } else if (input.matches(Constants.REGEX_USE)) {
            return CommandType.USE;
        } else if (input.matches(Constants.REGEX_UNDO)) {
            return CommandType.UNDO;
        } else if (input.matches(Constants.REGEX_HELP)) {
            return CommandType.HELP;
        } else if (input.matches(Constants.REGEX_WILDCARD)) {
            return CommandType.WILDCARD;
        } else if (input.matches(Constants.REGEX_EXIT)) {
            return CommandType.EXIT;
        } else {
            return CommandType.INVALID;
        }

    }

}
