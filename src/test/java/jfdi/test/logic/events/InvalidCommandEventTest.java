package jfdi.test.logic.events;

import jfdi.logic.events.InvalidCommandEvent;
import jfdi.parser.Constants;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Xinan
 */
public class InvalidCommandEventTest {

    @Test
    public void getInputString() throws Exception {
        InvalidCommandEvent event = new InvalidCommandEvent("WTF", null, null);
        assertEquals("WTF", event.getInputString());
    }

    @Test
    public void getCommandType() throws Exception {
        InvalidCommandEvent event = new InvalidCommandEvent("undo redo", Constants.CommandType.UNDO, "add undo redo");
        assertEquals(Constants.CommandType.UNDO, event.getCommandType());
    }

    @Test
    public void getSuggestion() throws Exception {
        InvalidCommandEvent event = new InvalidCommandEvent("list com", Constants.CommandType.LIST, "list completed");
        assertEquals("list completed", event.getSuggestion());
    }

}
