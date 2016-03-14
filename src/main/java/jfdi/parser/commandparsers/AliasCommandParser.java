package jfdi.parser.commandparsers;

import java.util.ArrayList;
import java.util.Collection;

import jfdi.logic.commands.AliasCommandStub.Builder;
import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants;
import jfdi.parser.Constants.CommandType;
import jfdi.parser.exceptions.InvalidCommandException;
import jfdi.parser.exceptions.UsedAliasException;
import jfdi.storage.apis.AliasAttributes;

/**
 * The AliasCommandParser class takes in a user input representing an "Alias"
 * command and parses it into an AliasCommand object. This AliasCommand object
 * will then contain the associated mapping of alias to command type. All alias
 * user commands must be in the format: "alias {command type} {alias}"
 *
 * @author Leonard Hio
 *
 */
public class AliasCommandParser extends AbstractCommandParser {
    public static AliasCommandParser instance;
    private static Collection<AliasAttributes> aliasAttributesList = new ArrayList<AliasAttributes>();

    private AliasCommandParser(Collection<AliasAttributes> aliasAttributesList) {
        this.aliasAttributesList = aliasAttributesList;
    }

    public static AliasCommandParser getInstance(
            Collection<AliasAttributes> aliasAttributesList) {
        if (instance == null) {
            return instance = new AliasCommandParser(aliasAttributesList);
        }
        instance.aliasAttributesList = aliasAttributesList;
        return instance;
    }

    @Override
    public Command build(String input) {
        Builder builder = new Builder();
        if (!isValidFormat(input)) {
            return createInvalidCommand(Constants.CommandType.alias, input);
        }
        String command = null;
        String alias = null;

        try {
            command = getCommand(input);
        } catch (InvalidCommandException e) {
            return createInvalidCommand(Constants.CommandType.alias, input);
        }

        try {
            alias = getAlias(input);
        } catch (UsedAliasException e) {
            return createInvalidCommand(Constants.CommandType.alias, input);
        }

        builder.setCommand(command);
        builder.setAlias(alias);

        return builder.build();
    }

    private boolean isValidFormat(String input) {
        return input.split(Constants.REGEX_WHITESPACE).length == 3;
    }

    private String getCommand(String input) throws InvalidCommandException {
        String secondWord = getSecondWord(input);
        CommandType commandType = getCommandType(secondWord);

        return commandType.toString();
    }

    private String getAlias(String input) throws UsedAliasException {
        String alias = getThirdWord(input);
        for (AliasAttributes att : aliasAttributesList) {
            if (alias.equals(att)) {
                throw new UsedAliasException(input);
            }
        }
        return alias;
    }

    private String getSecondWord(String input) {
        return input.split(Constants.REGEX_WHITESPACE)[1];
    }

    private String getThirdWord(String input) {
        return input.split(Constants.REGEX_WHITESPACE)[2];
    }

    /**
     * This method returns the CommandType associated with the input String.
     *
     * @param input
     *            a String interpretation of a CommandType.
     * @return a CommandType enum. If no matches were found, return invalid.
     */
    protected static CommandType getCommandType(String input)
            throws InvalidCommandException {
        assert input.split(Constants.REGEX_WHITESPACE).length == 1;
        if (input.matches(Constants.REGEX_ADD)) {
            return CommandType.add;
        } else if (input.matches(Constants.REGEX_LIST)) {
            return CommandType.list;
        } else if (input.matches(Constants.REGEX_DELETE)) {
            return CommandType.delete;
        } else if (input.matches(Constants.REGEX_RENAME)) {
            return CommandType.rename;
        } else if (input.matches(Constants.REGEX_RESCHEDULE)) {
            return CommandType.reschedule;
        } else if (input.matches(Constants.REGEX_SEARCH)) {
            return CommandType.search;
        } else if (input.matches(Constants.REGEX_MARK)) {
            return CommandType.mark;
        } else if (input.matches(Constants.REGEX_UNMARK)) {
            return CommandType.unmark;
        } else if (input.matches(Constants.REGEX_ALIAS)) {
            return CommandType.alias;
        } else if (input.matches(Constants.REGEX_DIRECTORY)) {
            return CommandType.directory;
        } else if (input.matches(Constants.REGEX_UNDO)) {
            return CommandType.undo;
        } else if (input.matches(Constants.REGEX_HELP)) {
            return CommandType.help;
        } else {
            throw new InvalidCommandException(input);
        }

    }

}
