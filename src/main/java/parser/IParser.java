package parser;

import command.Command;

/**
 * This is the interface for the Parser component. All other components should access the Parser component
 * using only the methods in this interface.
 * 
 * The Parser component is simple: it takes in a String as input, and returns a Command object. 
 * 
 * @author leona_000
 *
 */
public interface IParser {

    /**
     * This method takes in a String as input. In most cases the input should be what the user inputs
     * into the UI. The Logic component will then pass this String input into the parser via this method
     * to get the Command object.
     * @param userinput
     *          the String input to be parsed. In most cases, this input should be the input by the user.
     * @return a Command object.
     */
    public Command parse(String userInput);
}
