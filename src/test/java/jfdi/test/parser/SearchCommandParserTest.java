// @@author A0127393B

package jfdi.test.parser;

import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import jfdi.logic.commands.InvalidCommand;
import jfdi.logic.commands.SearchCommand;
import jfdi.logic.interfaces.Command;
import jfdi.parser.commandparsers.AbstractCommandParser;
import jfdi.parser.commandparsers.SearchCommandParser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SearchCommandParserTest {

    AbstractCommandParser parser = SearchCommandParser.getInstance();

    @Before
    public void init() {
        parser = SearchCommandParser.getInstance();
    }

    @Test
    public void testGetInstance() throws Exception {
        assertSame(parser, SearchCommandParser.getInstance());
    }

    @Test
    public void testValidCommand() {
        Command command = parser.build("search happy");
        Assert.assertTrue(command instanceof SearchCommand);
        SearchCommand searchCommand = (SearchCommand) command;
        assertSameKeywords(searchCommand.getKeywords(), "happy");

        command = parser.build("search i am am happy");
        Assert.assertTrue(command instanceof SearchCommand);
        searchCommand = (SearchCommand) command;
        assertSameKeywords(searchCommand.getKeywords(), "i", "am", "am", "happy");

        command = parser.build("search CS2106 Project Work 23/13 ");
        Assert.assertTrue(command instanceof SearchCommand);
        searchCommand = (SearchCommand) command;
        assertSameKeywords(searchCommand.getKeywords(), "CS2106", "Project", "Work", "23/13");
    }

    @Test
    public void testInvalidCommand() {

        // Boundary case: wrong command
        Command command = parser.build("delete 1-5");
        Assert.assertTrue(command instanceof InvalidCommand);
        command = parser.build("add hello");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: no arguments
        command = parser.build("search");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: empty string
        command = parser.build("");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: null
        command = parser.build(null);
        Assert.assertTrue(command instanceof InvalidCommand);
    }

    private void assertSameKeywords(HashSet<String> searchCommand, String... expected) {
        List<String> expectedKeywords = Arrays.asList(expected);
        for (String keyword : searchCommand) {
            Assert.assertTrue(expectedKeywords.contains(keyword));
        }

        for (String keyword : expectedKeywords) {
            Assert.assertTrue(searchCommand.contains(keyword));
        }

    }

}
