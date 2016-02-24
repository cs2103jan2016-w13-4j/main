package parser;

import command.Command;

public abstract class CommandBuilder {
    protected String userInput;

    public CommandBuilder(String input) {
        userInput = input;
    }

    public abstract Command build();
}
