package jfdi.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants.CommandType;
import jfdi.parser.commandparsers.AddCommandParser;
import jfdi.parser.commandparsers.DeleteCommandParser;
import jfdi.parser.commandparsers.ListCommandParser;
import jfdi.parser.commandparsers.RenameCommandParser;
import jfdi.parser.commandparsers.RescheduleCommandParser;
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
    private static Collection<AliasAttributes> aliases = new ArrayList<AliasAttributes>();
    private static HashMap<CommandType, Set<String>> aliasMap = new HashMap<>();

    public static InputParser getInstance() {
        if (parserInstance == null) {
            parserInstance = new InputParser();
        }
        return parserInstance;
    }

    @Override
    public Command parse(String input) throws InvalidInputException {
        if (!isValidInput(input)) {
            throw new InvalidInputException(input);
        }

        // input is guaranteed to be at least one word long
        String firstWord = getFirstWord(input);
        CommandType commandType = getCommandType(firstWord);
        Command userCommand = getCommand(commandType, input);
        return userCommand;
    }

    @Override
    public void setAliases(Collection<AliasAttributes> aliases) {
        assert aliases != null;
        this.aliases.addAll(aliases);
        buildAliasMap();
    }

    /**
     * This method builds a mapping of CommandType to the corresponding list of
     * alias for that CommandType.
     */
    private void buildAliasMap() {
        for (AliasAttributes att : aliases) {
            CommandType commandType = getCommandType(att.getCommand());
            if (aliasMap.containsKey(commandType)) {
                Set<String> aliasList = aliasMap.get(commandType);
                if (aliasList == null) {
                    aliasList = new HashSet<>();
                    aliasList.add(att.getAlias());
                } else {
                    aliasList.add(att.getAlias());
                }
            } else {
                Set<String> aliasList = new HashSet<>();
                aliasList.add(att.getCommand());
                aliasMap.put(commandType, aliasList);
            }
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
     * This method returns the CommandType associated with the input String.
     *
     * @param input
     *            a String interpretation of a CommandType.
     * @return a CommandType enum.
     */
    private CommandType getCommandType(String input) {
        assert input.split(Constants.REGEX_WHITESPACE).length == 1;
        if (input.matches(Constants.REGEX_ADD)
                || isInAliasMap(CommandType.add, input)) {
            return CommandType.add;
        } else if (input.matches(Constants.REGEX_LIST)
                || isInAliasMap(CommandType.list, input)) {
            return CommandType.list;
        } else if (input.matches(Constants.REGEX_DELETE)
                || isInAliasMap(CommandType.delete, input)) {
            return CommandType.delete;
        } else if (input.matches(Constants.REGEX_RENAME)
                || isInAliasMap(CommandType.rename, input)) {
            return CommandType.rename;
        } else if (input.matches(Constants.REGEX_RESCHEDULE)
                || isInAliasMap(CommandType.reschedule, input)) {
            return CommandType.reschedule;
        } else {
            return CommandType.add;
        }

    }

    /**
     * This method checks to see if the given input string is present in the
     * CommandType to Alias mapping under a specific CommandType.
     *
     * @param commandType
     *            the CommandType under which the input might be an alias for
     * @param input
     *            the input string to be checked
     * @return true if the input is an alias for commandType; false otherwise.
     */
    private boolean isInAliasMap(CommandType commandType, String input) {
        if (aliasMap.containsKey(commandType)) {
            return aliasMap.get(commandType).contains(input);
        }
        return false;
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
            default:
                return AddCommandParser.getInstance().build(input);
        }
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
        return !(input.isEmpty() || input.trim().isEmpty());
    }
}
