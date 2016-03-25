package jfdi.test.logic.events;

import jfdi.logic.events.UnmarkTaskFailEvent;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * @author Xinan
 */
public class UnmarkTaskFailEventTest {

    @Mock
    private ArrayList<Integer> screenIds;

    @Mock
    private ArrayList<Integer> invalidIds;

    private UnmarkTaskFailEvent.Error error = UnmarkTaskFailEvent.Error.NON_EXISTENT_ID;

    @Test
    public void getScreenIds() throws Exception {
        UnmarkTaskFailEvent event = new UnmarkTaskFailEvent(screenIds, invalidIds);
        assertEquals(screenIds, event.getScreenIds());
    }

    @Test
    public void getInvalidIds() throws Exception {
        UnmarkTaskFailEvent event = new UnmarkTaskFailEvent(screenIds, invalidIds);
        assertEquals(invalidIds, event.getInvalidIds());
    }

    @Test
    public void getError() throws Exception {
        UnmarkTaskFailEvent event = new UnmarkTaskFailEvent(screenIds, invalidIds);
        assertEquals(error, event.getError());
    }

}
