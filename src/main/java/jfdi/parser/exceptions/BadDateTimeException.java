package jfdi.parser.exceptions;

/**
 * This exception is thrown when attempting to parse a string that does not
 * match with the supported formats of date and time. It contains a String
 * variable which corresponds to the bad input that was supposed to be parsed
 *
 * @author Leonard Hio
 *
 */
@SuppressWarnings("serial")
public class BadDateTimeException extends Exception {

    private String input = null;

    public BadDateTimeException(String input) {
        this.input = input;
    }

    public String getInput() {
        return this.input;
    }

}
