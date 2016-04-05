// @@author A0130195M
package jfdi.test.logic.events;

import jfdi.logic.events.NoSurpriseEvent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Liu Xinan
 */
public class NoSurpriseEventTest {

    @Test
    public void getError() throws Exception {
        NoSurpriseEvent event = new NoSurpriseEvent(NoSurpriseEvent.Error.NO_TASKS);
        assertEquals(NoSurpriseEvent.Error.NO_TASKS, event.getError());
    }

}
