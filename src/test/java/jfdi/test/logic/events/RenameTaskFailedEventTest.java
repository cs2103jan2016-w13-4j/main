package jfdi.test.logic.events;

import jfdi.logic.events.RenameTaskFailedEvent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Xinan
 */
public class RenameTaskFailedEventTest {

    private int screenId = 666;
    private String description = "Task 666";
    private RenameTaskFailedEvent event = new RenameTaskFailedEvent(screenId, description,
        RenameTaskFailedEvent.Error.DUPLICATED_TASK);

    @Test
    public void getScreenId() throws Exception {
        assertEquals(screenId, event.getScreenId());
    }

    @Test
    public void getDescription() throws Exception {
        assertEquals(description, event.getDescription());
    }

    @Test
    public void getError() throws Exception {
        assertEquals(RenameTaskFailedEvent.Error.DUPLICATED_TASK, event.getError());
    }

}
