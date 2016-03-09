package jfdi.storage.exceptions;

import jfdi.storage.apis.AliasAttributes;

@SuppressWarnings("serial")
public class DuplicateAliasException extends Exception {

    private String alias = null;
    private String command = null;

    public DuplicateAliasException(AliasAttributes aliasAttributes) {
        this.alias = aliasAttributes.getAlias();
        this.command = aliasAttributes.getCommand();
    }

    public String getAlias() {
        return alias;
    }

    public String getCommand() {
        return command;
    }

}
