// @@author A0127393B

package jfdi.parser.exceptions;

/**
 * This exception is thrown when attempting to parse a string that is invalid in
 * some way. It contains a String variable which corresponds to the invalid
 * input that was supposed to be parsed.
 *
 * @author Leonard Hio
 *
 */
@SuppressWarnings("serial")
public class InvalidInputException extends Exception {

    private String input = null;

    public InvalidInputException(String input) {
        this.input = input;
    }

    public String getInput() {
        return this.input;
    }

}
