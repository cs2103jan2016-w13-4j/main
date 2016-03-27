// @@author A0127393B

package jfdi.test.parser;

import static org.junit.Assert.assertSame;
import jfdi.logic.commands.DeleteTaskCommand;
import jfdi.logic.commands.InvalidCommand;
import jfdi.logic.interfaces.Command;
import jfdi.parser.commandparsers.AbstractCommandParser;
import jfdi.parser.commandparsers.DeleteCommandParser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.emory.mathcs.backport.java.util.Arrays;

public class DeleteCommandParserTest {

    AbstractCommandParser parser = DeleteCommandParser.getInstance();

    @Before
    public void init() {
        parser = DeleteCommandParser.getInstance();
    }

    @Test
    public void testGetInstance() throws Exception {
        assertSame(parser, DeleteCommandParser.getInstance());
    }

    @Test
    public void testValidDeleteInput() {
        Command command = parser.build("Delete 1");
        DeleteTaskCommand deleteCommand = validateAndReturnDeleteCommand(command);
        validateDeleteType(deleteCommand, 1);

        command = parser.build("delete 1 2 3");
        deleteCommand = validateAndReturnDeleteCommand(command);
        validateDeleteType(deleteCommand, 1, 2, 3);

        command = parser.build("Delete 1, 2, 3");
        deleteCommand = validateAndReturnDeleteCommand(command);
        validateDeleteType(deleteCommand, 1, 2, 3);

        command = parser.build("Delete 1, 2    3");
        deleteCommand = validateAndReturnDeleteCommand(command);
        validateDeleteType(deleteCommand, 1, 2, 3);

        command = parser.build("deLETE 1, 2-9, 20, 43");
        deleteCommand = validateAndReturnDeleteCommand(command);
        validateDeleteType(deleteCommand, 1, 2, 3, 4, 5, 6, 7, 8, 9, 20, 43);

        command = parser.build("Delete 1-3 2-9");
        deleteCommand = validateAndReturnDeleteCommand(command);
        validateDeleteType(deleteCommand, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        command = parser.build("Delete 1    -3,   2-  9");
        deleteCommand = validateAndReturnDeleteCommand(command);
        validateDeleteType(deleteCommand, 1, 2, 3, 4, 5, 6, 7, 8, 9);
    }

    @Test
    public void testInvalidDeleteInput() {
        Command command = parser.build("Delete");
        Assert.assertTrue(command instanceof InvalidCommand);

        command = parser.build("Delete 11-9");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: wrong command
        try {
            command = parser.build("List 1-8");
        } catch (AssertionError e) {
            Assert.assertTrue(true);
        }

        // Boundary case: empty string
        try {
            command = parser.build("");
        } catch (AssertionError e) {
            Assert.assertTrue(true);
        }

        // Boundary case: null
        try {
            command = parser.build(null);
        } catch (AssertionError e) {
            Assert.assertTrue(true);
        }
    }

    private DeleteTaskCommand validateAndReturnDeleteCommand(Command cmd) {
        Assert.assertNotNull(cmd);
        Assert.assertTrue(cmd instanceof DeleteTaskCommand);

        return (DeleteTaskCommand) cmd;
    }

    private void validateDeleteType(DeleteTaskCommand deleteCommand,
        int... screenIds) {
        int[] screenIdsInDeleteCommand = deleteCommand.getScreenIds().stream()
            .mapToInt(i -> i).toArray();
        Arrays.sort(screenIds);
        Arrays.sort(screenIdsInDeleteCommand);
        Assert.assertArrayEquals(screenIds, screenIdsInDeleteCommand);
    }
}
