package jfdi.storage.apis;

import jfdi.storage.entities.Alias;
import jfdi.storage.exceptions.DuplicateAliasException;
import jfdi.storage.exceptions.InvalidAliasParametersException;

public class AliasAttributes {

    // The regex for checking if a given word is a valid command
    private static String commandRegex = null;

    private String alias;
    private String command;

    public AliasAttributes(Alias alias) {
        this.alias = alias.getAlias();
        this.command = alias.getCommand();
    }

    public AliasAttributes(String alias, String command) {
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
        commandRegex = regex;
    }

    public String getAlias() {
        return alias;
    }

    public String getCommand() {
        return command;
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

    /**
     * This method creates a new alias in the database with the given
     * attributes.
     *
     * @throws InvalidAliasParametersException
     *             if the aliasAttributes contains an invalid attribute
     * @throws DuplicateAliasException
     *             if the alias already exists in the database
     */
    public void save() throws InvalidAliasParametersException,
        DuplicateAliasException {
        validateAttributes();
        AliasDb.getInstance().create(this);
    }

    public Alias toEntity() {
        return new Alias(alias, command);
    }

    private void validateAttributes() throws InvalidAliasParametersException,
        DuplicateAliasException {
        if (alias == null || command == null || !isValidCommand(command)) {
            throw new InvalidAliasParametersException(this);
        }
    }

}
