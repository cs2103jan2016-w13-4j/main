// @@author A0127393B

package jfdi.test.parser;

import static org.junit.Assert.assertSame;
import jfdi.logic.commands.HelpCommand;
import jfdi.logic.commands.InvalidCommand;
import jfdi.logic.interfaces.Command;
import jfdi.parser.commandparsers.AbstractCommandParser;
import jfdi.parser.commandparsers.HelpCommandParser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HelpCommandParserTest {

    AbstractCommandParser parser = HelpCommandParser.getInstance();

    @Before
    public void init() {
        parser = HelpCommandParser.getInstance();
    }

    @Test
    public void testGetInstance() throws Exception {
        assertSame(parser, HelpCommandParser.getInstance());
    }

    @Test
    public void testValidCommand() {
        Command command = parser.build("help");
        Assert.assertTrue(command instanceof HelpCommand);
        command = parser.build("Help");
        Assert.assertTrue(command instanceof HelpCommand);
        command = parser.build("hELP");
        Assert.assertTrue(command instanceof HelpCommand);
    }

    @Test
    public void testInvalidCommand() {
        // Multiple words
        Command command = parser.build("help me");
        Assert.assertTrue(command instanceof InvalidCommand);
        command = parser.build("help 1-5");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: wrong command
        command = parser.build("list incomplete");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: empty string
        command = parser.build("");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: null
        command = parser.build(null);
        Assert.assertTrue(command instanceof InvalidCommand);
    }

}
