// @@author A0127393B

package jfdi.test.parser;

import static org.junit.Assert.assertSame;
import jfdi.logic.commands.DirectoryCommand;
import jfdi.logic.commands.InvalidCommand;
import jfdi.logic.interfaces.Command;
import jfdi.parser.commandparsers.AbstractCommandParser;
import jfdi.parser.commandparsers.DirectoryCommandParser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DirectoryCommandParserTest {

    AbstractCommandParser parser = DirectoryCommandParser.getInstance();

    @Before
    public void init() {
        parser = DirectoryCommandParser.getInstance();
    }

    @Test
    public void testGetInstance() throws Exception {
        assertSame(parser, DirectoryCommandParser.getInstance());
    }

    @Test
    public void testValidCommand() {
        Command command = parser.build("directory");
        Assert.assertTrue(command instanceof DirectoryCommand);
        command = parser.build("Directory");
        Assert.assertTrue(command instanceof DirectoryCommand);
        command = parser.build("DirECToRy");
        Assert.assertTrue(command instanceof DirectoryCommand);
    }

    @Test
    public void testInvalidCommand() {
        // Multiple words
        Command command = parser.build("directory to somewhere");
        Assert.assertTrue(command instanceof InvalidCommand);
        command = parser.build("direct me");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: wrong command
        command = parser.build("add 1-8");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: empty string
        command = parser.build("");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: null
        try {
            command = parser.build(null);
        } catch (AssertionError e) {
            Assert.assertTrue(true);
        }
    }

}
