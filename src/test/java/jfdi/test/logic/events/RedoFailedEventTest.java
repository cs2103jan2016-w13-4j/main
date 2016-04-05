// @@author A0130195M

package jfdi.test.logic.events;

import jfdi.logic.events.RedoFailedEvent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Liu Xinan
 */
public class RedoFailedEventTest {

    @Test
    public void getError() throws Exception {
        RedoFailedEvent event = new RedoFailedEvent(RedoFailedEvent.Error.NONTHING_TO_REDO);
        assertEquals(RedoFailedEvent.Error.NONTHING_TO_REDO, event.getError());
    }

}
