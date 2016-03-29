// @@author A0127393B

package jfdi.parser.commandparsers;

import jfdi.logic.commands.HelpCommand.Builder;
import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants;
import jfdi.parser.Constants.CommandType;

/**
 * This class parses the Help command input by the user. The Help command is in
 * the format {Help identifier} i.e. a singular word.
 *
 * @author Leonard Hio
 *
 */
public class HelpCommandParser extends AbstractCommandParser {
    public static HelpCommandParser instance;

    private HelpCommandParser() {
    }

    public static HelpCommandParser getInstance() {
        if (instance == null) {
            return instance = new HelpCommandParser();
        }
        return instance;
    }

    @Override
    public Command build(String input) {
        if (!isValidHelpCommand(input)) {
            return createInvalidCommand(CommandType.wildcard, input);
        }
        Builder builder = new Builder();
        return builder.build();
    }

    private boolean isValidHelpCommand(String input) {
        assert input != null;
        return isSingleWord(input) && input.matches(Constants.REGEX_HELP);
    }

}
