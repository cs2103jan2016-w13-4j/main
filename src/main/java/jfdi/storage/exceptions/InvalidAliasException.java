//@@author A0121621Y

package jfdi.storage.exceptions;

@SuppressWarnings("serial")
public class InvalidAliasException extends Exception {

    private String alias = null;

    public InvalidAliasException(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

}
