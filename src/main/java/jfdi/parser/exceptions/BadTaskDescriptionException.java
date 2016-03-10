package jfdi.parser.exceptions;

/**
 * This exception is thrown when attempting to parse a string that does not
 * contain a valid task description. It contains a String variable which
 * corresponds to the bad input that was supposed to be parsed
 *
 * @author Leonard Hio
 *
 */
@SuppressWarnings("serial")
public class BadTaskDescriptionException extends Exception {

    private String input = null;

    public BadTaskDescriptionException(String input) {
        this.input = input;
    }

    public String getInput() {
        return this.input;
    }

}
