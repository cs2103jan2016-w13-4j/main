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
        Command cmd = parser.build("add hello");
        AddTaskCommand addTaskCommand = validateAndReturnAddCommand(cmd);
        validateDescription(addTaskCommand, "hello");
        Assert.assertFalse(hasStartDateTime(addTaskCommand));
        Assert.assertFalse(hasEndDateTime(addTaskCommand));

        cmd = parser.build("add hello, it's me.");
        addTaskCommand = validateAndReturnAddCommand(cmd);
        validateDescription(addTaskCommand, "hello, it's me.");
        Assert.assertFalse(hasStartDateTime(addTaskCommand));
        Assert.assertFalse(hasEndDateTime(addTaskCommand));

        cmd = parser.build("This should parse as an add command.");
        addTaskCommand = validateAndReturnAddCommand(cmd);
        validateDescription(addTaskCommand,
            "This should parse as an add command.");
        Assert.assertFalse(hasStartDateTime(addTaskCommand));
        Assert.assertFalse(hasEndDateTime(addTaskCommand));

        cmd = parser.build("&%&^%%*@^#!)!@#()\\@*@)_    @#@#***");
        addTaskCommand = validateAndReturnAddCommand(cmd);
        validateDescription(addTaskCommand,
            "&%&^%%*@^#!)!@#()\\@*@)_    @#@#***");
        Assert.assertFalse(hasStartDateTime(addTaskCommand));
        Assert.assertFalse(hasEndDateTime(addTaskCommand));
    }

    @Test
    public void testValidAddInputWithDateTime() {
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
    }

    @Test
    public void testInvalidInput() {
        Command addCommand = parser.build("add");
        Assert.assertTrue(addCommand instanceof InvalidCommand);

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
