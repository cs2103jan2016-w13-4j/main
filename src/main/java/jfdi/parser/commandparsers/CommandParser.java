package jfdi.parser.commandparsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jfdi.logic.interfaces.AbstractCommand;
import jfdi.parser.Constants;

public abstract class CommandParser {
    protected String userInput;

    public abstract AbstractCommand build(String input);

    protected List<String> getArguments(String input) {
        List<String> arguments = new ArrayList<String>();
        arguments.addAll(Arrays.asList(input.trim().split(
                Constants.REGEX_WHITESPACE)));
        return arguments;
    }
}
