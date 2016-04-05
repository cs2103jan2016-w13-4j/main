// @@author A0127393B

package jfdi.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import jfdi.common.utilities.JfdiLogger;
import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants.CommandType;
import jfdi.parser.commandparsers.AddCommandParser;
import jfdi.parser.commandparsers.AliasCommandParser;
import jfdi.parser.commandparsers.DeleteCommandParser;
import jfdi.parser.commandparsers.DirectoryCommandParser;
import jfdi.parser.commandparsers.ExitCommandParser;
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
    private String originalInput = "";

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
            LOGGER.throwing(SOURCECLASS, "parse", new InvalidInputException(input));
            throw new InvalidInputException(input);
        }

        originalInput = input;
        input = trimInput(input);

        // input is guaranteed to be at least one word long at this point
        String unaliasedInput = unalias(input);
        String firstWord = getFirstWord(unaliasedInput);
        CommandType commandType = ParserUtils.getCommandType(firstWord);
        Command userCommand = getCommand(commandType, unaliasedInput);
        return userCommand;
    }

    @Override
    public void setAliases(Collection<AliasAttributes> aliases) {
        assert aliases != null;
        this.aliases = new ArrayList<AliasAttributes>();
        this.aliases.addAll(aliases);
        buildAliasMap();
    }

    @Override
    public String getAllCommandRegexes() {
        return String.join("|", Constants.getCommandRegexes());
    }

    public HashMap<String, String> getAliasMap() {
        return aliasMap;
    }

    // ===================================
    // First Level of Abstraction
    // ===================================

    /**
     * This method builds a mapping of aliases to their corresponding command
     * types (represented as Strings).
     */
    private void buildAliasMap() {
        aliasMap = new HashMap<>();
        for (AliasAttributes att : aliases) {
            aliasMap.put(att.getAlias(), att.getCommand());
        }
    }

    /**
     * This method replaces an alias (if present) in the input into its
     * corresponding command type.
     *
     * @param input
     *            the String that may or may not have an alias.
     * @return the unaliased input.
     */
    private String unalias(String input) {
        assert isValidInput(input);
        String firstWord = getFirstWord(input);
        Set<String> aliasSet = aliasMap.keySet();
        for (String str : aliasSet) {
            if (firstWord.matches("^" + Pattern.quote(str))) {
                return input.replaceAll("^" + Pattern.quote(str), aliasMap.get(str));
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
            case ADD:
                return AddCommandParser.getInstance().build(input);
            case LIST:
                return ListCommandParser.getInstance().build(input);
            case DELETE:
                return DeleteCommandParser.getInstance().build(input);
            case RENAME:
                return RenameCommandParser.getInstance().build(input);
            case RESCHEDULE:
                return RescheduleCommandParser.getInstance().build(input);
            case SEARCH:
                return SearchCommandParser.getInstance().build(input);
            case MARK:
                return MarkCommandParser.getInstance().build(input);
            case UNMARK:
                return UnmarkCommandParser.getInstance().build(input);
            case ALIAS:
                return AliasCommandParser.getInstance().build(input);
            case UNALIAS:
                return UnaliasCommandParser.getInstance().build(input);
            case DIRECTORY:
                // In this case, we check to see if input is made up of just the
                // command name. If it is not, parse it as an 'Add' command
                // instead.
                return isSingleWord(input) ? DirectoryCommandParser.getInstance().build(input) : AddCommandParser
                    .getInstance().build(input);
            case MOVE:
                return MoveCommandParser.getInstance().build(input);
            case USE:
                return UseCommandParser.getInstance().build(input);
            case UNDO:
                return isSingleWord(input) ? UndoCommandParser.getInstance().build(input) : AddCommandParser
                    .getInstance().build(originalInput);
            case HELP:
                return isSingleWord(input) ? HelpCommandParser.getInstance().build(input) : AddCommandParser
                    .getInstance().build(originalInput);
            case WILDCARD:
                return isSingleWord(input) ? WildcardCommandParser.getInstance().build(input) : AddCommandParser
                    .getInstance().build(originalInput);
            case EXIT:
                return isSingleWord(input) ? ExitCommandParser.getInstance().build(input) : AddCommandParser
                    .getInstance().build(originalInput);
            default:
                return AddCommandParser.getInstance().build(input);
        }
    }

    // ===================================
    // Second Level of Abstraction
    // ===================================

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

    private boolean isSingleWord(String input) {
        return input.trim().split(Constants.REGEX_WHITESPACE).length == 1;
    }

    private String trimInput(String input) {
        return input.trim();
    }
}
