// @@author A0127393B

package jfdi.parser.commandparsers;

import jfdi.logic.commands.ExitCommand.Builder;
import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants;
import jfdi.parser.Constants.CommandType;

/**
 * This class parses the Wildcard command input by the user. The Wildcard
 * command is in the format {Exit identifier} i.e. a singular word.
 *
 * @author Leonard Hio
 *
 */
public class ExitCommandParser extends AbstractCommandParser {
    public static ExitCommandParser instance;

    private ExitCommandParser() {
    }

    public static ExitCommandParser getInstance() {
        if (instance == null) {
            return instance = new ExitCommandParser();
        }
        return instance;
    }

    @Override
    public Command build(String input) {
        if (!isValidExitCommand(input)) {
            return createInvalidCommand(CommandType.wildcard, input);
        }
        Builder builder = new Builder();
        return builder.build();
    }

    private boolean isValidExitCommand(String input) {
        assert input != null;
        return isSingleWord(input) && input.matches(Constants.REGEX_EXIT);
    }
}
