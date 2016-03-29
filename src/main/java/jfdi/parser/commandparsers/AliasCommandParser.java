// @@author A0127393B

package jfdi.parser.commandparsers;

import jfdi.logic.commands.AliasCommand.Builder;
import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants;
import jfdi.parser.Constants.CommandType;
import jfdi.parser.ParserUtils;

/**
 * The AliasCommandParser class takes in a user input representing an "Alias"
 * command and parses it into an AliasCommand object. This AliasCommand object
 * will then contain the associated mapping of alias to command type. All alias
 * user commands must be in the format:
 * "{alias identifier} {command type} {alias}"
 *
 * @author Leonard Hio
 *
 */
public class AliasCommandParser extends AbstractCommandParser {
    public static AliasCommandParser instance;

    private AliasCommandParser() {
    }

    public static AliasCommandParser getInstance() {
        if (instance == null) {
            return instance = new AliasCommandParser();
        }
        return instance;
    }

    @Override
    public Command build(String input) {
        if (!isValidFormat(input)) {
            return createInvalidCommand(Constants.CommandType.alias, input);
        }
        Builder builder = new Builder();
        String command = null;
        String alias = null;

        command = getCommand(input);

        alias = getAlias(input);

        builder.setIsValid(isPartOfCommandKeyword(alias));

        builder.setCommand(command);
        builder.setAlias(alias);

        return builder.build();
    }

    // ===================================
    // First Level of Abstraction
    // ===================================

    /**
     * This method checks to see if the format of the Alias command input is
     * valid. In this case, a user input is considered valid if it is 3 words
     * long.
     *
     * @param input
     *            the Alias input.
     * @return True if it is valid; false otherwise.
     */
    private boolean isValidFormat(String input) {
        if (!isValidInput(input)) {
            return false;
        }
        return input.split(Constants.REGEX_WHITESPACE).length == 3
            && getFirstWord(input).matches(Constants.REGEX_ALIAS);
    }

    /**
     * This method extracts the command from the user input.
     *
     * @param input
     *            the user input representing an alias command.
     * @return the command.
     */
    private String getCommand(String input) {
        assert isValidFormat(input);
        // Following the established format of alias command inputs, the command
        // type can be located as the second word.

        String secondWord = getSecondWord(input);
        return secondWord;
    }

    /**
     * This method extracts the alias from the user input.
     *
     * @param input
     *            the user input representing an alias command.
     * @return the alias.
     */
    private String getAlias(String input) {
        assert isValidFormat(input);
        String alias = getThirdWord(input);
        return alias;
    }

    private boolean isPartOfCommandKeyword(String alias) {
        return ParserUtils.getCommandType(alias).equals(CommandType.invalid);
    }

    // ===================================
    // Second Level of Abstraction
    // ===================================

    private String getSecondWord(String input) {
        return input.split(Constants.REGEX_WHITESPACE)[1];
    }

    private String getThirdWord(String input) {
        return input.split(Constants.REGEX_WHITESPACE)[2];
    }

}
