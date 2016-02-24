package jfdi.parser.commandparsers;

import java.util.Arrays;
import java.util.List;

import jfdi.parser.Constants;

import command.Command;

public abstract class CommandParser {
    protected String userInput;

    public abstract Command build(String input);

    protected List<String> getArguments(String input) {
        return Arrays.asList(input.trim().split(Constants.REGEX_WHITESPACE));
    }
}
