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
            return CommandType.add;
        } else if (input.matches(Constants.REGEX_LIST)) {
            return CommandType.list;
        } else if (input.matches(Constants.REGEX_DELETE)) {
            return CommandType.delete;
        } else if (input.matches(Constants.REGEX_RENAME)) {
            return CommandType.rename;
        } else if (input.matches(Constants.REGEX_RESCHEDULE)) {
            return CommandType.reschedule;
        } else if (input.matches(Constants.REGEX_SEARCH)) {
            return CommandType.search;
        } else if (input.matches(Constants.REGEX_MARK)) {
            return CommandType.mark;
        } else if (input.matches(Constants.REGEX_UNMARK)) {
            return CommandType.unmark;
        } else if (input.matches(Constants.REGEX_ALIAS)) {
            return CommandType.alias;
        } else if (input.matches(Constants.REGEX_UNALIAS)) {
            return CommandType.unalias;
        } else if (input.matches(Constants.REGEX_DIRECTORY)) { // TODO
            return CommandType.directory;
        } else if (input.matches(Constants.REGEX_MOVE)) {
            return CommandType.move;
        } else if (input.matches(Constants.REGEX_USE)) {
            return CommandType.use;
        } else if (input.matches(Constants.REGEX_UNDO)) { // TODO
            return CommandType.undo;
        } else if (input.matches(Constants.REGEX_HELP)) { // TODO
            return CommandType.help;
        } else if (input.matches(Constants.REGEX_WILDCARD)) { // TODO
            return CommandType.wildcard;
        } else {
            return CommandType.invalid;
        }

    }

}
