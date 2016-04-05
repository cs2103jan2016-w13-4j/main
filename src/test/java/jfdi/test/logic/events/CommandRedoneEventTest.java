package jfdi.test.logic.events;

import jfdi.logic.commands.AddTaskCommand;
import jfdi.logic.events.CommandRedoneEvent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @@author Liu Xinan
 */
public class CommandRedoneEventTest {

    @Test
    public void getCommandType() throws Exception {
        CommandRedoneEvent event = new CommandRedoneEvent(AddTaskCommand.class);
        assertEquals(AddTaskCommand.class, event.getCommandType());
    }

}
