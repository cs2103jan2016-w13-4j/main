// @@author A0127393B

package jfdi.test.parser;

import static org.junit.Assert.assertSame;
import jfdi.logic.commands.ExitCommand;
import jfdi.logic.commands.InvalidCommand;
import jfdi.logic.interfaces.Command;
import jfdi.parser.commandparsers.AbstractCommandParser;
import jfdi.parser.commandparsers.ExitCommandParser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExitCommandParserTest {

    AbstractCommandParser parser = ExitCommandParser.getInstance();

    @Before
    public void init() {
        parser = ExitCommandParser.getInstance();
    }

    @Test
    public void testGetInstance() throws Exception {
        assertSame(parser, ExitCommandParser.getInstance());
    }

    @Test
    public void testValidCommand() {
        Command command = parser.build("exit");
        Assert.assertTrue(command instanceof ExitCommand);
        command = parser.build("Exit");
        Assert.assertTrue(command instanceof ExitCommand);
        command = parser.build("exit");
        Assert.assertTrue(command instanceof ExitCommand);
    }

    @Test
    public void testInvalidCommand() {
        // Multiple words
        Command command = parser.build("exit via the back door");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: wrong command
        command = parser.build("add 1-8");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: empty string

        // Boundary case: null
        command = parser.build(null);
        Assert.assertTrue(command instanceof InvalidCommand);

    }

}
