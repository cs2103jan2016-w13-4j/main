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

    @Test
    public void testEnum() throws Exception {
        for (NoSurpriseEvent.Error error : NoSurpriseEvent.Error.values()) {
            // Force a full coverage on enums
            NoSurpriseEvent.Error.valueOf(error.toString());
        }
    }

}
