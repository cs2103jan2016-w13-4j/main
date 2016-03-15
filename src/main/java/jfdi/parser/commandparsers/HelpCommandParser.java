package jfdi.parser.commandparsers;

import jfdi.logic.commands.HelpCommandStub.Builder;
import jfdi.logic.interfaces.Command;

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
        Builder builder = new Builder();
        return builder.build();
    }

}
