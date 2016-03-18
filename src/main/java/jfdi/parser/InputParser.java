package jfdi.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;

import jfdi.common.utilities.JfdiLogger;
import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants.CommandType;
import jfdi.parser.commandparsers.AddCommandParser;
import jfdi.parser.commandparsers.AliasCommandParser;
import jfdi.parser.commandparsers.DeleteCommandParser;
import jfdi.parser.commandparsers.DirectoryCommandParser;
import jfdi.parser.commandparsers.HelpCommandParser;
import jfdi.parser.commandparsers.ListCommandParser;
import jfdi.parser.commandparsers.MarkCommandParser;
import jfdi.parser.commandparsers.MoveCommandParser;
import jfdi.parser.commandparsers.RenameCommandParser;
import jfdi.parser.commandparsers.RescheduleCommandParser;
import jfdi.parser.commandparsers.SearchCommandParser;
import jfdi.parser.commandparsers.UnaliasCommandParser;
import jfdi.parser.commandparsers.UndoCommandParser;
import jfdi.parser.commandparsers.UnmarkCommandParser;
import jfdi.parser.commandparsers.UseCommandParser;
import jfdi.parser.commandparsers.WildcardCommandParser;
import jfdi.parser.exceptions.InvalidInputException;
import jfdi.storage.apis.AliasAttributes;

/**
 * The InputParser class is used to parse a String input into its associated
 * Command object. This class should be used to interface with the Logic
 * component via the parse(String) method.
 *
 * @author Leonard Hio
 *
 */
public class InputParser implements IParser {
    private static InputParser parserInstance;
    private static final Logger LOGGER = JfdiLogger.getLogger();
    private static final String SOURCECLASS = InputParser.class.getName();
    private Collection<AliasAttributes> aliases = new ArrayList<AliasAttributes>();
    private HashMap<String, String> aliasMap = new HashMap<>();

    public static InputParser getInstance() {
        LOGGER.entering(SOURCECLASS, "getInstance");
        if (parserInstance == null) {
            parserInstance = new InputParser();
        }
        LOGGER.exiting(SOURCECLASS, "getInstance");
        return parserInstance;
    }

    @Override
    public Command parse(String input) throws InvalidInputException {
        if (!isValidInput(input)) {
            LOGGER.throwing(SOURCECLASS, "parse", new InvalidInputException(
                input));
            throw new InvalidInputException(input);
        }
        input = trimInput(input);
        // input is guaranteed to be at least one word long
        String unaliasedInput = unalias(input);
        String firstWord = getFirstWord(unaliasedInput);
        CommandType commandType = ParserUtils.getCommandType(firstWord);
        Command userCommand = getCommand(commandType, input);
        return userCommand;
    }

    @Override
    public void setAliases(Collection<AliasAttributes> aliases) {
        assert aliases != null;
        this.aliases.addAll(aliases);
        buildAliasMap();
    }

    @Override
    public String getAllCommandRegexes() {
        return String.join("|", Constants.COMMAND_REGEXES);
    }

    /**
     * This method builds a mapping of aliases to their corresponding command
     * types (represented as strings).
     */
    private void buildAliasMap() {
        aliasMap = new HashMap<>();
        for (AliasAttributes att : aliases) {
            aliasMap.put(att.getAlias(), att.getCommand());
        }
    }

    /**
     * This method returns the first word of the input String.
     *
     * @param input
     *            a String from which the first word is to be returned.
     * @return the first word of the input String.
     */
    private String getFirstWord(String input) {
        assert isValidInput(input);
        return input.trim().split(Constants.REGEX_WHITESPACE)[0];
    }

    /**
     * This method replaces an alias (if present) in the input into its
     * corresponding command type.
     *
     * @param input
     *            the string that may or may not have an alias.
     * @return the unaliased input.
     */
    private String unalias(String input) {
        assert isValidInput(input);

        Set<String> aliasSet = aliasMap.keySet();
        for (String str : aliasSet) {
            if (input.matches("$" + str)) {
                input.replaceAll("$" + str, aliasMap.get(str));
                break;
            }
        }
        return input;
    }

    /**
     * This method gets the Command object associated with the user's input.
     *
     * @param commandType
     *            the CommandType of the user's input.
     * @param input
     *            the user's input itself.
     * @return a Command object that was built from the user's input.
     */
    private Command getCommand(CommandType commandType, String input) {
        assert commandType != null && isValidInput(input);

        switch (commandType) {
            case add:
                return AddCommandParser.getInstance().build(input);
            case list:
                return ListCommandParser.getInstance().build(input);
            case delete:
                return DeleteCommandParser.getInstance().build(input);
            case rename:
                return RenameCommandParser.getInstance().build(input);
            case reschedule:
                return RescheduleCommandParser.getInstance().build(input);
            case search:
                return SearchCommandParser.getInstance().build(input);
            case mark:
                return MarkCommandParser.getInstance().build(input);
            case unmark:
                return UnmarkCommandParser.getInstance().build(input);
            case alias:
                return AliasCommandParser.getInstance().build(input);
            case unalias:
                return UnaliasCommandParser.getInstance().build(input);
            case directory:
                // In this case, we check to see if input is made up of just the
                // command name. If it is not, parse it as an 'Add' command
                // instead.
                return isSingleWord(input) ? DirectoryCommandParser
                    .getInstance().build(input) : AddCommandParser
                    .getInstance().build(input);
            case move:
                return MoveCommandParser.getInstance().build(input);
            case use:
                return UseCommandParser.getInstance().build(input);
            case undo:
                return isSingleWord(input) ? UndoCommandParser.getInstance()
                    .build(input) : AddCommandParser.getInstance().build(input);
            case help:
                return isSingleWord(input) ? HelpCommandParser.getInstance()
                    .build(input) : AddCommandParser.getInstance().build(input);
            case wildcard:
                return isSingleWord(input) ? WildcardCommandParser
                    .getInstance().build(input) : AddCommandParser
                    .getInstance().build(input);
            default:
                return AddCommandParser.getInstance().build(input);
        }
    }

    private boolean isSingleWord(String input) {
        return input.trim().split(Constants.REGEX_WHITESPACE).length == 1;
    }

    /**
     * This method checks if the given input is valid. A valid input is one that
     * is (1) not empty, and (2) not made of whitespaces only.
     *
     * @param input
     *            the input which validity is to be checked
     * @return true if the input is valid; false otherwise
     */
    private boolean isValidInput(String input) {
        return input != null && !input.isEmpty() && !input.trim().isEmpty();
    }

    private String trimInput(String input) {
        return input.trim();
    }
}
