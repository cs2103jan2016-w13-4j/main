package jfdi.test.logic.events;

import jfdi.logic.events.AddTaskFailedEvent;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Xinan
 */

public class AddTaskFailedEventTest {

    @Test
    public void getError() throws Exception {
        AddTaskFailedEvent event = new AddTaskFailedEvent(AddTaskFailedEvent.Error.EMPTY_DESCRIPTION);
        assertEquals(AddTaskFailedEvent.Error.EMPTY_DESCRIPTION, event.getError());
    }
}