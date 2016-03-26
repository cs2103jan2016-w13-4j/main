package jfdi.parser.commandparsers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import jfdi.logic.commands.SearchCommand;
import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants;
import jfdi.parser.Constants.CommandType;
import jfdi.parser.exceptions.InvalidInputException;

/**
 * The SearchCommandParser class is used to parse a user input that represents a
 * search command. The search input format is: {search identifier} {keywords}.
 *
 * @author Leonard Hio
 *
 */
public class SearchCommandParser extends AbstractCommandParser {
    private static SearchCommandParser instance;

    private SearchCommandParser() {

    }

    public static SearchCommandParser getInstance() {
        return instance == null ? instance = new SearchCommandParser()
            : instance;
    }

    @Override
    /**
     * This method parses the user input (representing a search command) and
     * builds the SearchCommand object.
     * @param input
     *            the user input String
     * @return the SearchCommand object encapsulating the keywords of the search command.
     */
    public Command build(String input) {
        assert isValidInput(input);

        SearchCommand.Builder builder = new SearchCommand.Builder();
        Collection<String> keywords = new HashSet<String>();
        try {
            keywords = getKeywords(input);
        } catch (InvalidInputException e) {
            return createInvalidCommand(CommandType.search, input);
        }
        builder.addKeywords(keywords);
        return builder.build();
    }

    /**
     * This method returns a list of keywords that can be extracted from the
     * given input.
     *
     * @param input
     *            the input string.
     * @return a Collection of keywords found.
     * @throws InvalidInputException
     *             if no keywords are specified in the input, or if the input is
     *             empty to begin with.
     */
    private Collection<String> getKeywords(String input)
        throws InvalidInputException {
        if (!isValidInput(input)) {
            throw new InvalidInputException(input);
        }
        String[] keywords = input.split(Constants.REGEX_WHITESPACE);
        if (keywords.length <= 1) {
            throw new InvalidInputException(input);
        }
        List<String> keywordsAsList = new ArrayList<>();
        for (int i = 1; i < keywords.length; i++) {
            keywordsAsList.add(keywords[i]);
        }

        return keywordsAsList;
    }

}
