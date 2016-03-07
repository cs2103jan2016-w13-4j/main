package jfdi.parser.exceptions;

@SuppressWarnings("serial")
public class NoTaskIdFoundException extends Exception {
    private String input = null;

    public NoTaskIdFoundException(String input) {
        this.input = input;
    }

    public String getInput() {
        return this.input;
    }
}
