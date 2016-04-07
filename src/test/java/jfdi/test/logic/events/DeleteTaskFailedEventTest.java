// @@author A0130195M

package jfdi.test.logic.events;

import jfdi.logic.events.DeleteTaskFailedEvent;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * @author Liu Xinan
 */
public class DeleteTaskFailedEventTest {

    @Mock
    private ArrayList<Integer> invalidIds;

    @Test
    public void getInvalidIds() throws Exception {
        DeleteTaskFailedEvent event = new DeleteTaskFailedEvent(invalidIds);
        assertEquals(invalidIds, event.getInvalidIds());
    }

    @Test
    public void getError() throws Exception {
        DeleteTaskFailedEvent event1 = new DeleteTaskFailedEvent(invalidIds);
        assertEquals(DeleteTaskFailedEvent.Error.NON_EXISTENT_ID, event1.getError());
    }

    @Test
    public void testEnum() throws Exception {
        for (DeleteTaskFailedEvent.Error error : DeleteTaskFailedEvent.Error.values()) {
            // Force a full coverage on enums
            DeleteTaskFailedEvent.Error.valueOf(error.toString());
        }
    }
}
