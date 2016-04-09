// @@author A0130195M

package jfdi.test.logic.events;

import jfdi.logic.events.InvalidCommandEvent;
import jfdi.parser.Constants;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Liu Xinan
 */
public class InvalidCommandEventTest {

    @Test
    public void getInputString() throws Exception {
        InvalidCommandEvent event = new InvalidCommandEvent("WTF", null);
        assertEquals("WTF", event.getInputString());
    }

    @Test
    public void getCommandType() throws Exception {
        InvalidCommandEvent event = new InvalidCommandEvent("undo redo", Constants.CommandType.UNDO);
        assertEquals(Constants.CommandType.UNDO, event.getCommandType());
    }

}
