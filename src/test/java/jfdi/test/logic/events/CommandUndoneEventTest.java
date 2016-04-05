// @@author A0130195M
package jfdi.test.logic.events;

import jfdi.logic.commands.DeleteTaskCommand;
import jfdi.logic.events.CommandUndoneEvent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Liu Xinan
 */
public class CommandUndoneEventTest {

    @Test
    public void getCommandType() throws Exception {
        CommandUndoneEvent event = new CommandUndoneEvent(DeleteTaskCommand.class);
        assertEquals(DeleteTaskCommand.class, event.getCommandType());
    }

}
