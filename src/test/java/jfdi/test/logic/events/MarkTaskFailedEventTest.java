package jfdi.test.logic.events;

import jfdi.logic.events.MarkTaskFailedEvent;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * @@author Liu Xinan
 */
public class MarkTaskFailedEventTest {

    @Mock
    private ArrayList<Integer> screenIds;

    @Mock
    private ArrayList<Integer> invalidIds;

    private MarkTaskFailedEvent.Error error = MarkTaskFailedEvent.Error.NON_EXISTENT_ID;

    @Test
    public void getScreenIds() throws Exception {
        MarkTaskFailedEvent event = new MarkTaskFailedEvent(screenIds, invalidIds);
        assertEquals(screenIds, event.getScreenIds());
    }

    @Test
    public void getInvalidIds() throws Exception {
        MarkTaskFailedEvent event = new MarkTaskFailedEvent(screenIds, invalidIds);
        assertEquals(invalidIds, event.getInvalidIds());
    }

    @Test
    public void getError() throws Exception {
        MarkTaskFailedEvent event = new MarkTaskFailedEvent(screenIds, invalidIds);
        assertEquals(error, event.getError());
    }

}
