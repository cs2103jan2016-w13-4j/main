// @@author A0127393B

package jfdi.test.parser;

import static org.junit.Assert.assertSame;
import jfdi.logic.commands.InvalidCommand;
import jfdi.logic.commands.UseDirectoryCommand;
import jfdi.logic.interfaces.Command;
import jfdi.parser.commandparsers.AbstractCommandParser;
import jfdi.parser.commandparsers.UseCommandParser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UseCommandParserTest {

    AbstractCommandParser parser = UseCommandParser.getInstance();

    @Before
    public void init() {
        parser = UseCommandParser.getInstance();
    }

    @Test
    public void testGetInstance() throws Exception {
        assertSame(parser, UseCommandParser.getInstance());
    }

    @Test
    public void testValidCommand() {
        Command command = parser.build("use happy");
        Assert.assertTrue(command instanceof UseDirectoryCommand);
        command = parser.build("Use C://leonard hio/dir");
        Assert.assertTrue(command instanceof UseDirectoryCommand);
        command = parser.build("Use 12343 45451 15454 56 6 6 ");
        Assert.assertTrue(command instanceof UseDirectoryCommand);
    }

    @Test
    public void testInvalidCommand() {
        // Multiple words
        Command command = parser.build("surprise me");
        Assert.assertTrue(command instanceof InvalidCommand);
        command = parser.build("surprise! me too");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: wrong command
        command = parser.build("add");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: empty string
        command = parser.build("");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: null
        command = parser.build(null);
        Assert.assertTrue(command instanceof InvalidCommand);
    }

}
