// @@author A0127393B

package jfdi.parser.exceptions;

/**
 * This exception is thrown when attempting to assign a command to an alias that
 * has already been used for another command. It contains a String variable
 * which corresponds to the invalid alias.
 *
 * @author Leonard Hio
 *
 */
@SuppressWarnings("serial")
public class InvalidAliasException extends Exception {

    private String alias = null;

    public InvalidAliasException(String alias) {
        this.alias = alias;
    }

    public String getInput() {
        return this.alias;
    }

}
