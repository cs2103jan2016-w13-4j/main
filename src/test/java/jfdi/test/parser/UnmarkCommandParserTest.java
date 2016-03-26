// @@author A0127393B

package jfdi.test.parser;

import static org.junit.Assert.assertSame;
import jfdi.logic.commands.InvalidCommand;
import jfdi.logic.commands.UnmarkTaskCommand;
import jfdi.logic.interfaces.Command;
import jfdi.parser.commandparsers.AbstractCommandParser;
import jfdi.parser.commandparsers.UnmarkCommandParser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.emory.mathcs.backport.java.util.Arrays;

public class UnmarkCommandParserTest {

    AbstractCommandParser parser = UnmarkCommandParser.getInstance();

    @Before
    public void init() {
        parser = UnmarkCommandParser.getInstance();
    }

    @Test
    public void testGetInstance() throws Exception {
        assertSame(parser, UnmarkCommandParser.getInstance());
    }

    @Test
    public void testValidUnmarkInput() {
        Command command = parser.build("Unmark 1");
        UnmarkTaskCommand unmarkCommand = validateAndReturnUnmarkCommand(command);
        validateUnmarkType(unmarkCommand, 1);

        command = parser.build("unmark 1 2 3");
        unmarkCommand = validateAndReturnUnmarkCommand(command);
        validateUnmarkType(unmarkCommand, 1, 2, 3);

        command = parser.build("Unmark 1, 2, 3");
        unmarkCommand = validateAndReturnUnmarkCommand(command);
        validateUnmarkType(unmarkCommand, 1, 2, 3);

        command = parser.build("Unmark 1, 2    3");
        unmarkCommand = validateAndReturnUnmarkCommand(command);
        validateUnmarkType(unmarkCommand, 1, 2, 3);

        command = parser.build("unmARk 1, 2-9, 20, 43");
        unmarkCommand = validateAndReturnUnmarkCommand(command);
        validateUnmarkType(unmarkCommand, 1, 2, 3, 4, 5, 6, 7, 8, 9, 20, 43);

        command = parser.build("Unmark 1-3 2-9");
        unmarkCommand = validateAndReturnUnmarkCommand(command);
        validateUnmarkType(unmarkCommand, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        command = parser.build("Unmark 1    -3,   2-  9");
        unmarkCommand = validateAndReturnUnmarkCommand(command);
        validateUnmarkType(unmarkCommand, 1, 2, 3, 4, 5, 6, 7, 8, 9);
    }

    @Test
    public void testInvalidUnmarkInput() {
        Command command = parser.build("Unmark");
        Assert.assertTrue(command instanceof InvalidCommand);

        command = parser.build("Unmark 11-9");
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

    private UnmarkTaskCommand validateAndReturnUnmarkCommand(Command cmd) {
        Assert.assertNotNull(cmd);
        Assert.assertTrue(cmd instanceof UnmarkTaskCommand);

        return (UnmarkTaskCommand) cmd;
    }

    private void validateUnmarkType(UnmarkTaskCommand unmarkCommand,
        int... screenIds) {
        int[] screenIdsInUnmarkCommand = unmarkCommand.getScreenIds().stream()
            .mapToInt(i -> i).toArray();
        Arrays.sort(screenIds);
        Arrays.sort(screenIdsInUnmarkCommand);
        Assert.assertArrayEquals(screenIds, screenIdsInUnmarkCommand);
    }
}
