package jfdi.parser.commandparsers;

import java.util.ArrayList;
import java.util.Collection;

import jfdi.logic.commands.UnaliasCommandStub.Builder;
import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants;
import jfdi.parser.exceptions.InvalidAliasException;
import jfdi.storage.apis.AliasAttributes;

/**
 * The UnaliasCommandParser class takes in a user input representing an "Alias"
 * command and parses it into an UnaliasCommand object. This UnaliasCommand
 * object will then contain the alias that is to be removed from the list of
 * aliases. All unalias user commands must be in the format: "unalias {alias}"
 *
 * @author Leonard Hio
 *
 */
public class UnaliasCommandParser extends AbstractCommandParser {
    public static UnaliasCommandParser instance;
    private Collection<AliasAttributes> aliasAttributesList = new ArrayList<AliasAttributes>();

    private UnaliasCommandParser(Collection<AliasAttributes> aliasAttributesList) {
        this.aliasAttributesList = aliasAttributesList;
    }

    public static UnaliasCommandParser getInstance(
            Collection<AliasAttributes> aliasAttributesList) {
        if (instance == null) {
            return instance = new UnaliasCommandParser(aliasAttributesList);
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
        String alias = null;

        try {
            alias = getAlias(input);
        } catch (InvalidAliasException e) {
            return createInvalidCommand(Constants.CommandType.alias, input);
        }

        builder.setAlias(alias);

        return builder.build();
    }

    /**
     * This method checks the validity of the unalias user input. The input is
     * considered valid if it begins with an 'unalias' keyword and is exactly 2
     * words long.
     *
     * @param input
     *            is the user unalias input.
     * @return true if the unalias input is valid; false otherwise.
     */
    private boolean isValidFormat(String input) {
        String[] inputAsArray = input.split(Constants.REGEX_WHITESPACE);
        return inputAsArray.length == 2
                && inputAsArray[0].equals(Constants.REGEX_UNALIAS);
    }

    /**
     * This method attempts to retrive the alias to be removed from the input,
     * making sure that the alias found is in the list of existing aliases in
     * the first place.
     *
     * @param input
     *            the input string from which the alias that is to be removed is
     *            found.
     * @return the alias to be removed.
     * @throws InvalidAliasException
     *             if the alias found is not in the list of aliases.
     */
    private String getAlias(String input) throws InvalidAliasException {
        String alias = getSecondWord(input);
        for (AliasAttributes att : aliasAttributesList) {
            if (alias.equals(att.getAlias())) {
                return alias;
            }
        }
        throw new InvalidAliasException(input);
    }

    private String getSecondWord(String input) {
        return input.split(Constants.REGEX_WHITESPACE)[1];
    }

}
