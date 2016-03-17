package jfdi.parser.commandparsers;

import jfdi.logic.commands.DirectoryCommandStub.Builder;
import jfdi.logic.interfaces.Command;

/**
 * This class parses the Directory command input by the user. The Directory
 * command is in the format {Directory command} i.e. a singular word.
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
        Builder builder = new Builder();
        return builder.build();
    }

}
