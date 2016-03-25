package jfdi.test.parser;

import static org.junit.Assert.assertSame;

import java.time.LocalDateTime;

import jfdi.logic.commands.AddTaskCommand;
import jfdi.logic.commands.InvalidCommand;
import jfdi.logic.interfaces.Command;
import jfdi.parser.DateTimeObject;
import jfdi.parser.DateTimeParser;
import jfdi.parser.commandparsers.AbstractCommandParser;
import jfdi.parser.commandparsers.AddCommandParser;
import jfdi.parser.exceptions.BadDateTimeException;

import org.junit.Assert;
import org.junit.Test;
import org.testng.annotations.BeforeClass;

public class AddCommandParserTest {
    AbstractCommandParser parser = AddCommandParser.getInstance();

    @BeforeClass
    public void init() {
        parser = AddCommandParser.getInstance();
    }

    @Test
    public void testGetInstance() throws Exception {
        assertSame(parser, AddCommandParser.getInstance());
    }

    @Test
    public void testValidAddInputWithoutDateTime() {
        // Equivalance class: valid inputs
        Command cmd = parser.build("add hello");
        AddTaskCommand addTaskCommand = validateAndReturnAddCommand(cmd);
        validateDescription(addTaskCommand, "hello");
        Assert.assertFalse(hasStartDateTime(addTaskCommand));
        Assert.assertFalse(hasEndDateTime(addTaskCommand));

        // Boundary case: capitalised add keyword
        cmd = parser.build("ADD hello, it's me.");
        addTaskCommand = validateAndReturnAddCommand(cmd);
        validateDescription(addTaskCommand, "hello, it's me.");
        Assert.assertFalse(hasStartDateTime(addTaskCommand));
        Assert.assertFalse(hasEndDateTime(addTaskCommand));

        // Boundary case: no 'add' keyword
        cmd = parser.build("This should parse as an add command.");
        addTaskCommand = validateAndReturnAddCommand(cmd);
        validateDescription(addTaskCommand,
            "This should parse as an add command.");
        Assert.assertFalse(hasStartDateTime(addTaskCommand));
        Assert.assertFalse(hasEndDateTime(addTaskCommand));

        // Boundary case: With escape delimiters
        cmd = parser.build("\"This should parse as an add command.\"");
        addTaskCommand = validateAndReturnAddCommand(cmd);
        validateDescription(addTaskCommand,
            "This should parse as an add command.");
        Assert.assertFalse(hasStartDateTime(addTaskCommand));
        Assert.assertFalse(hasEndDateTime(addTaskCommand));

        // Boundary case: With escape delimiters
        cmd = parser
            .build("\"The date time here should not be parsed by tomorrow\"");
        addTaskCommand = validateAndReturnAddCommand(cmd);
        validateDescription(addTaskCommand,
            "The date time here should not be parsed by tomorrow");
        Assert.assertFalse(hasStartDateTime(addTaskCommand));
        Assert.assertFalse(hasEndDateTime(addTaskCommand));

        // Boundary case: symbols
        cmd = parser.build("&%&^%%*@^#!)!@#()\\@*@)_    @#@#***");
        addTaskCommand = validateAndReturnAddCommand(cmd);
        validateDescription(addTaskCommand,
            "&%&^%%*@^#!)!@#()\\@*@)_    @#@#***");
        Assert.assertFalse(hasStartDateTime(addTaskCommand));
        Assert.assertFalse(hasEndDateTime(addTaskCommand));

        // Boundary case: numbers
        cmd = parser.build("12381209474");
        addTaskCommand = validateAndReturnAddCommand(cmd);
        validateDescription(addTaskCommand, "12381209474");
        Assert.assertFalse(hasStartDateTime(addTaskCommand));
        Assert.assertFalse(hasEndDateTime(addTaskCommand));
    }

