package jfdi.parser.commandparsers;

import java.util.ArrayList;
import java.util.List;

import jfdi.parser.Constants;
import command.Command;

public class AddCommandParser extends CommandParser {

    public static AddCommandParser instance;

    public static AddCommandParser getInstance() {
        if (instance == null) {
            return instance = new AddCommandParser();
        }

        return instance;
    }

    @Override
    public Command build(String input) {
        /*
         * Command resultCommand = new Command(); List<String> arguments =
         * getArguments(input); List<String> tags = getAndStripTags(arguments);
         * resultCommand.setTags(tags); setCommandAsAddType(resultCommand);
         * setTaskDescription(resultCommand, input); setDateTime(resultCommand,
         * input); setTags(resultCommand, input);
         */
        return new Command();
    }

    private List<String> getAndStripTags(List<String> arguments) {
        List<String> tags = new ArrayList<String>();

        // Tags are always located at the end of the user input
        int currentArgumentIndex = arguments.size() - 1;
        while (arguments.get(currentArgumentIndex)
                .matches(Constants.REGEX_TAGS)) {
            tags.add(arguments.get(currentArgumentIndex));
            removeArgumentAt(arguments, currentArgumentIndex);
        }

        return tags;
    }

    private void removeArgumentAt(List<String> arguments,
            int currentArgumentIndex) {
        arguments.remove(currentArgumentIndex);
    }

}
