package jfdi.test.logic.events;

import jfdi.logic.events.RescheduleTaskDoneEvent;
import jfdi.storage.apis.TaskAttributes;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;

/**
 * @author Xinan
 */
public class RescheduleTaskDoneEventTest {

    @Mock
    private TaskAttributes task;

    @Test
    public void getTask() throws Exception {
        RescheduleTaskDoneEvent event = new RescheduleTaskDoneEvent(task);
        assertEquals(task, event.getTask());
    }

}
