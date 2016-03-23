package jfdi.test.logic.events;

import jfdi.logic.events.AliasDoneEvent;
import org.junit.Test;

import static javafx.scene.input.KeyCode.COMMAND;
import static org.junit.Assert.*;

/**
 * @author Xinan
 */
public class AliasDoneEventTest {

    private static final String COMMAND = "delete";
    private static final String ALIAS = "del";

    @Test
    public void getCommand() throws Exception {
        AliasDoneEvent event = new AliasDoneEvent(COMMAND, ALIAS);
        assertEquals(COMMAND, event.getCommand());
    }

    @Test
    public void getAlias() throws Exception {
        AliasDoneEvent event = new AliasDoneEvent(COMMAND, ALIAS);
        assertEquals(ALIAS, event.getAlias());
    }
}
