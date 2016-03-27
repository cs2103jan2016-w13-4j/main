// @@author A0127393B

package jfdi.test.parser;

import static org.junit.Assert.assertSame;
import jfdi.logic.commands.InvalidCommand;
import jfdi.logic.commands.MarkTaskCommand;
import jfdi.logic.interfaces.Command;
import jfdi.parser.commandparsers.AbstractCommandParser;
import jfdi.parser.commandparsers.MarkCommandParser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.emory.mathcs.backport.java.util.Arrays;

public class MarkCommandParserTest {

    AbstractCommandParser parser = MarkCommandParser.getInstance();

    @Before
    public void init() {
        parser = MarkCommandParser.getInstance();
    }

    @Test
    public void testGetInstance() throws Exception {
        assertSame(parser, MarkCommandParser.getInstance());
    }

    @Test
    public void testValidMarkInput() {
        Command command = parser.build("Mark 1");
        MarkTaskCommand markCommand = validateAndReturnMarkCommand(command);
        validateMarkType(markCommand, 1);

        command = parser.build("mark 1 2 3");
        markCommand = validateAndReturnMarkCommand(command);
        validateMarkType(markCommand, 1, 2, 3);

        command = parser.build("Mark 1, 2, 3");
        markCommand = validateAndReturnMarkCommand(command);
        validateMarkType(markCommand, 1, 2, 3);

        command = parser.build("Mark 1, 2    3");
        markCommand = validateAndReturnMarkCommand(command);
        validateMarkType(markCommand, 1, 2, 3);

        command = parser.build("mARk 1, 2-9, 20, 43");
        markCommand = validateAndReturnMarkCommand(command);
        validateMarkType(markCommand, 1, 2, 3, 4, 5, 6, 7, 8, 9, 20, 43);

        command = parser.build("Mark 1-3 2-9");
        markCommand = validateAndReturnMarkCommand(command);
        validateMarkType(markCommand, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        command = parser.build("Mark 1    -3,   2-  9");
        markCommand = validateAndReturnMarkCommand(command);
        validateMarkType(markCommand, 1, 2, 3, 4, 5, 6, 7, 8, 9);
    }

    @Test
    public void testInvalidMarkInput() {
        Command command = parser.build("Mark");
        Assert.assertTrue(command instanceof InvalidCommand);

        command = parser.build("Mark 11-9");
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

    private MarkTaskCommand validateAndReturnMarkCommand(Command cmd) {
        Assert.assertNotNull(cmd);
        Assert.assertTrue(cmd instanceof MarkTaskCommand);

        return (MarkTaskCommand) cmd;
    }

    private void validateMarkType(MarkTaskCommand markCommand, int... screenIds) {
        int[] screenIdsInMarkCommand = markCommand.getScreenIds().stream()
            .mapToInt(i -> i).toArray();
        Arrays.sort(screenIds);
        Arrays.sort(screenIdsInMarkCommand);
        Assert.assertArrayEquals(screenIds, screenIdsInMarkCommand);
    }
}
