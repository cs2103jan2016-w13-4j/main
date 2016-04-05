// @@author A0130195M

package jfdi.test.logic.events;

import jfdi.logic.events.RenameTaskDoneEvent;
import jfdi.storage.apis.TaskAttributes;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;

/**
 * @author Liu Xinan
 */
public class RenameTaskDoneEventTest {

    @Mock
    private TaskAttributes task;

    @Test
    public void getTask() throws Exception {
        RenameTaskDoneEvent event = new RenameTaskDoneEvent(task);
        assertEquals(task, event.getTask());
    }

}
