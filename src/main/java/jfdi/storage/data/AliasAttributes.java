package jfdi.storage.data;

import jfdi.storage.exceptions.DuplicateAliasException;
import jfdi.storage.exceptions.InvalidAliasParametersException;

public class AliasAttributes {

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

    public String getAlias() {
        return alias;
    }

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
        AliasDb.create(this);
    }

    public Alias toEntity() {
        return new Alias(alias, command);
    }

    private void validateAttributes() throws InvalidAliasParametersException, DuplicateAliasException {
        if (alias == null || command == null) {
            throw new InvalidAliasParametersException(this);
        }
    }

}
