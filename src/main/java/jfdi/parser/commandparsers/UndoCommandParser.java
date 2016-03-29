// @@author A0127393B

package jfdi.parser.commandparsers;

import jfdi.logic.commands.UndoCommand.Builder;
import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants;
import jfdi.parser.Constants.CommandType;

/**
 * This class parses the Undo command input by the user. The Undo command is in
 * the format {Undo identifier} i.e. a singular word.
 *
 * @author Leonard Hio
 *
 */
public class UndoCommandParser extends AbstractCommandParser {
    public static UndoCommandParser instance;

    private UndoCommandParser() {
    }

    public static UndoCommandParser getInstance() {
        if (instance == null) {
            return instance = new UndoCommandParser();
        }
        return instance;
    }

    @Override
    public Command build(String input) {
        if (!isValidUndoCommand(input)) {
            return createInvalidCommand(CommandType.wildcard, input);
        }
        Builder builder = new Builder();
        return builder.build();
    }

    private boolean isValidUndoCommand(String input) {
        assert input != null;
        return isSingleWord(input) && input.matches(Constants.REGEX_UNDO);
    }

}
