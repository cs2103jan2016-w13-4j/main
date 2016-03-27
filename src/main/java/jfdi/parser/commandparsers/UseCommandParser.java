// @@author A0127393B

package jfdi.parser.commandparsers;

import jfdi.logic.commands.UseDirectoryCommand.Builder;
import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants;
import jfdi.parser.Constants.CommandType;

/**
 * This class parses the 'Use' user command, which the user inputs whenever he
 * or she wishes to load tasks from another directory. The user input should
 * follow the format: {Use command} {Directory}
 *
 * @author Leonard Hio
 *
 */
public class UseCommandParser extends AbstractCommandParser {
    public static AbstractCommandParser instance;

    private UseCommandParser() {

    }

    public static AbstractCommandParser getInstance() {
        if (instance == null) {
            return instance = new UseCommandParser();
        }

        return instance;
    }

    /**
     * This method builds a UseCommand from the given input.
     */
    @Override
    public Command build(String input) {
        if (!isValidUseInput(input)) {
            return createInvalidCommand(CommandType.use, input);
        }

        String directoryName = getDirectoryName(input);

        Builder builder = new Builder();
        builder.setNewDirectory(directoryName);

        return builder.build();
    }

    private boolean isValidUseInput(String input) {
        return isValidInput(input) && input.trim().split(Constants.REGEX_WHITESPACE).length >= 2;
    }

    private String getDirectoryName(String input) {
        return removeFirstWord(input);
    }

}
