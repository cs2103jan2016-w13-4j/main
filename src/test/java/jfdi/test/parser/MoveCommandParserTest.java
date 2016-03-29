// @@author A0127393B

package jfdi.test.parser;

import static org.junit.Assert.assertSame;
import jfdi.logic.commands.InvalidCommand;
import jfdi.logic.commands.MoveDirectoryCommand;
import jfdi.logic.interfaces.Command;
import jfdi.parser.commandparsers.AbstractCommandParser;
import jfdi.parser.commandparsers.MoveCommandParser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MoveCommandParserTest {

    AbstractCommandParser parser = MoveCommandParser.getInstance();

    @Before
    public void init() {
        parser = MoveCommandParser.getInstance();
    }

    @Test
    public void testGetInstance() throws Exception {
        assertSame(parser, MoveCommandParser.getInstance());
    }

    @Test
    public void testValidCommand() {
        Command command = parser.build("move happy");
        Assert.assertTrue(command instanceof MoveDirectoryCommand);
        MoveDirectoryCommand moveCommand = (MoveDirectoryCommand) command;
        Assert.assertEquals("happy", moveCommand.getNewDirectory());

        command = parser.build("Move C://leonard hio/dir");
        Assert.assertTrue(command instanceof MoveDirectoryCommand);
        moveCommand = (MoveDirectoryCommand) command;
        Assert.assertEquals("C://leonard hio/dir", moveCommand.getNewDirectory());

        command = parser.build("Move 12343 45451 15454 56 6 6 ");
        Assert.assertTrue(command instanceof MoveDirectoryCommand);
        moveCommand = (MoveDirectoryCommand) command;
        Assert.assertEquals("12343 45451 15454 56 6 6 ", moveCommand.getNewDirectory());
    }

    @Test
    public void testInvalidCommand() {

        // Boundary case: wrong command
        Command command = parser.build("surprise me");
        Assert.assertTrue(command instanceof InvalidCommand);
        command = parser.build("add hello");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: no arguments
        command = parser.build("Move");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: empty string
        command = parser.build("");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: null
        command = parser.build(null);
        Assert.assertTrue(command instanceof InvalidCommand);
    }

}
