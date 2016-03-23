//@@author A0121621Y
package jfdi.storage.apis;

import jfdi.storage.entities.Alias;
import jfdi.storage.exceptions.DuplicateAliasException;
import jfdi.storage.exceptions.InvalidAliasParametersException;

/**
 * This is the data transfer class of the Alias entity.
 *
 * @author Thng Kai Yuan
 *
 */
public class AliasAttributes {

    // The regex for checking if a given word is a valid command
    private static String commandRegex = null;

    private String alias;
    private String command;

    /**
     * Constructs an AliasAttributes from an Alias.
     *
     * @param alias
     *            the alias to construct from
     */
    public AliasAttributes(Alias alias) {
        assert alias != null;
        this.alias = alias.getAlias();
        this.command = alias.getCommand();
    }

    /**
     * Constructs an AliasAttributes from an alias and command string.
     *
     * @param alias
     *            the alias for this command
     * @param command
     *            the command that is to be aliased
     */
    public AliasAttributes(String alias, String command) {
        assert alias != null && command != null;
        this.alias = alias;
        this.command = command;
    }

    /**
     * Sets the regex used for checking if a given word is a valid command.
     *
     * @param regex
     *            the regex that checks if a given word is a command command
     */
    public static void setCommandRegex(String regex) {
        assert regex != null;
        commandRegex = regex;
    }

    /**
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @return the command that is aliased
     */
    public String getCommand() {
        return command;
    }

    /**
     * This method creates a new alias in the database with the given
     * attributes.
     *
     * @throws InvalidAliasParametersException
     *             if the aliasAttributes contains an invalid attribute
     * @throws DuplicateAliasException
     *             if the alias already exists in the database
     */
    public void save() throws InvalidAliasParametersException, DuplicateAliasException {
        validateAttributes();
        AliasDb.getInstance().create(this);
    }

    /**
     * Converts the current AliasAttributes to an Alias entity.
     *
     * @return the corresponding Alias
     */
    public Alias toEntity() {
        return new Alias(alias, command);
    }

    /**
     * This method checks if the current alias is valid.
     *
     * @return a boolean indicating if the current alias is valid
     */
    public boolean isValid() {
        if (alias == null || command == null || isValidCommand(alias) || !isValidCommand(command)) {
            return false;
        }

        return true;
    }

    /**
     * Validates the existing AliasAttributes.
     *
     * @throws InvalidAliasParametersException
     *             if any of the parameters are invalid
     */
    private void validateAttributes() throws InvalidAliasParametersException {
        if (!isValid()) {
            throw new InvalidAliasParametersException(this);
        }
    }

    /**
     * This method checks if a given string matches the command regex (i.e. if a
     * string is a command word).
     *
     * @param input
     *            the string to be checked
     * @return boolean indicating if the input is a command word
     */
    private boolean isValidCommand(String input) {
        assert commandRegex != null;
        return input.matches(commandRegex);
    }

}
