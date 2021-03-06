// @@author A0130195M

package jfdi.test.logic.events;

import jfdi.logic.events.AddTaskDoneEvent;
import jfdi.storage.apis.TaskAttributes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertSame;

/**
 * @author Liu Xinan
 */
@RunWith(MockitoJUnitRunner.class)
public class AddTaskDoneEventTest {

    @Mock
    private TaskAttributes task;

    @Test
    public void getTask() throws Exception {
        AddTaskDoneEvent event = new AddTaskDoneEvent(task);
        assertSame(task, event.getTask());
    }

}
