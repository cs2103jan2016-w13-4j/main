package jfdi.test.logic.events;

import jfdi.logic.events.UndoFailedEvent;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Xinan
 */
public class UndoFailedEventTest {

    @Test
    public void getError() throws Exception {
        UndoFailedEvent event = new UndoFailedEvent(UndoFailedEvent.Error.NONTHING_TO_UNDO);
        assertEquals(UndoFailedEvent.Error.NONTHING_TO_UNDO, event.getError());
    }

}
