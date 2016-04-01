// @@author A0127393B

package jfdi.test.parser;

import static org.junit.Assert.assertSame;
import jfdi.logic.commands.InvalidCommand;
import jfdi.logic.commands.RenameTaskCommand;
import jfdi.logic.interfaces.Command;
import jfdi.parser.commandparsers.AbstractCommandParser;
import jfdi.parser.commandparsers.RenameCommandParser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RenameCommandParserTest {

    AbstractCommandParser parser = RenameCommandParser.getInstance();

    @Before
    public void init() {
        parser = RenameCommandParser.getInstance();
    }

    @Test
    public void testGetInstance() throws Exception {
        assertSame(parser, RenameCommandParser.getInstance());
    }

    @Test
    public void testValidCommand() {
        Command command = parser.build("rename 1 happy");
        Assert.assertTrue(command instanceof RenameTaskCommand);
        RenameTaskCommand renameCommand = (RenameTaskCommand) command;
        assertSameFields(renameCommand, 1, "happy");

        command = parser.build("rename 123 12345");
        Assert.assertTrue(command instanceof RenameTaskCommand);
        renameCommand = (RenameTaskCommand) command;
        assertSameFields(renameCommand, 123, "12345");

        command = parser.build("rename 100232132 2 or more words");
        Assert.assertTrue(command instanceof RenameTaskCommand);
        renameCommand = (RenameTaskCommand) command;
        assertSameFields(renameCommand, 100232132, "2 or more words");
    }

    @Test
    public void testInvalidCommand() {

        // Boundary case: wrong command
        Command command = parser.build("delete 1-5");
        Assert.assertTrue(command instanceof InvalidCommand);
        command = parser.build("add hello");
        Assert.assertTrue(command instanceof InvalidCommand);
        command = parser.build("add hello 3words");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: insufficient arguments
        command = parser.build("rename");
        Assert.assertTrue(command instanceof InvalidCommand);
        command = parser.build("rename oneword");
        Assert.assertTrue(command instanceof InvalidCommand);
        command = parser.build("rename 1");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: empty string
        command = parser.build("");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: null
        command = parser.build(null);
        Assert.assertTrue(command instanceof InvalidCommand);
    }

    private void assertSameFields(RenameTaskCommand renameTaskCommand, int taskId, String desc) {
        Assert.assertEquals(renameTaskCommand.getScreenId(), taskId);
        Assert.assertEquals(renameTaskCommand.getDescription(), desc);
    }

}
