package parser;

import java.util.LinkedList;

import command.Command;
/**
 * The Parser class exposes the key API for the parser component.
 * @author leona_000
 *
 */
public class Parser {
    public boolean test() {
        return true;
    }

    public Command parse(String inputString) {
        LinkedList<Token> tokenizedInput = Tokenizer.tokenize(inputString);
        Command command = ParserGrammar.getCommand(tokenizedInput);
        return command;
    }
}