    @Test
    public void testValidAddInputWithDateTime() {
        // Equivalence class: valid inputs with date-time
        // covers the three types of date-time tasks: deadline, event, point
        Command addCommand = parser
            .build("add watch how i met your mother by tomorrow");
        AddTaskCommand addTaskCommand = validateAndReturnAddCommand(addCommand);
        validateDescription(addTaskCommand, "watch how i met your mother");
        Assert.assertFalse(hasStartDateTime(addTaskCommand));
        Assert.assertTrue(hasEndDateTime(addTaskCommand));
        Assert.assertEquals(getEndDateTime(addTaskCommand),
            getEndDateTime("by tomorrow"));

        addCommand = parser.build("go to bed at 9pm");
        addTaskCommand = validateAndReturnAddCommand(addCommand);
        validateDescription(addTaskCommand, "go to bed");
        Assert.assertTrue(hasStartDateTime(addTaskCommand));
        Assert.assertFalse(hasEndDateTime(addTaskCommand));
        Assert.assertEquals(getStartDateTime(addTaskCommand),
            getStartDateTime("at 9pm"));

        addCommand = parser.build("play Goat Simulator from 4pm to 11pm");
        addTaskCommand = validateAndReturnAddCommand(addCommand);
        validateDescription(addTaskCommand, "play Goat Simulator");
        Assert.assertTrue(hasStartDateTime(addTaskCommand));
        Assert.assertTrue(hasEndDateTime(addTaskCommand));
        Assert.assertEquals(getStartDateTime(addTaskCommand),
            getStartDateTime("from 4pm to 11pm"));
        Assert.assertEquals(getEndDateTime(addTaskCommand),
            getEndDateTime("from 4pm to 11pm"));

        // Boundary case: with escape delimiters
        addCommand = parser.build("\"play Goat Simulator\" from 4pm to 11pm");
        addTaskCommand = validateAndReturnAddCommand(addCommand);
        validateDescription(addTaskCommand, "play Goat Simulator");
        Assert.assertTrue(hasStartDateTime(addTaskCommand));
        Assert.assertTrue(hasEndDateTime(addTaskCommand));
        Assert.assertEquals(getStartDateTime(addTaskCommand),
            getStartDateTime("from 4pm to 11pm"));
        Assert.assertEquals(getEndDateTime(addTaskCommand),
            getEndDateTime("from 4pm to 11pm"));

    }

    @Test
    public void testInvalidInput() {
        // Boundary case: no description and no date-time
        Command addCommand = parser.build("add");
        Assert.assertTrue(addCommand instanceof InvalidCommand);

        // Boundary case: no description
        addCommand = parser.build("add from 5pm to 6pm");
        Assert.assertTrue(addCommand instanceof InvalidCommand);

        // Boundary case: with delimiters, wrapped around nothing
        addCommand = parser.build("\"\" from 5pm to 6pm");
        Assert.assertTrue(addCommand instanceof InvalidCommand);

        // Boundary case: null
        try {
            addCommand = parser.build(null);
        } catch (AssertionError e) {
            Assert.assertTrue(true);
        }

        // Boundary case: Empty string
        try {
            addCommand = parser.build("");
        } catch (AssertionError e) {
            Assert.assertTrue(true);
        }

    }

    private AddTaskCommand validateAndReturnAddCommand(Command cmd) {
        Assert.assertNotNull(cmd);
        Assert.assertTrue(cmd instanceof AddTaskCommand);

        return (AddTaskCommand) cmd;
    }

    private void validateDescription(AddTaskCommand addTaskCommand,
        String expected) {
        Assert.assertEquals(addTaskCommand.getDescription(), expected);
    }

    private boolean hasStartDateTime(AddTaskCommand addTaskCommand) {
        return addTaskCommand.getStartDateTime().isPresent();
    }

    private LocalDateTime getStartDateTime(AddTaskCommand addTaskCommand) {
        return addTaskCommand.getStartDateTime().get();
    }

    private LocalDateTime getStartDateTime(String input) {
        DateTimeObject res = null;
        try {
            res = DateTimeParser.getInstance().parseDateTime(input);
        } catch (BadDateTimeException e) {
            Assert.fail();
        }

        return res.getStartDateTime();
    }

    private boolean hasEndDateTime(AddTaskCommand addTaskCommand) {
        return addTaskCommand.getEndDateTime().isPresent();
    }

    private LocalDateTime getEndDateTime(AddTaskCommand addTaskCommand) {
        return addTaskCommand.getEndDateTime().get();
    }

    private LocalDateTime getEndDateTime(String input) {
        DateTimeObject res = null;
        try {
            res = DateTimeParser.getInstance().parseDateTime(input);
        } catch (BadDateTimeException e) {
            Assert.fail();
        }

        return res.getEndDateTime();
    }

}
