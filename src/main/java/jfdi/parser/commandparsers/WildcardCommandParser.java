package jfdi.parser.commandparsers;

import jfdi.logic.commands.WildcardCommand.Builder;
import jfdi.logic.interfaces.Command;

/**
 * This class parses the Wildcard command input by the user. The Wildcard
 * command is in the format {Wildcard identifier} i.e. a singular word.
 *
 * @author Leonard Hio
 *
 */
public class WildcardCommandParser extends AbstractCommandParser {
    public static WildcardCommandParser instance;

    private WildcardCommandParser() {
    }

    public static WildcardCommandParser getInstance() {
        if (instance == null) {
            return instance = new WildcardCommandParser();
        }
        return instance;
    }

    @Override
    public Command build(String input) {
        Builder builder = new Builder();
        return builder.build();
    }

}
