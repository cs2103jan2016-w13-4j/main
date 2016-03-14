package jfdi.parser.commandparsers;

import jfdi.logic.commands.UseTaskCommandStub.Builder;
import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants;
import jfdi.parser.Constants.CommandType;

/**
 * This class parses the 'Use' user command, which the user inputs whenever he
 * or she wishes to load tasks from another directory.
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
     * This method builds a MoveCommand from the given input.
     */
    @Override
    public Command build(String input) {
        if (!isValidInput(input)) {
            return createInvalidCommand(CommandType.move, input);
        }

        String directoryName = getDirectoryName(input);

        Builder builder = new Builder();
        builder.setDirectory(directoryName);

        return builder.build();
    }

    private boolean isValidInput(String input) {
        return input != null && !input.isEmpty()
                && input.trim().split(Constants.REGEX_WHITESPACE).length == 2;
    }

    private String getDirectoryName(String input) {
        String directoryName = input.trim().split(Constants.REGEX_WHITESPACE)[1];
        return directoryName;
    }

}
