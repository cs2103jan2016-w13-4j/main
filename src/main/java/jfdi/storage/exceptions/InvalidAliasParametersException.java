//@@author A0121621Y

package jfdi.storage.exceptions;

import jfdi.storage.apis.AliasAttributes;

@SuppressWarnings("serial")
public class InvalidAliasParametersException extends Exception {

    private String alias = null;
    private String command = null;

    public InvalidAliasParametersException(AliasAttributes aliasAttributes) {
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
