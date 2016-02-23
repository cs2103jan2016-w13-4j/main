package parser;

import command.Command;

/**
 * The Parser class exposes the key API for the parser component.
 *
 * @author leona_000
 *
 */
public class Parser {

    public Parser() {

    }

    public Command parse(String input) {
        Tokenizer tokenizer = new Tokenizer();
        return new Command();
    }

}
