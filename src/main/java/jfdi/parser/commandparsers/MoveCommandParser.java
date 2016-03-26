// @@author A0127393B

package jfdi.parser.commandparsers;

import jfdi.logic.commands.MoveDirectoryCommand.Builder;
import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants;

/**
 * This class parses the 'Move' user command, which the user inputs whenever he
 * or she wishes to migrate from one directory to another. The format for this
 * command is a singular word i.e. {Mark identifier}.
 *
 * @author Leonard Hio
 *
 */
public class MoveCommandParser extends AbstractCommandParser {
    public static AbstractCommandParser instance;

    private MoveCommandParser() {

    }

    public static AbstractCommandParser getInstance() {
        if (instance == null) {
            return instance = new MoveCommandParser();
        }

        return instance;
    }

    @Override
    public Command build(String input) {
        Builder builder = new Builder();
        String directoryName = "";

        if (!isValidMoveInput(input)) {
            return createInvalidCommand(Constants.CommandType.move, input);
        }

        directoryName = getDirectoryName(input);
        builder.setNewDirectory(directoryName);
        return builder.build();
    }

    private boolean isValidMoveInput(String input) {
        return input != null && !input.isEmpty()
            && input.trim().split(Constants.REGEX_WHITESPACE).length == 2;
    }

    private String getDirectoryName(String input) {
        assert isValidMoveInput(input);

        String directoryName = input.trim().split(Constants.REGEX_WHITESPACE)[1];
        return directoryName;
    }

}
