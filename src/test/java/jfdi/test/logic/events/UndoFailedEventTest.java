// @@author A0130195M

package jfdi.test.logic.events;

import jfdi.logic.events.UndoFailedEvent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Liu Xinan
 */
public class UndoFailedEventTest {

    @Test
    public void getError() throws Exception {
        UndoFailedEvent event = new UndoFailedEvent(UndoFailedEvent.Error.NONTHING_TO_UNDO);
        assertEquals(UndoFailedEvent.Error.NONTHING_TO_UNDO, event.getError());
    }

    @Test
    public void testEnum() throws Exception {
        for (UndoFailedEvent.Error error : UndoFailedEvent.Error.values()) {
            // Force a full coverage on enums
            UndoFailedEvent.Error.valueOf(error.toString());
        }
    }

}
