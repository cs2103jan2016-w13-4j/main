package jfdi.parser.commandparsers;

import jfdi.logic.commands.ExitCommand.Builder;
import jfdi.logic.interfaces.Command;

/**
 * This class parses the Wildcard command input by the user. The Wildcard
 * command is in the format {Wildcard identifier} i.e. a singular word.
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
        Builder builder = new Builder();
        return builder.build();
    }

}
