// @@author A0130195M

package jfdi.test.logic.events;

import jfdi.logic.events.UnmarkTaskFailedEvent;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * @author Liu Xinan
 */
public class UnmarkTaskFailedEventTest {

    @Mock
    private ArrayList<Integer> screenIds;

    @Mock
    private ArrayList<Integer> invalidIds;

    private UnmarkTaskFailedEvent.Error error = UnmarkTaskFailedEvent.Error.NON_EXISTENT_ID;

    @Test
    public void getScreenIds() throws Exception {
        UnmarkTaskFailedEvent event = new UnmarkTaskFailedEvent(screenIds, invalidIds);
        assertEquals(screenIds, event.getScreenIds());
    }

    @Test
    public void getInvalidIds() throws Exception {
        UnmarkTaskFailedEvent event = new UnmarkTaskFailedEvent(screenIds, invalidIds);
        assertEquals(invalidIds, event.getInvalidIds());
    }

    @Test
    public void getError() throws Exception {
        UnmarkTaskFailedEvent event = new UnmarkTaskFailedEvent(screenIds, invalidIds);
        assertEquals(error, event.getError());
    }

    @Test
    public void testEnum() throws Exception {
        for (UnmarkTaskFailedEvent.Error error : UnmarkTaskFailedEvent.Error.values()) {
            // Force a full coverage on enums
            UnmarkTaskFailedEvent.Error.valueOf(error.toString());
        }
    }

}
