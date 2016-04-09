// @@author A0127393B

package jfdi.test.parser;

import static org.junit.Assert.assertSame;
import jfdi.logic.commands.InvalidCommand;
import jfdi.logic.commands.ListCommand;
import jfdi.logic.commands.ListCommand.ListType;
import jfdi.logic.interfaces.Command;
import jfdi.parser.commandparsers.AbstractCommandParser;
import jfdi.parser.commandparsers.ListCommandParser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ListCommandParserTest {
    AbstractCommandParser parser = ListCommandParser.getInstance();

    @Before
    public void init() {
        parser = ListCommandParser.getInstance();
    }

    @Test
    public void testGetInstance() throws Exception {
        assertSame(parser, ListCommandParser.getInstance());
    }

    @Test
    public void testDefault() {
        Command cmd = parser.build("list");
        ListCommand listCommand = validateAndReturnListCommand(cmd);
        validateListType(listCommand, ListType.INCOMPLETE);

        // Boundary case: capitalised list keyword
        cmd = parser.build("LiSt");
        listCommand = validateAndReturnListCommand(cmd);
        validateListType(listCommand, ListType.INCOMPLETE);

        cmd = parser.build("LiSt incomplete");
        listCommand = validateAndReturnListCommand(cmd);
        validateListType(listCommand, ListType.INCOMPLETE);
    }

    @Test
    public void testAll() {
        Command cmd = parser.build("list all");
        ListCommand listCommand = validateAndReturnListCommand(cmd);
        validateListType(listCommand, ListType.ALL);

        // Boundary case: capitalised list keyword
        cmd = parser.build("LiSt AlL");
        listCommand = validateAndReturnListCommand(cmd);
        validateListType(listCommand, ListType.ALL);
    }

    @Test
    public void testCompleted() {
        Command cmd = parser.build("list completed");
        ListCommand listCommand = validateAndReturnListCommand(cmd);
        validateListType(listCommand, ListType.COMPLETED);

        // Boundary case: capitalised list keyword
        cmd = parser.build("LiSt CoMplETEd");
        listCommand = validateAndReturnListCommand(cmd);
        validateListType(listCommand, ListType.COMPLETED);
    }

    @Test
    public void testOverdue() {
        Command cmd = parser.build("list overdue");
        ListCommand listCommand = validateAndReturnListCommand(cmd);
        validateListType(listCommand, ListType.OVERDUE);

        // Boundary case: capitalised list keyword
        cmd = parser.build("LiSt overDUE");
        listCommand = validateAndReturnListCommand(cmd);
        validateListType(listCommand, ListType.OVERDUE);

        // Boundary case: alternative spelling + capitalisation
        cmd = parser.build("List Overdue");
        listCommand = validateAndReturnListCommand(cmd);
        validateListType(listCommand, ListType.OVERDUE);
    }

    @Test
    public void testUpcoming() {
        Command cmd = parser.build("list upcoming");
        ListCommand listCommand = validateAndReturnListCommand(cmd);
        validateListType(listCommand, ListType.UPCOMING);

        // Boundary case: capitalised list keyword
        cmd = parser.build("LiSt upCOMING");
        listCommand = validateAndReturnListCommand(cmd);
        validateListType(listCommand, ListType.UPCOMING);

        // Boundary case: alternative spelling + capitalisation
        cmd = parser.build("List Upcoming");
        listCommand = validateAndReturnListCommand(cmd);
        validateListType(listCommand, ListType.UPCOMING);
    }

    @Test
    public void testInvalid() {
        Command cmd = parser.build("list nonsense");
        Assert.assertTrue(cmd instanceof InvalidCommand);

        // Boundary case: numbers
        cmd = parser.build("list 12343121");
        Assert.assertTrue(cmd instanceof InvalidCommand);

        // Boundary case: symbols
        cmd = parser.build("LiSt *&(*&(*@$*^$");
        Assert.assertTrue(cmd instanceof InvalidCommand);

        // Boundary case: null
        cmd = parser.build("LiSt *&(*&(*@$*^$");
        Assert.assertTrue(cmd instanceof InvalidCommand);

        // Boundary case: symbols
        cmd = parser.build("LiSt *&(*&(*@$*^$");
        Assert.assertTrue(cmd instanceof InvalidCommand);

        // Boundary case: wrong command
        try {
            cmd = parser.build("delete 1-9");
        } catch (AssertionError e) {
            Assert.assertTrue(true);
        }

        // Boundary case: empty string
        try {
            cmd = parser.build("");
        } catch (AssertionError e) {
            Assert.assertTrue(true);
        }

        // Boundary case: null
        try {
            cmd = parser.build(null);
        } catch (AssertionError e) {
            Assert.assertTrue(true);
        }
    }

    private ListCommand validateAndReturnListCommand(Command cmd) {
        Assert.assertNotNull(cmd);
        Assert.assertTrue(cmd instanceof ListCommand);

        return (ListCommand) cmd;
    }

    private void validateListType(ListCommand listCommand, ListType listType) {
        Assert.assertEquals(listCommand.getListType(), listType);
    }

}
