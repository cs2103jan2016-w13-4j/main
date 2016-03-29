// @@author A0127393B

package jfdi.parser.commandparsers;

import jfdi.logic.commands.DirectoryCommand.Builder;
import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants;
import jfdi.parser.Constants.CommandType;

/**
 * This class parses the Directory command input by the user. The Directory
 * command is in the format {Directory identifier} i.e. a singular word.
 *
 * @author Leonard Hio
 *
 */
public class DirectoryCommandParser extends AbstractCommandParser {
    public static DirectoryCommandParser instance;

    private DirectoryCommandParser() {
    }

    public static DirectoryCommandParser getInstance() {
        if (instance == null) {
            return instance = new DirectoryCommandParser();
        }
        return instance;
    }

    @Override
    public Command build(String input) {
        if (!isValidDirectoryCommand(input)) {
            return createInvalidCommand(CommandType.wildcard, input);
        }
        Builder builder = new Builder();
        return builder.build();
    }

    private boolean isValidDirectoryCommand(String input) {
        return isValidInput(input) && input.matches(Constants.REGEX_DIRECTORY);
    }
}
