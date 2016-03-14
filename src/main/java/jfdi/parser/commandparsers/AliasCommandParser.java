package jfdi.parser.commandparsers;

import java.util.ArrayList;
import java.util.Collection;

import jfdi.logic.commands.AliasCommandStub.Builder;
import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants;
import jfdi.parser.Constants.CommandType;
import jfdi.parser.ParserUtils;
import jfdi.parser.exceptions.InvalidAliasException;
import jfdi.parser.exceptions.InvalidCommandException;
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
    private Collection<AliasAttributes> aliasAttributesList = new ArrayList<AliasAttributes>();

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
        } catch (InvalidAliasException e) {
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
        CommandType commandType = ParserUtils.getCommandType(secondWord);
        if (commandType == CommandType.invalid) {
            throw new InvalidCommandException(commandType.toString());
        }
        return commandType.toString();
    }

    /**
     * This method extracts the alias from the user input.
     *
     * @param input
     *            the user input representing an alias command.
     * @return the alias.
     * @throws InvalidAliasException
     *             if the alias is already in use.
     */
    private String getAlias(String input) throws InvalidAliasException {
        String alias = getThirdWord(input);
        for (AliasAttributes att : aliasAttributesList) {
            if (alias.equals(att.getAlias())) {
                throw new InvalidAliasException(input);
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

}
