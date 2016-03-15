package jfdi.parser.exceptions;

/**
 * This exception is thrown when attempting to parse a string that makes
 * reference to an invalid command type. It contains a String variable which
 * corresponds to the invalid input that was supposed to be parsed.
 *
 * @author Leonard Hio
 *
 */
@SuppressWarnings("serial")
public class InvalidCommandException extends Exception {

    private String input = null;

    public InvalidCommandException(String input) {
        this.input = input;
    }

    public String getInput() {
        return this.input;
    }

}
