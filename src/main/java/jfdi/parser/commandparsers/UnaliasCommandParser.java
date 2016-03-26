package jfdi.parser.commandparsers;

import jfdi.logic.commands.UnaliasCommand.Builder;
import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants;

/**
 * The UnaliasCommandParser class takes in a user input representing an "Alias"
 * command and parses it into an UnaliasCommand object. This UnaliasCommand
 * object will then contain the alias that is to be removed from the list of
 * aliases. All unalias user commands must be in the format:
 * "{unalias identifier} {alias}"
 *
 * @author Leonard Hio
 *
 */
public class UnaliasCommandParser extends AbstractCommandParser {
    public static UnaliasCommandParser instance;

    private UnaliasCommandParser() {
    }

    public static UnaliasCommandParser getInstance() {
        if (instance == null) {
            return instance = new UnaliasCommandParser();
        }
        return instance;
    }

    @Override
    public Command build(String input) {
        assert isValidInput(input);

        Builder builder = new Builder();
        System.out.println(input);
        if (!isValidFormat(input)) {
            return createInvalidCommand(Constants.CommandType.alias, input);
        }
        String alias = null;

        alias = getAlias(input);

        builder.setAlias(alias);

        return builder.build();
    }

    /**
     * This method checks the validity of the unalias user input. The input is
     * considered valid if it begins with an 'unalias' keyword and is exactly 2
     * words long.
     *
     * @param input
     *            is the user unalias input.
     * @return true if the unalias input is valid; false otherwise.
     */
    private boolean isValidFormat(String input) {
        String[] inputAsArray = input.split(Constants.REGEX_WHITESPACE);
        return inputAsArray.length == 2
            && inputAsArray[0].matches(Constants.REGEX_UNALIAS);
    }

    /**
     * This method attempts to retrive the alias to be removed from the input,
     * making sure that the alias found is in the list of existing aliases in
     * the first place.
     *
     * @param input
     *            the input string from which the alias that is to be removed is
     *            found.
     * @return the alias to be removed.
     */
    private String getAlias(String input) {
        assert isValidFormat(input);

        String alias = getSecondWord(input);
        return alias;
    }

    private String getSecondWord(String input) {
        return input.split(Constants.REGEX_WHITESPACE)[1];
    }

}
