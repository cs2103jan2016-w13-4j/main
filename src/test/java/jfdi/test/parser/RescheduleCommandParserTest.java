// @@author A0127393B

package jfdi.test.parser;

import static org.junit.Assert.assertSame;

import java.time.LocalDateTime;

import jfdi.logic.commands.InvalidCommand;
import jfdi.logic.commands.RescheduleTaskCommand;
import jfdi.logic.interfaces.Command;
import jfdi.parser.DateTimeObject;
import jfdi.parser.DateTimeParser;
import jfdi.parser.commandparsers.AbstractCommandParser;
import jfdi.parser.commandparsers.RescheduleCommandParser;
import jfdi.parser.exceptions.BadDateTimeException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RescheduleCommandParserTest {

    AbstractCommandParser parser = RescheduleCommandParser.getInstance();
    DateTimeParser dateTimeParser = DateTimeParser.getInstance();

    @Before
    public void init() {
        parser = RescheduleCommandParser.getInstance();
    }

    @Test
    public void testGetInstance() throws Exception {
        assertSame(parser, RescheduleCommandParser.getInstance());
    }

    @Test
    public void testValidCommand() {
        Command command = parser.build("reschedule 1 to 5pm");
        Assert.assertTrue(command instanceof RescheduleTaskCommand);
        RescheduleTaskCommand rescheduleCommand = (RescheduleTaskCommand) command;
        DateTimeObject dateTimeObject = getDateTimeObject("5pm");
        assertSameFields(rescheduleCommand, 1, false, true, dateTimeObject, null, null);

        command = parser.build("reschedule 1 5pm");
        Assert.assertTrue(command instanceof RescheduleTaskCommand);
        rescheduleCommand = (RescheduleTaskCommand) command;
        dateTimeObject = getDateTimeObject("5pm");
        assertSameFields(rescheduleCommand, 1, false, true, dateTimeObject, null, null);

        command = parser.build("Reschedule 123 to 23rd feb 2016 5pm");
        Assert.assertTrue(command instanceof RescheduleTaskCommand);
        rescheduleCommand = (RescheduleTaskCommand) command;
        dateTimeObject = getDateTimeObject("23rd feb 2016 5pm");
        assertSameFields(rescheduleCommand, 123, true, true, dateTimeObject, null, null);

        command = parser.build("RescheDULE 123 by 23rd feb 2016 5pm");
        Assert.assertTrue(command instanceof RescheduleTaskCommand);
        rescheduleCommand = (RescheduleTaskCommand) command;
        dateTimeObject = getDateTimeObject("by 23rd feb 2016 5pm");
        assertSameFields(rescheduleCommand, 123, true, true, null, dateTimeObject.getStartDateTime(),
            dateTimeObject.getEndDateTime());

        command = parser.build("reschedule 543457 at 23rd feb 2016");
        Assert.assertTrue(command instanceof RescheduleTaskCommand);
        rescheduleCommand = (RescheduleTaskCommand) command;
        dateTimeObject = getDateTimeObject("at 23rd feb 2016");
        assertSameFields(rescheduleCommand, 543457, true, false, null, dateTimeObject.getStartDateTime(),
            dateTimeObject.getEndDateTime());

        command = parser.build("reschedule 543457 to 23rd feb 2016 to 15 May 2016 7pm");
        Assert.assertTrue(command instanceof RescheduleTaskCommand);
        rescheduleCommand = (RescheduleTaskCommand) command;
        dateTimeObject = getDateTimeObject("23rd feb 2016 to 15 May 2016 7pm");
        assertSameFields(rescheduleCommand, 543457, true, true, null, dateTimeObject.getStartDateTime(),
            dateTimeObject.getEndDateTime());

        command = parser.build("reschedule 1");
        rescheduleCommand = (RescheduleTaskCommand) command;
        Assert.assertTrue(command instanceof RescheduleTaskCommand);
        assertSameFields(rescheduleCommand, 1, false, false, null, null, null);

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
        command = parser.build("reschedule");
        Assert.assertTrue(command instanceof InvalidCommand);
        command = parser.build("reschedule oneword");
        Assert.assertTrue(command instanceof InvalidCommand);
        command = parser.build("reschedule to 6pm");
        Assert.assertTrue(command instanceof InvalidCommand);
        command = parser.build("reschedule 1 to nonsense");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: empty string
        command = parser.build("");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: null
        command = parser.build(null);
        Assert.assertTrue(command instanceof InvalidCommand);
    }

    private void
        assertSameFields(RescheduleTaskCommand rescheduleTaskCommand, int taskId, boolean isDateSpecified,
            boolean isTimeSpecified, DateTimeObject shiftedDateTime, LocalDateTime startDateTime,
            LocalDateTime endDateTime) {
        Assert.assertEquals(rescheduleTaskCommand.getScreenId(), taskId);
        Assert.assertEquals(rescheduleTaskCommand.isShiftedDateSpecified(), isDateSpecified);
        Assert.assertEquals(rescheduleTaskCommand.isShiftedTimeSpecified(), isTimeSpecified);

        if (shiftedDateTime != null) {
            Assert.assertNull(shiftedDateTime.getEndDateTime());
            Assert.assertNotNull(shiftedDateTime.getStartDateTime());
            assertSameDateTime(rescheduleTaskCommand.getShiftedDateTime(), shiftedDateTime.getStartDateTime());
        } else {
            Assert.assertNull(rescheduleTaskCommand.getShiftedDateTime());
        }
        if (startDateTime != null) {
            assertSameDateTime(rescheduleTaskCommand.getStartDateTime(), startDateTime);
        } else {
            Assert.assertNull(rescheduleTaskCommand.getStartDateTime());
        }
        if (endDateTime != null) {
            assertSameDateTime(rescheduleTaskCommand.getEndDateTime(), endDateTime);
        } else {
            Assert.assertNull(rescheduleTaskCommand.getEndDateTime());
        }

    }

    private void assertSameDateTime(LocalDateTime dateTime, LocalDateTime dateTime2) {
        Assert.assertNotNull(dateTime);
        Assert.assertNotNull(dateTime2);
        Assert.assertEquals(dateTime.getYear(), dateTime2.getYear());
        Assert.assertEquals(dateTime.getMonth(), dateTime2.getMonth());
        Assert.assertEquals(dateTime.getDayOfMonth(), dateTime2.getDayOfMonth());
        Assert.assertEquals(dateTime.getMinute(), dateTime2.getMinute());
        Assert.assertEquals(dateTime.getHour(), dateTime2.getHour());
    }

    private DateTimeObject getDateTimeObject(String dateTime) {
        DateTimeObject dateTimeObject = null;
        try {
            dateTimeObject = dateTimeParser.parseDateTime(dateTime);
        } catch (BadDateTimeException e) {
            Assert.fail();
        }
        return dateTimeObject;
    }

}
