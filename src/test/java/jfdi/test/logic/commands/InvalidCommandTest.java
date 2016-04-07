package jfdi.test.logic.commands;

import jfdi.logic.commands.InvalidCommand;
import jfdi.logic.events.InvalidCommandEvent;
import jfdi.parser.Constants;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

/**
 * @author Liu Xinan
 */
public class InvalidCommandTest extends CommonCommandTest {

    @Test
    public void testBuilder() throws Exception {
        InvalidCommand command1 = new InvalidCommand.Builder()
            .setInputString("never gonna give")
            .setCommandType(null)
            .build();

        assertEquals("never gonna give", command1.getInputString());
        assertNull(command1.getCommandType());

        InvalidCommand command2 = new InvalidCommand.Builder()
            .setInputString("list processing")
            .setCommandType(Constants.CommandType.LIST)
            .build();

        assertEquals("list processing", command2.getInputString());
        assertEquals(Constants.CommandType.LIST, command2.getCommandType());
    }

    @Test
    public void execute() throws Exception {
        InvalidCommand command = new InvalidCommand.Builder()
            .setInputString("never gonna give")
            .setCommandType(null)
            .build();

        command.execute();

        verify(eventBus).post(any(InvalidCommandEvent.class));
    }

    @Test
    public void undo() throws Exception {
        InvalidCommand command = new InvalidCommand.Builder()
            .setInputString("list processing")
            .setCommandType(Constants.CommandType.LIST)
            .build();

        thrown.expect(AssertionError.class);
        command.undo();
    }

}
