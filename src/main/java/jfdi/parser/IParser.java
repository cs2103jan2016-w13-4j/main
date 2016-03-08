package jfdi.parser;

import jfdi.logic.interfaces.Command;
import jfdi.parser.exceptions.InvalidInputException;

/**
 * This is the interface for the Parser component. All other components should
 * access the Parser component using only the methods in this interface. The
 * Parser component is simple: it takes in a String as input, and returns a
 * Command object.
 *
 * @author leona_000
 */
public interface IParser {

    /**
     * This method takes in a String as input and returns its associated Command
     * object. In most cases, the input should be what the user inputs into the
     * UI, and should be passed into the Parser via the Logic component.
     *
     * @param userinput
     *            the String input to be parsed. In most cases, this input
     *            should be the input by the user.
     * @return a Command object.
     * @throws InvalidInputException
     *             if the userInput is determined to be invalid.
     */
    Command parse(String userInput) throws InvalidInputException;
}
